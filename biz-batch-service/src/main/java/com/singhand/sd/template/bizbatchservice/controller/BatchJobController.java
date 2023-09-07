package com.singhand.sd.template.bizbatchservice.controller;

import cn.hutool.core.util.StrUtil;
import com.singhand.sd.template.bizmodel.model.response.OperationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RestController
@RequestMapping("batch-job")
@Tag(name = "批量处理")
@Slf4j
public class BatchJobController {

    private final JobExplorer jobExplorer;

    private final JobRepository jobRepository;

    private final JobLauncher jobLauncher;

    private final Job exportDocumentJob;


    @Autowired
    public BatchJobController(JobExplorer jobExplorer, JobRepository jobRepository,
                              JobLauncher jobLauncher,
                              Job exportDocumentJob) {

        this.jobExplorer = jobExplorer;
        this.jobRepository = jobRepository;
        this.jobLauncher = jobLauncher;
        this.exportDocumentJob = exportDocumentJob;
    }

    @GetMapping("{id}")
    public OperationResponse getExecutionStatus(@PathVariable Long id) {

        final var jobExecution = jobExplorer.getJobExecution(id);
        return jobExecution2OperationResponse(Optional.ofNullable(jobExecution)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @GetMapping("{name}/uuid/{uuid}")
    public OperationResponse getExecutionStatusByJobNameAndUuid(@PathVariable String name,
                                                                @PathVariable String uuid) {

        final var jobExecution = jobRepository.getLastJobExecution(name,
                new JobParametersBuilder().addString("instance_id", uuid, true).toJobParameters());
        if (jobExecution == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "未找到任务 " + uuid);
        }
        return jobExecution2OperationResponse(jobExecution);
    }


    @Operation(summary = "后台任务查询")
    @GetMapping("latest")
    @SneakyThrows
    public Collection<OperationResponse> getJobs(@RequestParam String username,
                                                 @RequestParam String jobName) {

        final var jobInstanceCount = jobExplorer.getJobInstanceCount(jobName);
        final var jobExecution = jobExplorer.getJobInstances(jobName, 0, (int) jobInstanceCount)
                .stream()
                .map(jobExplorer::getLastJobExecution)
                .filter(Objects::nonNull)
                .filter(it -> username.equals(it.getJobParameters().getString("username", "")))
                .sorted(Comparator.comparing(JobExecution::getCreateTime))
                .reduce((a, b) -> b);

        if (jobExecution.isEmpty()) {
            return List.of();
        }

        if (jobName.equals("importDatasourceJob")) {
            final var groupId = jobExecution.get().getJobParameters().getString("group_id", "");
            if (StrUtil.isBlank(groupId)) {
                return List.of();
            }

            return jobExplorer.getJobInstances(jobName, 0, (int) jobInstanceCount)
                    .stream()
                    .map(jobExplorer::getLastJobExecution)
                    .filter(Objects::nonNull)
                    .filter(it -> groupId.equals(it.getJobParameters().getString("group_id", "")))
                    .map(this::jobExecution2OperationResponse)
                    .toList();
        }

        return jobExplorer.getJobInstances(jobName, 0, (int) jobInstanceCount)
                .stream()
                .map(jobExplorer::getLastJobExecution)
                .filter(Objects::nonNull)
                .map(this::jobExecution2OperationResponse)
                .toList();
    }


    @Operation(summary = "想定文档导出")
    @PostMapping("/document/{id}/user/{username}:export")
    @SneakyThrows
    public OperationResponse exportDocument(@PathVariable("id") Long id,
                                            @PathVariable String username) {

        final var report = jobLauncher.run(exportDocumentJob, new JobParametersBuilder()
                .addLong("id", id, false)
                .addString("username", username, false)
                .addString("instance_id", UUID.randomUUID().toString(), true)
                .toJobParameters());

        return jobExecution2OperationResponse(report);
    }

    private OperationResponse jobExecution2OperationResponse(JobExecution jobExecution) {

        final var operationResponse = new OperationResponse();
        operationResponse.setId(jobExecution.getId());
        operationResponse.setStatus(jobExecution.getStatus().toString());
        operationResponse.setExitCode(jobExecution.getExitStatus().getExitCode());
        operationResponse.setName(jobExecution.getJobInstance().getJobName());
        operationResponse.setStartTime(jobExecution.getStartTime() == null ? null
                : Timestamp.valueOf(jobExecution.getStartTime()).getTime());
        operationResponse.setEndTime(jobExecution.getEndTime() == null ? null
                : Timestamp.valueOf(jobExecution.getEndTime()).getTime());
        operationResponse.setParameters(
                jobExecution.getJobParameters().getParameters().entrySet().stream()
                        .collect(Collectors.toMap(Entry::getKey,
                                // IDEA 的 bug getValue().getValue() 可能为空
                                // toMap 的 key 和 value 不可为空
                                it -> it.getValue().getValue() == null ? "" : it.getValue().getValue())));
        jobExecution.getExecutionContext().entrySet()
                .forEach(it -> operationResponse.getCtx().put(it.getKey(), it.getValue()));
        return operationResponse;
    }
}
