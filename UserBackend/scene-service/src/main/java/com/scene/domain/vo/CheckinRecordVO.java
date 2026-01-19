package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 打卡记录视图对象
 */
@Data
@ApiModel(value = "打卡记录视图对象", description = "打卡记录信息")
public class CheckinRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡记录ID")
    private String id;

    @ApiModelProperty(value = "打卡点名称")
    private String spotName;

    @ApiModelProperty(value = "打卡时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "打卡照片URL")
    private String image;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "打卡备注")
    private String remark;
}