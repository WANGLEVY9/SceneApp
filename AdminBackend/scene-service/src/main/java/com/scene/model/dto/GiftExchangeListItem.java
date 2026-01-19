package com.scene.model.dto;

import lombok.Data;

/**
 * 礼品兑换记录列表项
 */
@Data
public class GiftExchangeListItem {

    private String id;

    private String userName;

    private String giftName;

    private Integer giftNumber;

    private Integer points;

    private String applyTime;

    private String status;
}

