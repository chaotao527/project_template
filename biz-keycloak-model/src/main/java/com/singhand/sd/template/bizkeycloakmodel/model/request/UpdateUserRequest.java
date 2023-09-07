package com.singhand.sd.template.bizkeycloakmodel.model.request;

import com.singhand.sd.template.bizkeycloakmodel.validator.NotSuperAdminRoleId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {

  private String name;

  private String groupId;

  private String phoneNumber;

  @NotSuperAdminRoleId
  private String roleId;

  private String picture;
}
