package com.singhand.sd.template.bizbatchservice;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Docx4JTests {

  @Test
  @SneakyThrows
  void testDocx2Html() {

    @Cleanup final var inputStream = ResourceUtil.getStream("TestDocx2Html.docx");
    final var opcPackage = OpcPackage.load(inputStream);
    final var tempFilename = File.createTempFile("TestDocx2Html", ".html");
    @Cleanup final var outputStream = FileUtil.getOutputStream(tempFilename);
    final var htmlSettings = Docx4J.createHTMLSettings();
    htmlSettings.setOpcPackage(opcPackage);
    htmlSettings.setImageHandler((abstractWordXmlPicture, relationship, binaryPart) -> {
      final var mime = binaryPart.getContentType();
      final var base64 = Base64.encode(binaryPart.getBytes());
      return String.format("data:%s,%s", mime, base64);
    });
    Docx4J.toHTML(htmlSettings, outputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
    log.info("{}", FileUtil.readLines(tempFilename.getAbsolutePath(), StandardCharsets.UTF_8));
  }

  @Test
  @SneakyThrows
  void testDocx2Html_report() {

    @Cleanup final var inputStream = ResourceUtil.getStream("TestReportDocx2Html.docx");
    final var opcPackage = OpcPackage.load(inputStream);
    final var tempFilename = File.createTempFile("TestReportDocx2Html", ".html");
    @Cleanup final var outputStream = FileUtil.getOutputStream(tempFilename);
    final var htmlSettings = Docx4J.createHTMLSettings();
    htmlSettings.setOpcPackage(opcPackage);
    htmlSettings.setImageHandler((abstractWordXmlPicture, relationship, binaryPart) -> {
      final var mime = binaryPart.getContentType();
      final var base64 = Base64.encode(binaryPart.getBytes());
      return String.format("data:%s,%s", mime, base64);
    });
    Docx4J.toHTML(htmlSettings, outputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
    log.info("{}", FileUtil.readLines(tempFilename.getAbsolutePath(), StandardCharsets.UTF_8));
  }

  @Test
  @SneakyThrows
  void testDocx2Html_no_default_style() {

    @Cleanup final var document = new XWPFDocument();
    final var table = document.createTable();
    table.createRow().createCell().setText("Hello World");
    final var tempFilename = File.createTempFile("TestReportDocx2Html", ".docx");
    final var demoFileOutputStream = FileUtil.getOutputStream(tempFilename);
    document.write(demoFileOutputStream);
    demoFileOutputStream.flush();
    demoFileOutputStream.close();
    document.close();
    log.info("Demo Docx file: {}", tempFilename.getAbsolutePath());

    @Cleanup final var inputStream = FileUtil.getInputStream(tempFilename);
    final var opcPackage = OpcPackage.load(inputStream);
    @Cleanup final var outputStream = FileUtil.getOutputStream(tempFilename);
    final var htmlSettings = Docx4J.createHTMLSettings();
    htmlSettings.setOpcPackage(opcPackage);
    htmlSettings.setImageHandler((abstractWordXmlPicture, relationship, binaryPart) -> {
      final var mime = binaryPart.getContentType();
      final var base64 = Base64.encode(binaryPart.getBytes());
      return String.format("data:%s,%s", mime, base64);
    });
    Docx4J.toHTML(htmlSettings, outputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
    log.info("{}", FileUtil.readLines(tempFilename.getAbsolutePath(), StandardCharsets.UTF_8));
  }
}
