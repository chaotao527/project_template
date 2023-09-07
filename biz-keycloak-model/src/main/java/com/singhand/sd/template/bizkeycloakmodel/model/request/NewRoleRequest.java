package com.singhand.sd.template.bizkeycloakmodel.model.request;

import com.singhand.sd.template.bizkeycloakmodel.validator.NotSuperAdminRole;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewRoleRequest {

  @NotSuperAdminRole
  private String name;

  private List<String> scopes;
}
