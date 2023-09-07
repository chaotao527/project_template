package com.singhand.sd.template.bizshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.singhand.sd.template")
public class BizShellApplication {

  public static void main(String[] args) {

    SpringApplication.run(BizShellApplication.class, args);
  }
}
