package com.singhand.sd.template.bizbatchservice;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.resource.ResourceUtil;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.junit.jupiter.api.Test;

@Slf4j
public class WordToHtmlConverterTests {

  @Test
  @SneakyThrows
  void testConvertDocToHtml() {

    final var newDocument = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder().newDocument();
    final var wordToHtmlConverter = new WordToHtmlConverter(
        newDocument);
    final var hwpfDocument = new HWPFDocument(ResourceUtil.getStream("TestDoc2Html.doc"));
    wordToHtmlConverter.setPicturesManager(
        (content, pictureType, suggestedName, widthInches, heightInches) -> String.format(
            "data:%s;base64,%s", pictureType.getMime(), Base64.encode(content)));
    wordToHtmlConverter.processDocument(hwpfDocument);
    final var stringWriter = new StringWriter();
    final var transformer = TransformerFactory.newInstance()
        .newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
    transformer.setOutputProperty(OutputKeys.METHOD, "html");
    transformer.transform(
        new DOMSource(wordToHtmlConverter.getDocument()),
        new StreamResult(stringWriter));
    log.info(stringWriter.toString());
  }
}
