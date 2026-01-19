package com.scene.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 打卡记录列表条目
 */
@Data
public class CheckinRecordListItem {
    private String id;
    private String userName;
    private String spotName;
    private LocalDateTime time;
    private String image;
    private String remark;
    private Integer likeCount;
}

