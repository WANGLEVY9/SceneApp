package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("gift_info")
public class GiftInfo {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String name;
    private Integer points;
    private Integer stock;
    private String image;
    private String description;
    private Integer status;
}