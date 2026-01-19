package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.CheckinRecordListResponse;
import com.scene.model.entity.CheckinRecord;

public interface CheckinRecordService extends IService<CheckinRecord> {

    /**
     * 分页查询打卡记录
     */
    CheckinRecordListResponse pageList(String userName, String spotName, long page, long size);

    /**
     * 删除打卡记录
     */
    void deleteById(String id);
}

