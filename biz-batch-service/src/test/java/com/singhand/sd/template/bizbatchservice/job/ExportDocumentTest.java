package com.singhand.sd.template.bizbatchservice.job;

import com.singhand.sd.template.testenvironments.UnitTestEnvironment;
import com.singhand.sd.template.testenvironments.helper.DataHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

class ExportDocumentTest extends UnitTestEnvironment {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job exportDocumentJob;

    @Autowired
    private DataHelper dataHelper;


    private Long documentID;

    @BeforeAll
    @SneakyThrows
    void setUp() {
    }

    @Test
    @SneakyThrows
    void testImportDocumentJob() {

        /*final var jobExecution = jobLauncher.run(exportDocumentJob, new JobParametersBuilder()
                .addLong("id", documentID)
                .toJobParameters());
        // 因为在 BatchConfig 中强制指定了任务调度使用异步方式，所以任务启动后，稍微等一下，再检查结果，否则会得到 UNKNOWN
        Thread.sleep(5000);
        Assertions.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());*/
    }
}