package com.scene.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打卡记录
 */
@Data
@TableName("checkin_record")
public class CheckinRecord {

    @TableId(type = IdType.INPUT)
    private String id;

    private String userId;

    private String userName;

    private String spotId;

    private String spotName;

    private String image;

    private String remark;

    private Double lat;

    private Double lng;

    private Integer likeCount;

    private LocalDateTime checkinTime;

    private Integer status;
}

