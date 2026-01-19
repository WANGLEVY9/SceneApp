package com.scene.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GiftExchangeRecordVO {
    private String id;
    private String giftName;
    private Integer giftNumber;
    private Integer points;
    private LocalDateTime applyTime;
    private String status;
}