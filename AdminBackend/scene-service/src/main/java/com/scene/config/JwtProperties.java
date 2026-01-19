package com.scene.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Data
@Component
@ConfigurationProperties(prefix = "hm.jwt")
public class JwtProperties {
    /**
     * keystore 路径，例：classpath:hmall.jks
     */
    private String location;
    /**
     * keystore 别名
     */
    private String alias;
    /**
     * keystore 密码
     */
    private String password;
    /**
     * token 有效期
     */
    private Duration tokenTTL = Duration.ofMinutes(30);
}

