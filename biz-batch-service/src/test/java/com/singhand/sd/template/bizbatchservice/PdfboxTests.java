package com.singhand.sd.template.bizbatchservice;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class PdfboxTests {

  @SneakyThrows
  @Test
  void testExtractText() {

    final var file = new File(ResourceUtil.getResource("Foo.pdf").toURI());
    @Cleanup final var randomAccessFile = new RandomAccessFile(file, "r");
    final var parser = new PDFParser(randomAccessFile);
    parser.parse();
    @Cleanup final var cosDoc = parser.getDocument();
    final var stripper = new PDFTextStripper();
    final var document = new PDDocument(cosDoc);
    final var text = stripper.getText(document);
    Assertions.assertFalse(text.isBlank());
    final var lines = StrUtil.splitToArray(text, "\n");
    for (final var line : lines) {
      log.info("{}", line);
    }
  }
}
