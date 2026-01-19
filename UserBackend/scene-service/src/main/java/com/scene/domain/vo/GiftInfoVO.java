package com.scene.domain.vo;

import lombok.Data;

@Data
public class GiftInfoVO {
    private String id;
    private String name;
    private Integer points;
    private Integer stock;
    private String image;
    private String description;
    private Integer status;
}