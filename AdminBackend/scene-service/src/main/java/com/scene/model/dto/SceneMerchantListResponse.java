package com.scene.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 商家分页响应
 */
@Data
public class SceneMerchantListResponse {
    private List<SceneMerchantListItem> list;
    private long total;
}

