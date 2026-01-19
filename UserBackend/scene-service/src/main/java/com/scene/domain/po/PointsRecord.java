package com.scene.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("points_record")
public class PointsRecord {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String userId;
    private String type;
    private Integer points;
    private String remark;
    private LocalDateTime createTime;
}