package com.scene.model.dto;

import lombok.Data;

/**
 * 商家列表条目
 */
@Data
public class SceneMerchantListItem {
    private String id;
    private String name;
    private String contact;
    private Double lat;
    private Double lng;
}

