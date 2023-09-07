package com.singhand.sd.template.bizbatchservice.schedule;

import com.singhand.sd.template.bizbatchservice.service.PostLaunchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

  private final PostLaunchService postLaunchService;

  @Autowired
  public ScheduledTasks(PostLaunchService postLaunchService) {

    this.postLaunchService = postLaunchService;
  }

  @Scheduled(fixedDelay = 60 * 60 * 1000)
  public void removeUnusedJobInstances() {

    postLaunchService.removeUnusedJobInstances();
  }
}
