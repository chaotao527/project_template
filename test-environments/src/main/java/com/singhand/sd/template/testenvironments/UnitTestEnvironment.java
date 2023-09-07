package com.singhand.sd.template.testenvironments;

import com.singhand.sd.template.bizkeycloakmodel.model.KeycloakRole;
import com.singhand.sd.template.bizkeycloakmodel.model.UserEntity;
import com.singhand.sd.template.bizkeycloakmodel.repository.UserEntityRepository;
import com.singhand.sd.template.bizkeycloakmodel.service.KeycloakService;
import io.minio.MinioClient;
import java.util.Optional;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public class UnitTestEnvironment extends TestEnvironment {

  @MockBean
  private UserEntityRepository userEntityRepository;

  @MockBean
  private KeycloakService keycloakService;

 /* @MockBean
  private MinioClient minioClient;*/

  @DynamicPropertySource
  @SneakyThrows
  static void bindProperties(DynamicPropertyRegistry registry) {
    // 单元测试不依赖于外部环境，所以此处禁用 Zookeeper
    registry.add("spring.cloud.zookeeper.enabled", () -> false);
  }

  @BeforeAll
  void beforeAll() {

    // BizLogger 会记录用户信息，因此要 mock 一个固定的返回值，否则不会生成 BizLog 记录，
    // 若后续测试有具体的值需求或者判定逻辑，那么需要针对每个测试的具体逻辑，mock 具体的值来进行测试，
    // 届时，可以考虑将 mock 返回值的代码写到对应的 method 中，而不是 beforeAll。
    final var userEntity = new UserEntity();
    userEntity.setId("admin");
    userEntity.setUsername("admin");
    userEntity.setFirstName("admin");
    userEntity.setRoles(Set.of(KeycloakRole.builder().name("超级管理员").clientRole(true).build()));
    Mockito.when(keycloakService.getRealm()).thenReturn("console-app");
    Mockito.when(userEntityRepository.findByUsernameAndRealmId("admin", keycloakService.getRealm()))
        .thenReturn(Optional.of(userEntity));
  }
}
