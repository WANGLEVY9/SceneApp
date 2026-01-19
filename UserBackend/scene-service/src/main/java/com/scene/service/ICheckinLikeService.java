package com.scene.service;

import com.scene.domain.dto.LikeDTO;
import com.scene.domain.vo.LikeResultVO;

/**
 * 打卡点赞服务接口
 */
public interface ICheckinLikeService {

    /**
     * 为打卡点赞
     * @param userId 用户ID
     * @param likeDTO 点赞请求
     * @return 点赞结果
     */
    LikeResultVO likeCheckin(String userId, LikeDTO likeDTO);
}