package com.scene.service;

import com.scene.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtService {

    private final KeyPair keyPair;
    private final Duration ttl;
    private final Map<String, Instant> blackList = new ConcurrentHashMap<>();

    public JwtService(JwtProperties properties, ResourceLoader resourceLoader) {
        Resource resource = resolveResource(properties.getLocation(), resourceLoader);
        if (!resource.exists()) {
            throw new IllegalStateException("JWT keystore 未找到: " + properties.getLocation());
        }
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(resource, properties.getPassword().toCharArray());
        this.keyPair = factory.getKeyPair(properties.getAlias(), properties.getPassword().toCharArray());
        this.ttl = Objects.requireNonNullElse(properties.getTokenTTL(), Duration.ofMinutes(30));
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl)))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256);
        if (claims != null && !claims.isEmpty()) {
            builder.addClaims(claims);
        }
        return builder.compact();
    }

    public Claims parse(String token) {
        cleanupBlackList();
        if (isInvalid(token)) {
            throw new IllegalStateException("Token 已失效");
        }
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void invalidate(String token) {
        blackList.put(token, Instant.now().plus(ttl));
        cleanupBlackList();
    }

    private boolean isInvalid(String token) {
        return blackList.containsKey(token);
    }

    private void cleanupBlackList() {
        Instant now = Instant.now();
        blackList.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }

    private Resource resolveResource(String location, ResourceLoader resourceLoader) {
        return resourceLoader.getResource(location);
    }
}

