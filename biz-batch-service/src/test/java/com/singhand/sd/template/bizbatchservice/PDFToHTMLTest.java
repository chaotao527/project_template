package com.singhand.sd.template.bizbatchservice;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fit.pdfdom.PDFDomTree;
import org.junit.Test;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class PDFToHTMLTest {

  @Test
  @SneakyThrows
  public void testPPFToHTML() {

    @Cleanup final var document = PDDocument.load(new File(
        ResourceUtil.getResource("xxx.pdf").toURI()));
    @Cleanup final var output = new ByteArrayOutputStream();
    @Cleanup final var writer = IoUtil.getWriter(output, StandardCharsets.UTF_8);
    final var domTree = new PDFDomTree();
    final var dom = domTree.createDOM(document);

    final var registry = DOMImplementationRegistry.newInstance();
    final var impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
    final var lsSerializer = impl.createLSSerializer();
    final var lsOutput = impl.createLSOutput();
    lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
    lsOutput.setCharacterStream(writer);
    lsSerializer.write(dom, lsOutput);
    final var str = IoUtil.toStr(output, StandardCharsets.UTF_8);
    writer.close();
    output.close();
    log.info(str);
  }

  @Test
  @SneakyThrows
  public void testPPFToText() {

    final var str = StrUtil.join("", pdf2lines());
    log.info(str);
  }

  @SneakyThrows
  private List<String> pdf2lines() {

    final var file = new File(ResourceUtil.getResource("xxx.pdf").toURI());
    @Cleanup final var randomAccessFile = new RandomAccessFile(file, "r");
    final var parser = new PDFParser(randomAccessFile);
    parser.parse();
    @Cleanup final var cosDoc = parser.getDocument();
    final var stripper = new PDFTextStripper();
    final var document = new PDDocument(cosDoc);
    return Arrays.asList(stripper.getText(document).split("\n"));
  }

}
