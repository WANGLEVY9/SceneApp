package com.scene.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 推荐商家视图对象
 */
@Data
@ApiModel(value = "推荐商家视图对象", description = "推荐商家信息")
public class RecommendMerchantVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家ID")
    private String id;

    @ApiModelProperty(value = "商家名称")
    private String name;

    @ApiModelProperty(value = "距离")
    private String distance;
}