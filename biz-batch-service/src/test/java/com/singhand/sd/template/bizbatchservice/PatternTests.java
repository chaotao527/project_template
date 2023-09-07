package com.singhand.sd.template.bizbatchservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PatternTests {

  @Test
  void testReplaceExtName() {

    final var path = "import_datasource/2022-11-28/filename.complex.ext";
    final var newPath = path.replaceFirst("\\.[^.]*$", ".html");
    Assertions.assertEquals("import_datasource/2022-11-28/filename.complex.html", newPath);
  }
}
