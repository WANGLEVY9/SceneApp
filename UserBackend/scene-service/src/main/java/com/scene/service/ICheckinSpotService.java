package com.scene.service;

import com.scene.domain.vo.CheckinSpotVO;

import java.util.List;

/**
 * 打卡点位服务接口
 */
public interface ICheckinSpotService {

    /**
     * 获取所有打卡点位列表
     * @return 打卡点位列表
     */
    List<CheckinSpotVO> listSpots();
}