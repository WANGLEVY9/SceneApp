package com.scene.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增或更新景点请求
 */
@Data
public class SceneSpotUpsertRequest {

    @NotBlank(message = "景点名称不能为空")
    private String name;

    @NotNull(message = "纬度不能为空")
    private Double lat;

    @NotNull(message = "经度不能为空")
    private Double lng;

    private String desc;
}

