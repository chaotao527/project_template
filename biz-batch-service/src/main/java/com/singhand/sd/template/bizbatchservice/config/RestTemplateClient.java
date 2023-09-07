package com.singhand.sd.template.bizbatchservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateClient {

  //注入restTemplate 参考：https://stackoverflow.com/questions/36151421/could-not-autowire-fieldresttemplate-in-spring-boot-application
  @Bean
  public RestTemplate restTemplate() {

    return new RestTemplate();
  }
}
