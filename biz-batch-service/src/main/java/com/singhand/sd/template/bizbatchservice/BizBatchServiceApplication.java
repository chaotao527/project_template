package com.singhand.sd.template.bizbatchservice;

import com.singhand.sd.template.bizbatchservice.service.PostLaunchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.singhand.sd.template")
@Slf4j
@EnableFeignClients
@EnableScheduling
public class BizBatchServiceApplication {

  public static void main(String[] args) {

    ZipSecureFile.setMinInflateRatio(0);
    final var context = SpringApplication.run(BizBatchServiceApplication.class, args);
    log.debug(">>> Datasource instance: " + context.getBean("dataSource"));
    log.debug(">>> TransactionManager instance: " + context.getBean("transactionManager"));
    postLaunch(context);
  }

  private static void postLaunch(ApplicationContext context) {

    final var postLaunchService = context.getBean(PostLaunchService.class);
    postLaunchService.abortRunningJobs();
    postLaunchService.removeUnusedJobInstances();
  }
}
