package com.singhand.sd.template.testenvironments.helper;

import com.singhand.sd.template.bizkeycloakmodel.service.KeycloakGroupService;
import com.singhand.sd.template.bizkeycloakmodel.service.KeycloakUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Slf4j
public class DataHelper {

    public final static String NAME_PREFIX = "";


    private final KeycloakUserService keycloakUserService;

    private final KeycloakGroupService keycloakGroupService;


    @Autowired
    public DataHelper(
            KeycloakUserService keycloakUserService,
            KeycloakGroupService keycloakGroupService) {

        this.keycloakUserService = keycloakUserService;
        this.keycloakGroupService = keycloakGroupService;
    }

}
