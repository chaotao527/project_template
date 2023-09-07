package com.singhand.sd.template.bizshell.service;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
@Slf4j
public class DatasourceCommandsService {



  private final MinioClient minioClient;

  private final String bucket;

  private final PlatformTransactionManager bizTransactionManager;






  @Autowired
  public DatasourceCommandsService(MinioClient minioClient,
      @Value("${minio.bucket}") String bucket,
      @Qualifier("bizTransactionManager") PlatformTransactionManager bizTransactionManager) {

    this.minioClient = minioClient;
    this.bucket = bucket;
    this.bizTransactionManager = bizTransactionManager;
  }
}
