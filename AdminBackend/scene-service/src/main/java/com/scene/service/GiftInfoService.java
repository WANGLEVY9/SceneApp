package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.GiftCreateRequest;
import com.scene.model.dto.GiftListResponse;
import com.scene.model.entity.GiftInfo;

/**
 * 礼品信息服务
 */
public interface GiftInfoService extends IService<GiftInfo> {

    /**
     * 查询礼品列表（分页）
     */
    GiftListResponse list(long page, long size);

    /**
     * 新增礼品
     */
    void create(GiftCreateRequest request);

    /**
     * 按ID删除礼品
     */
    void removeById(String id);
}

