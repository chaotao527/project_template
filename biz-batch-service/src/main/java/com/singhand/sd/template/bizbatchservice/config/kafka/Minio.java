package com.singhand.sd.template.bizbatchservice.config.kafka;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

@Configuration
public class Minio {

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "app.kafka.minio")
  public KafkaProperties minioKafkaProperties() {

    return new KafkaProperties();
  }

  @Bean
  @Primary
  public ConsumerFactory<String, String> minioKafkaConsumerFactory() {

    return new DefaultKafkaConsumerFactory<>(minioKafkaProperties().buildConsumerProperties());
  }

  @Bean
  @Primary
  public ConcurrentKafkaListenerContainerFactory<String, String> minioKafkaListenerContainerFactory() {

    final var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
    factory.setConsumerFactory(minioKafkaConsumerFactory());
    factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
    return factory;
  }
}
