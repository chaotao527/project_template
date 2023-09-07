package com.singhand.sd.template.bizbatchservice.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.http.ContentType;
import com.singhand.sd.template.testenvironments.UnitTestEnvironment;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.SetBucketNotificationArgs;
import io.minio.messages.EventType;
import io.minio.messages.NotificationConfiguration;
import io.minio.messages.QueueConfiguration;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class KafkaConsumerServiceTest extends UnitTestEnvironment {

  public static final String TOPIC = "minio_bucket_notification";

  private static final String MINIO_DEFAULT_BUCKET = "tp-bucket";

  @Autowired
  private MinioClient minioClient;

  @Autowired
  private FooKafkaConsumerService fooKafkaConsumerService;

  @BeforeAll
  @SneakyThrows
  public void setUp() {

    setBucketNotification();
  }

  @Test
  @SneakyThrows
  public void testMinioNotification() {

    final var file = FileUtil.file("banner.txt");

    ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
            .bucket(MINIO_DEFAULT_BUCKET)
            .object(file.getName())
            .contentType(ContentType.TEXT_PLAIN.getValue())
            .stream(FileUtil.getInputStream(file), file.length(), -1)
            .build());

    final var messageConsumed = fooKafkaConsumerService.getLatch().await(10, TimeUnit.SECONDS);
    Assert.isTrue(messageConsumed);
    Assert.notBlank(fooKafkaConsumerService.getPayload());
  }

  @SneakyThrows
  private void setBucketNotification() {

    final var eventList = new LinkedList<EventType>();
    eventList.add(EventType.OBJECT_CREATED_PUT);
    eventList.add(EventType.OBJECT_CREATED_COPY);

    final var queueConfiguration = new QueueConfiguration();
    // 此处 TP 一定要大写，ARN 要对应 Minio 启动后日志中 SQS 所在行的 ARN
    queueConfiguration.setQueue("arn:minio:sqs::TP:kafka");
    queueConfiguration.setEvents(eventList);

    final var queueConfigurationList = new LinkedList<QueueConfiguration>();
    queueConfigurationList.add(queueConfiguration);

    final var config = new NotificationConfiguration();
    config.setQueueConfigurationList(queueConfigurationList);

    minioClient.setBucketNotification(
        SetBucketNotificationArgs.builder().bucket(MINIO_DEFAULT_BUCKET).config(config).build());
  }
}