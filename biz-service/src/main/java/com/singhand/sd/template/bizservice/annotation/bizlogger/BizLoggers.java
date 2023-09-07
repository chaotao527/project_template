package com.singhand.sd.template.bizservice.annotation.bizlogger;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BizLoggers {

  BizLogger[] value() default {};
}
