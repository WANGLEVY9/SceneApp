package com.scene;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.scene.mapper")
@SpringBootApplication
public class SceneApplication {
    public static void main(String[] args) {
        SpringApplication.run(com.scene.SceneApplication.class, args);
    }
}