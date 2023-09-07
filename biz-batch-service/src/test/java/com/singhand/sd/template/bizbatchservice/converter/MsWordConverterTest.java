package com.singhand.sd.template.bizbatchservice.converter;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MsWordConverterTest {

  @Test
  void docx2lines() {

    final var converter = new MsWordConverter(null);
    final var lines = converter.docx2lines(
        ResourceUtil.getResource("AI_TestDocx2Txt.docx").getPath());
    log.info(StrUtil.join("\n", lines));
  }

  @Test
  void docx2lines_wps() {

    final var converter = new MsWordConverter(null);
    final var lines = converter.docx2lines(
        ResourceUtil.getResource("AI_TestDocx2Txt_WPS.docx").getPath());
    log.info(StrUtil.join("\n", lines));
  }

  @Test
  void docxTruncatedZipFile() {

    final var converter = new MsWordConverter(null);
    final var lines = converter.docx2lines(
        ResourceUtil.getResource("TestTruncatedZipFile.docx").getPath()
    );
    log.info(StrUtil.join("\n", lines));
  }
}