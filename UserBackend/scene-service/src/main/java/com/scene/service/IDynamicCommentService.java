package com.scene.service;

import com.scene.domain.dto.CommentDynamicDTO;
import com.scene.domain.vo.CommentResultVO;

/**
 * 动态评论服务接口
 */
public interface IDynamicCommentService {

    /**
     * 评论动态
     *
     * @param userId 用户ID
     * @param commentDTO 评论请求DTO
     * @return 评论结果VO
     */
    CommentResultVO commentDynamic(String userId, CommentDynamicDTO commentDTO);
}