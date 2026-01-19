package com.scene.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scene.domain.po.SceneMerchant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商家Mapper接口
 */
@Mapper
public interface SceneMerchantMapper extends BaseMapper<SceneMerchant> {
}