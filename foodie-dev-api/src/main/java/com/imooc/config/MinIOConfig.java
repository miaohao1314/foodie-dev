package com.imooc.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * MinIOConfig的配置，值是映射在yml文件中的配置数据
 */

@Configuration
@Data
public class MinIOConfig {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.fileHost}")
    private String fileHost;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

//    @Value("${minio.imgSize}")
//    private Integer imgSize;
//    @Value("${minio.fileSize}")
//    private Integer fileSize;

    /**
     * 创建MinIOUtils
     * @return
     */

//    @Bean
//    public MinIOUtils creatMinioClient() {
//        return  new MinIOUtils(endpoint,bucketName,accessKey,secretKey);
//    }
}
