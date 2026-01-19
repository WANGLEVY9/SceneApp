package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.AdminLoginRequest;
import com.scene.model.dto.AdminLoginResponse;
import com.scene.model.entity.Admin;

public interface AdminService extends IService<Admin> {

    /**
     * 管理员登录
     */
    AdminLoginResponse login(AdminLoginRequest request);
}

