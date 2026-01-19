package com.scene.model.dto;

import lombok.Data;

/**
 * 景点列表条目
 */
@Data
public class SceneSpotListItem {
    private String id;
    private String name;
    private Double lat;
    private Double lng;
    private String desc;
}

