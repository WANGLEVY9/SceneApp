package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.SceneSpotListResponse;
import com.scene.model.dto.SceneSpotUpsertRequest;
import com.scene.model.entity.SceneSpot;

public interface SceneSpotService extends IService<SceneSpot> {

    /**
     * 搜索景点（分页）
     */
    SceneSpotListResponse search(String keyword, long page, long size);

    /**
     * 新增景点
     */
    void create(SceneSpotUpsertRequest request);

    /**
     * 按ID删除景点
     */
    void removeById(String id);
}

