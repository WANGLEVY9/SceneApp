package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 打卡点位实体类
 */
@Data
@TableName("checkin_spot")
public class CheckinSpot implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 打卡点ID（雪花ID）
     */
    @TableId
    private String id;

    /**
     * 打卡点名称
     */
    private String name;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 有效半径（米）- GPS校验用
     */
    private Integer radius;

    /**
     * 热度值（用于推荐）
     */
    private Integer hot;
}