package com.scene.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 礼品兑换记录
 */
@Data
@TableName("gift_exchange_record")
public class GiftExchangeRecord {

    @TableId(type = IdType.INPUT)
    private String id;

    private String userId;

    private String userName;

    private String giftId;

    private String giftName;

    private Integer giftNumber;

    private Integer points;

    private LocalDateTime applyTime;

    private String status;
}

