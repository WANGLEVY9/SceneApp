package com.scene.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scene.domain.po.CheckinLike;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打卡点赞Mapper接口
 */
@Mapper
public interface CheckinLikeMapper extends BaseMapper<CheckinLike> {
}