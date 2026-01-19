package com.scene.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 打卡提交请求DTO
 */
@Data
@ApiModel(value = "打卡提交请求DTO", description = "打卡提交请求参数")
public class CheckinSubmitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "打卡点ID", required = true)
    @NotBlank(message = "打卡点ID不能为空")
    private String spotId;

    @ApiModelProperty(value = "打卡照片URL", required = true)
    @NotBlank(message = "打卡照片不能为空")
    private String image;

    @ApiModelProperty(value = "打卡备注")
    private String remark;

    @ApiModelProperty(value = "打卡位置", required = true)
    @NotNull(message = "打卡位置不能为空")
    private LocationVO location;

    @Data
    @ApiModel(value = "打卡位置")
    public static class LocationVO implements Serializable {
        
        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "纬度", required = true)
        @NotNull(message = "纬度不能为空")
        private BigDecimal lat;

        @ApiModelProperty(value = "经度", required = true)
        @NotNull(message = "经度不能为空")
        private BigDecimal lng;
    }
}