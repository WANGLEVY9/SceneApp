package com.scene.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 新增礼品请求
 */
@Data
public class GiftCreateRequest {

    @NotBlank(message = "礼品名称不能为空")
    private String name;

    @NotNull(message = "所需积分不能为空")
    @Positive(message = "所需积分必须大于0")
    private Integer points;

    @NotNull(message = "库存数量不能为空")
    @Positive(message = "库存数量必须大于0")
    private Integer stock;

    private String image;

    private String desc;
}

