package com.haier.auth.controller;

import com.haier.auth.domain.LoginUser;
import com.haier.auth.service.AuthService;
import com.haier.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * token 控制
 *
 * @author
 */
@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("login")
    public R<Map<String, Object>> login(@RequestBody LoginUser loginUser) {
        return authService.login(loginUser);
    }

    @DeleteMapping("logout")
    public R<?> logout() {
        return authService.logout();
    }

    @PostMapping("refresh")
    public R<?> refresh() {
        return null;
    }
}
