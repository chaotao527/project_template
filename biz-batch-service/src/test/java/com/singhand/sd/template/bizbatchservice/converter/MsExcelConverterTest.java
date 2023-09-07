package com.singhand.sd.template.bizbatchservice.converter;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class MsExcelConverterTest {

  @Test
  void xlsx2csv() {

    final var data = MsExcelConverter.xlsx2csv(
        ResourceUtil.getResource("TestExcel2Csv.xlsx").getPath());
    log.info(data);
    Assertions.assertEquals(6, StrUtil.split(data, "\n").size());
  }

  @Test
  void xls2csv() {

    final var data = MsExcelConverter.xls2csv(
        ResourceUtil.getResource("TestExcel2Csv.xls").getPath());
    log.info(data);
    Assertions.assertEquals(6, StrUtil.split(data, "\n").size());
  }

  @Test
  void xls2html() {

    final var html = MsExcelConverter.xls2html(
        ResourceUtil.getResource("TestExcel2Csv.xls").getPath());
    log.info(html);
  }

  @Test
  void xlsx2html() {

    final var html = MsExcelConverter.xlsx2html(
        ResourceUtil.getResource("TestExcel2Csv.xlsx").getPath());
    log.info(html);
  }
}