package com.haier.user.service.impl;

import com.haier.core.util.AssertUtils;
import com.haier.user.dao.UserMapper;
import com.haier.user.domain.User;
import com.haier.user.service.UserService;
import com.haier.user.api.domain.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:55
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserVO selectUserByUserName(String username) {
        User user = userMapper.selectUserByUserName(username);
        AssertUtils.notEmpty(user, "不存在用户：" + username);
        // 先用BeanUtil 后面改为MapStruct
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
