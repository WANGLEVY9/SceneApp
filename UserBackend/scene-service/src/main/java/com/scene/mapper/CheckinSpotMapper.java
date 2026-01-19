package com.scene.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scene.domain.po.CheckinSpot;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打卡点位Mapper接口
 */
@Mapper
public interface CheckinSpotMapper extends BaseMapper<CheckinSpot> {
}