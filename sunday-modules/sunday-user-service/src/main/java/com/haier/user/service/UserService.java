package com.haier.user.service;

import com.haier.api.user.domain.UserVO;
import com.haier.user.vo.request.RegisterUserVO;
import com.haier.user.vo.response.PersonalInfoVO;
import com.haier.user.vo.response.MenuVO;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:29
 */
public interface UserService {
    UserVO selectUserByUserName(String username, String password);

    PersonalInfoVO getPersonalInfo();

    Boolean registerUser(RegisterUserVO request);

    List<MenuVO> listMenus();

    Object todayElectricity();
}
