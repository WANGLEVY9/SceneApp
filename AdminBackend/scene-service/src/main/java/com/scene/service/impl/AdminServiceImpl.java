package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.exception.UnauthorizedException;
import com.scene.mapper.AdminMapper;
import com.scene.model.dto.AdminLoginRequest;
import com.scene.model.dto.AdminLoginResponse;
import com.scene.model.entity.Admin;
import com.scene.service.AdminService;
import com.scene.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {
        // 查询管理员并校验密码
        Admin admin = lambdaQuery()
                .eq(Admin::getUsername, request.getUsername())
                .one();
        if (admin == null || !passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new UnauthorizedException("用户名或密码错误");
        }
        // 签发 admin 角色的 JWT
        String token = jwtService.generateToken(admin.getUsername(), Map.of("role", "admin"));
        return new AdminLoginResponse(token, admin.getUsername());
    }
}

