package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 打卡记录实体类
 */
@Data
@TableName("checkin_record")
public class CheckinRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 打卡记录ID（雪花ID）
     */
    @TableId
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称（冗余）
     */
    private String userName;

    /**
     * 打卡点ID
     */
    private String spotId;

    /**
     * 打卡点名称（冗余）
     */
    private String spotName;

    /**
     * 打卡照片URL
     */
    private String image;

    /**
     * 打卡备注
     */
    private String remark;

    /**
     * 用户打卡时纬度
     */
    private BigDecimal lat;

    /**
     * 用户打卡时经度
     */
    private BigDecimal lng;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 打卡时间
     */
    private LocalDateTime checkinTime;

    /**
     * 状态：1-正常，0-违规（已删除）
     */
    private Integer status;
}