package com.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.scene.model.dto.CheckinSpotCreateRequest;
import com.scene.model.dto.CheckinSpotListItem;
import com.scene.model.entity.CheckinSpot;

import java.util.List;

public interface CheckinSpotService extends IService<CheckinSpot> {

    /**
     * 获取全部打卡点位
     */
    List<CheckinSpotListItem> listAll();

    /**
     * 新增打卡点位
     */
    void create(CheckinSpotCreateRequest request);

    /**
     * 删除打卡点位
     */
    void deleteById(String id);
}

