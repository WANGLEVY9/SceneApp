package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 动态评论实体类
 */
@Data
@TableName("dynamic_comment")
public class DynamicComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID（雪花ID）
     */
    @TableId
    private String id;

    /**
     * 对应的打卡ID（关联checkin_record.id）
     */
    private String checkinId;

    /**
     * 评论用户ID
     */
    private String userId;

    /**
     * 评论用户昵称（冗余）
     */
    private String nickname;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private LocalDateTime commentTime;

    /**
     * 状态：1-正常，0-已删除
     */
    private Integer status;
}