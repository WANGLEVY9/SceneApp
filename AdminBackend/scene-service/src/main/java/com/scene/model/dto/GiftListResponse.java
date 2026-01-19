package com.scene.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 礼品列表响应
 */
@Data
public class GiftListResponse {

    private List<GiftListItem> list;

    private Long total;
}

