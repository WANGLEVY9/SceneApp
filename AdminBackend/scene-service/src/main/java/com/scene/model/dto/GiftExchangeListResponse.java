package com.scene.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 礼品兑换记录列表响应
 */
@Data
public class GiftExchangeListResponse {

    private List<GiftExchangeListItem> list;

    private Long total;
}

