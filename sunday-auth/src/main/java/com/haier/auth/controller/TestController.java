package com.haier.auth.controller;

import com.haier.api.user.RemoteUserService;
import com.haier.api.user.domain.UserVO;
import com.haier.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/25 11:10
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    RemoteUserService remoteUserService;

    @GetMapping("user")
    public R<?> test() {
        R<UserVO> lisi = remoteUserService.getUserInfo("lisi");
        return lisi;
    }
}
