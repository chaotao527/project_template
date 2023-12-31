package com.singhand.sd.template.bizbatchservice.datasource;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

@Configuration
public class Batch {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSourceProperties batchDataSourceProperties() {

    return new DataSourceProperties();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public DataSource dataSource() {

    return batchDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {

    return new JdbcTransactionManager(dataSource);
  }
}
