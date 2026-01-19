package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 打卡提交结果VO
 */
@Data
@ApiModel(value = "打卡提交结果VO", description = "打卡提交结果")
public class CheckinResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡记录ID")
    private String checkinId;

    @ApiModelProperty(value = "获得积分")
    private Integer points;
}