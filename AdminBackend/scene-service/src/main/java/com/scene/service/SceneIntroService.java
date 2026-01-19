package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.SceneIntroResponse;
import com.scene.model.dto.SceneIntroUpdateRequest;
import com.scene.model.entity.SceneIntro;

public interface SceneIntroService extends IService<SceneIntro> {

    /**
     * 创建或更新景区引领区介绍
     */
    void upsertIntro(SceneIntroUpdateRequest request);

    /**
     * 查询景区引领区介绍（单条）
     */
    SceneIntroResponse getIntro();
}

