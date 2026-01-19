package com.scene.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 打卡点位
 */
@Data
@TableName("checkin_spot")
public class CheckinSpot {

    @TableId(type = IdType.INPUT)
    private String id;

    private String name;

    private Double lat;

    private Double lng;

    private Integer radius;

    private Integer hot;
}

