package com.singhand.sd.template.bizbatchservice.service;

import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class KafkaConsumerService {

    private final JobLauncher jobLauncher;

    private final Job importDocumentJob;

    private final String bucket;

    private final MinioClient minioClient;




    @Autowired
    public KafkaConsumerService(JobLauncher jobLauncher,
                                Job importDocumentJob, Job importDatasourceJob,
                                Job importWorkOrdersJob,
                                @Value("${minio.bucket}") String bucket,
                                MinioClient minioClient, Job previewDatasourceJob) {

        this.jobLauncher = jobLauncher;
        this.importDocumentJob = importDocumentJob;
        this.bucket = bucket;
        this.minioClient = minioClient;
    }

    public static String getObjectName(String bucket, String key) {

        return StrUtil.subAfter(key, bucket, true);
    }

    private static void setValue(Map<String, String> props, String key, String value) {

        if (StrUtil.isNotBlank(value)) {
            props.put(key, value);
        }
    }


    @KafkaListener(topics = "minio_bucket_notification", groupId = "minio", containerFactory = "minioKafkaListenerContainerFactory")
    @SneakyThrows
    public void minioBucketNotification(ConsumerRecord<?, ?> consumerRecord,
                                        Acknowledgment acknowledgment) {

        log.info("minio_bucket_notification: {}", consumerRecord);

        acknowledgment.acknowledge();

        final var statObjectResponse = minioClient.statObject(
                StatObjectArgs.builder().bucket(bucket)
                        .object(getObjectName(bucket, consumerRecord.key().toString()))
                        .build());
        var uuid = statObjectResponse.userMetadata().getOrDefault("uuid", "");
        if (StrUtil.isBlank(uuid)) {
            uuid = UUID.randomUUID().toString();
            log.warn("用户没有指定 UUID，将采用自定义 UUID！ UUID={}", uuid);
        }

        final var key = consumerRecord.key().toString();
        if (!StrUtil.endWithIgnoreCase(key, "html")) {
            if (StrUtil.startWith(key, bucket + "/import_document/")) {
                jobLauncher.run(importDocumentJob, new JobParametersBuilder()
                        .addString("instance_id", uuid, true)
                        .addString("object", key, false).toJobParameters());
            }
        }
    }

}
