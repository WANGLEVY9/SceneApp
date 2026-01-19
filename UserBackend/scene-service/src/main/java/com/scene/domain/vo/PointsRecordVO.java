package com.scene.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointsRecordVO {
    private String id;
    private String type;
    private Integer points;
    private String remark;
    private LocalDateTime createTime;
}