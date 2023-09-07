package com.singhand.sd.template.bizservice.annotation.resolver;

import org.aspectj.lang.ProceedingJoinPoint;

public interface Resolver {

  Object getProperty(ProceedingJoinPoint joinPoint, Object proceed, String beanPath)
      throws Exception;
}
