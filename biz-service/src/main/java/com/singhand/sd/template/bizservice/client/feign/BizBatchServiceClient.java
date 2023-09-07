package com.singhand.sd.template.bizservice.client.feign;

import com.singhand.sd.template.bizmodel.model.response.OperationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@FeignClient(value = "biz-batch-service", path = "/api/v1")
public interface BizBatchServiceClient {

  @GetMapping("/batch-job/{id}")
  OperationResponse getExecutionStatus(@PathVariable Long id);

  @GetMapping("/batch-job/{name}/uuid/{uuid}")
  OperationResponse getExecutionStatusByJobNameAndUuid(@PathVariable String name,
      @PathVariable String uuid);

  @PostMapping("/batch-job/log/user/{username}:export")
  OperationResponse launchExportLogsJob(
      @RequestParam(defaultValue = "") Set<String> modules,
      @RequestParam(defaultValue = "") String type,
      @RequestParam(defaultValue = "") String ip,
      @RequestParam(defaultValue = "") String operator,
      @RequestParam(defaultValue = "") String q,
      @RequestParam(defaultValue = "") String fromDate,
      @RequestParam(defaultValue = "") String toDate,
      @PathVariable String username);


  @GetMapping("/batch-job/latest")
  Collection<OperationResponse> getJobs(@RequestParam String username,
      @RequestParam String jobName);
}
