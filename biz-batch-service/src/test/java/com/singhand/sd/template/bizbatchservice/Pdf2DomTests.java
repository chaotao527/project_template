package com.singhand.sd.template.bizbatchservice;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;

@Slf4j
public class Pdf2DomTests {

  @Test
  @SneakyThrows
  void testPdf2Dom() {

    @Cleanup final var document = PDDocument.load(ResourceUtil.getStream("Foo.pdf"));
    @Cleanup final var output = new ByteArrayOutputStream();
    @Cleanup final var writer = IoUtil.getWriter(output, StandardCharsets.UTF_8);
    final var domTree = new PDFDomTree();
    final var dom = domTree.createDOM(document);

    final var images = dom.getElementsByTagName("img");
    final var imageNum = images.getLength();
    for (var i = 0; i < imageNum; i++) {
      final var image = images.item(i);
      image.getAttributes().getNamedItem("src").setNodeValue("https://www.baidu.com");
    }

    final var registry = DOMImplementationRegistry.newInstance();
    final var impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
    final var lsSerializer = impl.createLSSerializer();
    final var lsOutput = impl.createLSOutput();
    lsSerializer.getDomConfig().setParameter("format-pretty-print", true);
    lsOutput.setCharacterStream(writer);
    lsSerializer.write(dom, lsOutput);

    final var html = IoUtil.toStr(output, StandardCharsets.UTF_8);
    Assertions.assertTrue(StrUtil.contains(html, "https://www.baidu.com"));
  }
}
