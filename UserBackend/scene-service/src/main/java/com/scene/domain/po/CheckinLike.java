package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 打卡点赞实体类
 */
@Data
@TableName("checkin_like")
public class CheckinLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 点赞用户ID
     */
    private String userId;

    /**
     * 打卡记录ID
     */
    private String checkinId;
}