package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.domain.dto.*;
import com.scene.domain.po.User;
import com.scene.domain.vo.UserVO;


/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface IUserService extends IService<User> {
    void sendCode(String phone);

    UserVO login(LoginFormDTO loginFormDTO);

    UserVO getUserInfo(Long userId);

    void setupAccount(Long userId, SetupAccountDTO setupAccountDTO);

    void changeUsername(Long userId, String username);

    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);

    void updateProfile(Long userId, UpdateProfileDTO updateProfileDTO);
}
