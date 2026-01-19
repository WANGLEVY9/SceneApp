package com.scene.service;

import com.scene.common.domain.PageDTO;
import com.scene.domain.dto.SceneSpotQueryDTO;
import com.scene.domain.vo.SceneSpotVO;

/**
 * 景点服务接口
 */
public interface ISceneSpotService {
    /**
     * 获取景点列表
     * @param queryDTO 查询条件
     * @return 景点列表
     */
    PageDTO<SceneSpotVO> querySpots(SceneSpotQueryDTO queryDTO);
}