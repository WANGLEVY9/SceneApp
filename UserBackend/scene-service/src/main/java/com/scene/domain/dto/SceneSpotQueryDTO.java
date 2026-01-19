package com.scene.domain.dto;

import com.scene.common.domain.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 景点查询条件
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "景点查询条件")
public class SceneSpotQueryDTO extends PageQuery implements Serializable {
    
    @ApiModelProperty(value = "关键词")
    private String keyword;
    
    @ApiModelProperty(value = "用户纬度")
    private BigDecimal lat;
    
    @ApiModelProperty(value = "用户经度")
    private BigDecimal lng;
}