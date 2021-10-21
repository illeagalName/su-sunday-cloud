package com.haier.system.controller;

import com.haier.core.domain.R;
import com.haier.system.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/5 14:54
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("upload")
    public R<?> upload(MultipartFile file) {
        String s = fileService.uploadFile(file);
        return R.success(s);
    }
}
