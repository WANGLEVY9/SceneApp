package com.scene.service;

import com.scene.domain.vo.SceneIntroVO;

/**
 * 景区引领区服务接口
 */
public interface ISceneIntroService {
    /**
     * 获取景区引领区详情
     * @return 景区引领区详情
     */
    SceneIntroVO getIntro();
}