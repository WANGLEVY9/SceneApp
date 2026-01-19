package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.SceneMerchantListResponse;
import com.scene.model.dto.SceneMerchantUpsertRequest;
import com.scene.model.entity.SceneMerchant;

public interface SceneMerchantService extends IService<SceneMerchant> {

    /**
     * 分页查询商家
     */
    SceneMerchantListResponse pageList(long page, long size);

    /**
     * 新增商家
     */
    void create(SceneMerchantUpsertRequest request);

    /**
     * 按ID删除商家
     */
    void deleteById(String id);
}

