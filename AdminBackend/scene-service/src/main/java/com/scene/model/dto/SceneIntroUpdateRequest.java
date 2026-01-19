package com.scene.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 更新或创建景区引领区介绍的请求体
 */
@Data
public class SceneIntroUpdateRequest {

    // @NotBlank(message = "id不能为空")
    // private String id;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "介绍内容不能为空")
    private String text;

    private List<String> images;

    private String video;
}

