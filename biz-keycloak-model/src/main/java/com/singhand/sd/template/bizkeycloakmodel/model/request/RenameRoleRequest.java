package com.singhand.sd.template.bizkeycloakmodel.model.request;

import com.singhand.sd.template.bizkeycloakmodel.validator.NotSuperAdminRole;
import lombok.Data;

@Data
public class RenameRoleRequest {

  @NotSuperAdminRole
  private String newRoleName;
}
