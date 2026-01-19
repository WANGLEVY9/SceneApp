package com.scene.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 鉴权配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "hm.auth")
public class AuthProperties {

    /**
     * 排除鉴权的路径列表
     */
    private List<String> excludePaths = new ArrayList<>();
}

