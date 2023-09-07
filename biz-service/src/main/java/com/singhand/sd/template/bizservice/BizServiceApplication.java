package com.singhand.sd.template.bizservice;

import com.singhand.sd.template.bizservice.config.MinioConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.singhand.sd.template")
@ConfigurationPropertiesScan(basePackageClasses = {MinioConfig.class})
@EnableFeignClients
@Slf4j
public class BizServiceApplication {

  public static void main(String[] args) throws Exception {

    final var context = SpringApplication.run(BizServiceApplication.class, args);

    if ("always".equals(context.getEnvironment().getProperty("app.reset-with-demo-data"))) {

//      log.warn("正在初始化测试数据...");
//
//      ((DemoService) context.getBean("datasourceDemoService")).run();
//
//      log.warn("测试数据初始化完成！");
    }
  }
}
