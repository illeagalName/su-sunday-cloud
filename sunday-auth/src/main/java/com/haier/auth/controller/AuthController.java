package com.haier.auth.controller;

import com.haier.auth.domain.LoginUser;
import com.haier.auth.service.AuthService;
import com.haier.core.domain.R;
import com.haier.user.api.domain.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * token 控制
 *
 * @author
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("login")
    public R<UserVO> login(@RequestBody LoginUser loginUser) {
        return authService.login(loginUser);
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        return null;
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        return null;
    }

    @GetMapping("user")
    public R<?> getUserInfo(@RequestParam("token") String token) {
        return null;
    }
}
