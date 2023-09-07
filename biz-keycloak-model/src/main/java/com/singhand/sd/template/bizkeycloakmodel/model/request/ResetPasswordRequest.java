package com.singhand.sd.template.bizkeycloakmodel.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequest {

  private String originalPassword;
  private String password;
}
