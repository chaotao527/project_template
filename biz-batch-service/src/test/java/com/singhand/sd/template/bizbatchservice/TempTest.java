package com.singhand.sd.template.bizbatchservice;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;

/**
 * @describe: 描述
 * @author: chaotao
 * @create: 2023/8/7 11:19
 **/
@Slf4j
public class TempTest {
  @Test
  @SneakyThrows
  public void testPdf(){
    File file = new File("D:\\work\\xinghan\\制裁合规数字系统\\211213-制裁合规专刊001Y-美对华出口管制政策及最新动向评估.pdf");
    @Cleanup final var randomAccessFile = new RandomAccessFile(file, "r");
    final var parser = new PDFParser(randomAccessFile);
    parser.parse();
    @Cleanup final var cosDoc = parser.getDocument();
    final var stripper = new PDFTextStripper();
    final var document = new PDDocument(cosDoc);
    final var text = stripper.getText(document);
    Assertions.assertFalse(text.isBlank());
    FileUtil.writeUtf8String(text,"D:\\work\\xinghan\\制裁合规数字系统\\211213-制裁合规专刊001Y-美对华出口管制政策及最新动向评估.txt");
    final var lines = StrUtil.splitToArray(text, "\n");
    for (final var line : lines) {
      log.info("{}", line);
    }
  }
}
