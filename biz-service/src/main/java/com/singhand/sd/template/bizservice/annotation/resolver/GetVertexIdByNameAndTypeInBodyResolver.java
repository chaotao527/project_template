package com.singhand.sd.template.bizservice.annotation.resolver;

import org.apache.commons.math3.geometry.spherical.twod.Vertex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class GetVertexIdByNameAndTypeInBodyResolver implements Resolver {

  @Override
  public Object getProperty(ProceedingJoinPoint joinPoint, Object proceed, String beanPath)
      throws Exception {

    final var id = DefaultResolver.getValueFromRequestBody(joinPoint, beanPath);
    final var name = DefaultResolver.getValueFromRequestBody(joinPoint, "request.body.name");
    final var type = DefaultResolver.getValueFromRequestBody(joinPoint, "request.body.type");
    if (id != null) {
      return id.toString();
    } else if (name != null && type != null) {
    }

    return "";
  }
}
