package com.singhand.sd.template.bizshell.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import cn.hutool.core.io.resource.ResourceUtil;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.AutoConfigureShell;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureShell
@ContextConfiguration
@ActiveProfiles("test")
public class ReadCsvFileTest {

  @Autowired
  ShellTestClient shellTestClient;

  @Test
  @SneakyThrows
  void testReadCsvFile(){

    final var directory = ResourceUtil.getResource("diwu").getPath();
    final var session = shellTestClient
        .nonInterative("input-csv-file", "-i", directory)
        .run();

    await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      assertTrue(session.isComplete());
    });
  }

}
