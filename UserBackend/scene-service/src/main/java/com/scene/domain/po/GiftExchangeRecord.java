package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gift_exchange_record")
public class GiftExchangeRecord {
    @TableId(type = IdType.ASSIGN_ID)
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