package com.scene.interceptor;

import com.scene.common.exception.UnauthorizedException;
import com.scene.config.AuthProperties;
import com.scene.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 鉴权拦截器
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthProperties authProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // 检查是否在排除路径中
        if (isExcludedPath(uri)) {
            return true;
        }

        // 从请求头中获取 token
        String token = extractToken(request);
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException("未提供认证令牌");
        }

        // 验证 token
        try {
            Claims claims = jwtService.parse(token);
            // 可以将用户信息存储到 ThreadLocal 中，供后续使用
            String username = claims.getSubject();
            log.debug("用户 {} 通过 JWT 验证", username);
            return true;
        } catch (Exception e) {
            log.warn("JWT 验证失败: {}", e.getMessage());
            throw new UnauthorizedException("认证令牌无效或已过期");
        }
    }

    /**
     * 从请求头中提取 token
     */
    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        String prefix = "Bearer ";
        if (authorization.startsWith(prefix)) {
            return authorization.substring(prefix.length());
        }
        return authorization;
    }

    /**
     * 检查路径是否在排除列表中
     */
    private boolean isExcludedPath(String uri) {
        if (authProperties.getExcludePaths() == null) {
            return false;
        }
        for (String excludePath : authProperties.getExcludePaths()) {
            if (uri.equals(excludePath) || uri.startsWith(excludePath + "/")) {
                return true;
            }
        }
        return false;
    }
}

