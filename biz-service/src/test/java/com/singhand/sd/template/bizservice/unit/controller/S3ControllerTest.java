package com.singhand.sd.template.bizservice.unit.controller;

import com.singhand.sd.template.testenvironments.UnitTestEnvironment;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class S3ControllerTest extends UnitTestEnvironment {

  @Test
  @SneakyThrows
  @WithMockUser(username = "admin")
  void testGetPutDownloadURL_expect_403() {

    mockMvc.perform(get("/s3/putURL")
            .param("name", faker.file().fileName())
            .param("path", "images_and_videos"))
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  @WithMockUser(username = "admin", authorities = "profile")
  void testGetPutDownloadURL_expect_200() {

    mockMvc.perform(get("/s3/putURL")
            .param("name", faker.file().fileName())
            .param("path", "images_and_videos"))
        .andExpect(status().isOk());
  }
}