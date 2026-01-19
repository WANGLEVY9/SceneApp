package com.scene.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增打卡点位请求
 */
@Data
public class CheckinSpotCreateRequest {

    @NotBlank(message = "名称不能为空")
    private String name;

    @NotNull(message = "纬度不能为空")
    private Double lat;

    @NotNull(message = "经度不能为空")
    private Double lng;

    @NotNull(message = "半径不能为空")
    private Integer radius;
}

