package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.GiftExchangeListResponse;
import com.scene.model.entity.GiftExchangeRecord;

/**
 * 礼品兑换记录服务
 */
public interface GiftExchangeRecordService extends IService<GiftExchangeRecord> {

    /**
     * 查询礼品兑换记录列表（分页，支持状态筛选）
     */
    GiftExchangeListResponse list(String status, long page, long size);

    /**
     * 更新兑换申请状态
     */
    void updateStatus(String id, String status);

    /**
     * 按ID删除礼品兑换记录
     */
    void deleteById(String id);
}

