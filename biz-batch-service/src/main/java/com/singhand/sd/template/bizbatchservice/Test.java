package com.singhand.sd.template.bizbatchservice;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @describe: 描述
 * @author: chaotao
 * @create_date: 2023/3/29 10:40
 * @version: V1.0
 **/
public class Test {

  public static void main(String[] args) {
    String key="tp-bucket/import_datasource/2023-03-29/xxx.docx";
    final var realName = StrUtil.subBefore(key, ".datasource", true);
    final var id = FileNameUtil.mainName(realName);
  }
}
