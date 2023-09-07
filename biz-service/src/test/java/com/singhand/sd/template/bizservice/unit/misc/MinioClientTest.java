package com.singhand.sd.template.bizservice.unit.misc;

import cn.hutool.core.io.FileUtil;
import com.singhand.sd.template.testenvironments.UnitTestEnvironment;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;

@Slf4j
public class MinioClientTest extends UnitTestEnvironment {
    private static final String MINIO_DEFAULT_BUCKET = "tp-bucket";
    @Autowired
    private MinioClient minioClient;
    @Test
    @SneakyThrows
    void testGetPutDownloadURL_expect_403() {
        /*final var file = FileUtil.file("application.yml");
        @Cleanup final var inputStream = FileUtil.getInputStream(file);
        final var objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket(MINIO_DEFAULT_BUCKET)
                .object("/test/" + file.getName())
                .stream(inputStream, -1, 10485760)
                .build());
        log.info("");*/

        /*String url="http://localhost:9000";
        String accessKey="minio-root-user";
        String secretKey="minio-root-password";
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();*/
        File file = new File("D:\\work\\xinghan\\project_template\\biz-batch-service\\target\\classes\\banner.txt");
        @Cleanup InputStream stream = FileUtil.getInputStream(file);
        minioClient.putObject(PutObjectArgs.builder().bucket("tp-bucket").object(file.getName())
                .stream(stream, -1, 10485760).build());
        Thread.sleep(100*1000);
    }
}
