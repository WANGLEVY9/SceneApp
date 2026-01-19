package com.scene.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 景点分页响应
 */
@Data
public class SceneSpotListResponse {
    private List<SceneSpotListItem> list;
    private long total;
}

