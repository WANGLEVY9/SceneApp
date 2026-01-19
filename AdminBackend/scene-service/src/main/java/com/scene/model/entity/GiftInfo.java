package com.scene.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 礼品信息
 */
@Data
@TableName("gift_info")
public class GiftInfo {

    @TableId(type = IdType.INPUT)
    private String id;

    private String name;

    private Integer points;

    private Integer stock;

    private String image;

    @TableField("`desc`") 
    private String desc;

    private Integer status;
}

