package com.scene.service;

import com.scene.common.domain.PageDTO;
import com.scene.domain.dto.SceneMerchantQueryDTO;
import com.scene.domain.vo.SceneMerchantVO;

/**
 * 商家服务接口
 */
public interface ISceneMerchantService {
    /**
     * 获取商家列表
     * @param queryDTO 查询条件
     * @return 商家列表
     */
    PageDTO<SceneMerchantVO> queryMerchants(SceneMerchantQueryDTO queryDTO);
}