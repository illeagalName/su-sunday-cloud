package com.haier.system.file;

import com.haier.minio.service.BaseMinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/5 14:58
 */
@Service
@Slf4j
public class FileService extends BaseMinioService {

    public String uploadFile(MultipartFile file) {
        try {
            return uploadFile(file, "images");
        } catch (Exception e) {
            log.error("上传文件异常", e);
        }
        return null;
    }
}
