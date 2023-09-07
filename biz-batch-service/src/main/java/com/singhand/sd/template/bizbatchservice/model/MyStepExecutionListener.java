package com.singhand.sd.template.bizbatchservice.model;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class MyStepExecutionListener implements StepExecutionListener {

  private int timeout = 10; // 默认超时时间为10秒

  private long startTime;

  public MyStepExecutionListener(int timeout) {

    this.timeout = timeout;
  }

  @Override
  public void beforeStep(@NotNull StepExecution stepExecution) {

    startTime = System.currentTimeMillis();
  }

  @Override
  public ExitStatus afterStep(@NotNull StepExecution stepExecution) {

    if (System.currentTimeMillis() - startTime >= timeout * 1000L) {
      // 超时处理
      return ExitStatus.FAILED;
    } else {
      return null;
    }
  }
}