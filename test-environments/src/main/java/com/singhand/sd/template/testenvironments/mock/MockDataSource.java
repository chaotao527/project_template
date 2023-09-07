package com.singhand.sd.template.testenvironments.mock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单元测试中生成 Mock 数据。 注意，若 title 重复，则视为相同数据。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MockDataSources.class)
public @interface MockDataSource {

  String title();

  String sourceType() default "sourceType";

  String contentType() default "contentType";

  String source() default "source";
}
