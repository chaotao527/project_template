package com.singhand.sd.template.bizbatchservice.tasklet;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class ExportDocumentTasklet implements Tasklet {


    private final MinioClient minioClient;

    private final String bucket;

    private XWPFDocument xwpfDocument;

    @Autowired
    public ExportDocumentTasklet(MinioClient minioClient,
                                 @Value("${minio.bucket}") String bucket) {

        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    @Override
    public RepeatStatus execute(@NotNull StepContribution stepContribution,
                                @NotNull ChunkContext chunkContext)
            throws Exception {

        xwpfDocument = new XWPFDocument();

        final var stepContext = stepContribution.getStepExecution();
        final var executionContext = stepContext.getJobExecution().getExecutionContext();
        final var id = stepContext.getJobParameters().getLong("id");
        if (id == null) {
            throw new Exception("导出失败！id为空");
        }


        executionContext.putString("message", "已成功导出想定文档");
//    executionContext.putString("url", String.format("/%s/%s", bucket, objectName));
        return RepeatStatus.FINISHED;
    }


}


