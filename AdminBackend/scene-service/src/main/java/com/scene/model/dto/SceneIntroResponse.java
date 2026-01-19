package com.scene.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 景区引领区介绍响应
 */
@Data
public class SceneIntroResponse {
    private String id;
    private String title;
    private String text;
    private List<String> images;
    private String video;
}

