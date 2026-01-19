package com.scene.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scene.domain.po.SceneSpot;
import org.apache.ibatis.annotations.Mapper;

/**
 * 景点Mapper接口
 */
@Mapper
public interface SceneSpotMapper extends BaseMapper<SceneSpot> {
}