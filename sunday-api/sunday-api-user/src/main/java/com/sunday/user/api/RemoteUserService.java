package com.sunday.user.api;

import com.haier.core.domain.R;
import com.sunday.user.api.domain.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:35
 */
public interface RemoteUserService {
    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @return UserVO
     */
    @GetMapping(value = "/user/info/{username}")
    R<UserVO> getUserInfo(@PathVariable("username") String username);
}
