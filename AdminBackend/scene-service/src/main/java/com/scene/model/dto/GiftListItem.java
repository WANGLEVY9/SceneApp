package com.scene.model.dto;

import lombok.Data;

/**
 * 礼品列表项
 */
@Data
public class GiftListItem {

    private String id;

    private String name;

    private Integer points;

    private Integer stock;

    private String image;

    private String desc;

    private Integer status;
}

