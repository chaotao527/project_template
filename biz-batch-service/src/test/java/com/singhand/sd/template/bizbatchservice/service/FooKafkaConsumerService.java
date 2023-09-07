package com.singhand.sd.template.bizbatchservice.service;

import java.util.concurrent.CountDownLatch;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class FooKafkaConsumerService {

  private CountDownLatch latch = new CountDownLatch(1);

  private String payload;

  @KafkaListener(topics = KafkaConsumerServiceTest.TOPIC)
  public void receive(ConsumerRecord<?, ?> consumerRecord) {

    log.info("received payload='{}'", consumerRecord.toString());
    payload = consumerRecord.toString();
    latch.countDown();
  }
}
