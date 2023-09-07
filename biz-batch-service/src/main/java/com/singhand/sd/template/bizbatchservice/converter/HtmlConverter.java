package com.singhand.sd.template.bizbatchservice.converter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;

public class HtmlConverter {

  @SneakyThrows
  public static List<String> htmlLines(String htmlPath) {

    final var html = Jsoup.parse(new File(htmlPath), CharsetUtil.UTF_8).select("body").text();

    return Arrays.asList(html.split("\n"));
  }

  @SneakyThrows
  public static String raw(String htmlPath) {

    return Jsoup.parse(new File(htmlPath), CharsetUtil.UTF_8).select("body").html();
  }

  @SneakyThrows
  public static String html2txt(String pdfPath) {

    return StrUtil.join("", htmlLines(pdfPath));
  }
}
