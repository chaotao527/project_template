package com.singhand.sd.template.testenvironments;

import com.singhand.sd.template.bizkeycloakmodel.service.KeycloakService;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Testcontainers
public abstract class KeycloakIntegrationTestEnvironment extends TestEnvironment {

  private final static String PGSQL_ROOT_USER = "root";

  private final static String PGSQL_ROOT_PASSWORD = "example";

  public final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      DockerImageName.parse("postgres:alpine3.16"))
      .withUsername(PGSQL_ROOT_USER)
      .withPassword(PGSQL_ROOT_PASSWORD)
      .withClasspathResourceMapping("databases.sql", "/docker-entrypoint-initdb.d/databases.sql",
          BindMode.READ_ONLY);

  public final static GenericContainer<?> keycloak = new GenericContainer<>(
      DockerImageName.parse("bitnami/keycloak:19.0.3-debian-11-r3"))
      .withEnv("BASE_URL", "http://localhost/")
      .withEnv("KEYCLOAK_CREATE_ADMIN_USER", "true")
      .withEnv("KEYCLOAK_ADMIN_USER", "admin")
      .withEnv("KEYCLOAK_ADMIN_PASSWORD", "admin")
      .withEnv("KEYCLOAK_DATABASE_NAME", "keycloak")
      .withEnv("KEYCLOAK_DATABASE_HOST", "postgres")
      .withEnv("KEYCLOAK_DATABASE_USER", PGSQL_ROOT_USER)
      .withEnv("KEYCLOAK_DATABASE_PASSWORD", PGSQL_ROOT_PASSWORD)
      .withEnv("KEYCLOAK_PROXY", "edge")
      .withEnv("KEYCLOAK_HTTP_RELATIVE_PATH", "/auth")
      .withEnv("KEYCLOAK_EXTRA_ARGS", "-Dkeycloak.import=/tmp/realm.json")
      .withClasspathResourceMapping("realm-export.json", "/tmp/realm.json", BindMode.READ_ONLY)
      .withExposedPorts(8080)
      .withLogConsumer(new Slf4jLogConsumer(log))
      .dependsOn(postgres);

  @MockBean
  private MinioClient minioClient;

  @Autowired
  private KeycloakService keycloakService;

  @DynamicPropertySource
  @SneakyThrows
  static void bindProperties(DynamicPropertyRegistry registry) {

    final var network = Network.newNetwork();

    postgres.withNetwork(network).withNetworkAliases("postgres").start();
    final var keycloakJdbcUrl = postgres.getJdbcUrl().replace("test", "keycloak");
    keycloak.withNetwork(network).withNetworkAliases("keycloak").start();
    registry.add("keycloak.auth-server-url", () -> String.format("http://%s:%d/auth",
        keycloak.getHost(), keycloak.getMappedPort(8080)));
    registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
        () -> String.format("http://%s:%s/auth/realms/console-app/protocol/openid-connect/certs",
            keycloak.getHost(), keycloak.getMappedPort(8080)));
    registry.add("app.datasource.keycloak.url", () -> keycloakJdbcUrl);
    registry.add("app.datasource.keycloak.jdbcUrl", () -> keycloakJdbcUrl);
    registry.add("app.datasource.keycloak.username", () -> "root");
    registry.add("app.datasource.keycloak.password", () -> "example");
    registry.add("app.datasource.keycloak.driver-class-name", () -> "org.postgresql.Driver");
    registry.add("app.datasource.keycloak.dialect",
        () -> "org.hibernate.dialect.PostgreSQLDialect");
  }

  @AfterEach
  @Override
  void afterEach() {

    super.afterEach();

    keycloakService.getUsersResource().list().forEach(it -> {
      if (!it.getUsername().equals("admin") && !it.getUsername().startsWith("reserved_")) {
        keycloakService.getUserResourceById(it.getId()).remove();
      }
    });

    keycloakService.getClientResource().roles().list().forEach(it -> {
      if (!it.getName().equals("超级管理员")) {
        keycloakService.getClientRoleResource(it.getName()).remove();
      }
    });

    keycloakService.getGroupsResource().groups().forEach(it ->
        keycloakService.getGroupResource(it.getId()).remove());
  }
}
