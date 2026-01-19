package com.scene.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GiftExchangeDTO {
    @NotBlank(message = "礼品ID不能为空")
    private String giftId;
}