package com.scene.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增商家请求
 */
@Data
public class SceneMerchantUpsertRequest {

    @NotBlank(message = "商家名称不能为空")
    private String name;

    @NotBlank(message = "联系方式不能为空")
    private String contact;

    @NotNull(message = "纬度不能为空")
    private Double lat;

    @NotNull(message = "经度不能为空")
    private Double lng;
}

