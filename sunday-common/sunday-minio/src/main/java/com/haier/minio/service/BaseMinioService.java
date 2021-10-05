package com.haier.minio.service;

import com.haier.minio.config.MinioProperties;
import com.haier.minio.util.FileUploadUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/5 14:00
 */
@Slf4j
public abstract class BaseMinioService {

    @Resource
    MinioProperties minioProperties;

    @Resource
    MinioClient client;

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName 文件桶名
     * @return
     * @throws Exception
     */
    public String uploadFile(MultipartFile file, String bucketName) throws Exception {
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        return minioProperties.getEndpoint() + "/" + bucketName + "/" + fileName;
    }

    /**
     * 判断文件桶是否存在
     *
     * @param bucketName 文件桶名
     * @return
     * @throws Exception
     */
    public boolean existBucket(String bucketName) throws Exception {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建文件桶
     *
     * @param bucketName 文件桶名
     * @throws Exception
     */
    public void createBucket(String bucketName) throws Exception {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }
}
