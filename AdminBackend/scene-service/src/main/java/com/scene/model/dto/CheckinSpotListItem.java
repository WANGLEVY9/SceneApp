package com.scene.model.dto;

import lombok.Data;

/**
 * 打卡点位列表条目
 */
@Data
public class CheckinSpotListItem {
    private String id;
    private String name;
    private Double lat;
    private Double lng;
    private Integer radius;
}

