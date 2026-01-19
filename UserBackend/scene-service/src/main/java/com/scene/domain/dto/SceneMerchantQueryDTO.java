package com.scene.domain.dto;

import com.scene.common.domain.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商家查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商家查询条件")
public class SceneMerchantQueryDTO extends PageQuery implements Serializable {
    
    @ApiModelProperty("排序方式")
    private String sort;
    
    @ApiModelProperty("用户纬度")
    private BigDecimal lat;
    
    @ApiModelProperty("用户经度")
    private BigDecimal lng;
}