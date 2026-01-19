package com.scene.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scene.domain.po.DynamicComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态评论Mapper接口
 */
@Mapper
public interface DynamicCommentMapper extends BaseMapper<DynamicComment> {

}