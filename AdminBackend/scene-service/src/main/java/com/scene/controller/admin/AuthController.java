package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.AdminLoginRequest;
import com.scene.model.dto.AdminLoginResponse;
import com.scene.service.AdminService;
import com.scene.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public R<AdminLoginResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        // 管理员登录，返回签发的 JWT
        AdminLoginResponse response = adminService.login(request);
        return new R<>(200, "登录成功", response);
    }

    @PostMapping("/logout")
    public R<Void> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        // 直接在方法内提取并注销 token，避免辅助方法
        if (StringUtils.hasText(authorization)) {
            String prefix = "Bearer ";
            String token = authorization.startsWith(prefix) ? authorization.substring(prefix.length()) : authorization;
            if (StringUtils.hasText(token)) {
                jwtService.invalidate(token);
            }
        }
        return new R<>(200, "登出成功", null);
    }
}

