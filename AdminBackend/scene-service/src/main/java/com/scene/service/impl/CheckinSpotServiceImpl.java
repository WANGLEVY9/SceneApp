package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.exception.NotFoundException;
import com.scene.mapper.CheckinSpotMapper;
import com.scene.model.dto.CheckinSpotCreateRequest;
import com.scene.model.dto.CheckinSpotListItem;
import com.scene.model.entity.CheckinSpot;
import com.scene.service.CheckinSpotService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CheckinSpotServiceImpl extends ServiceImpl<CheckinSpotMapper, CheckinSpot> implements CheckinSpotService {

    @Override
    public List<CheckinSpotListItem> listAll() {
        List<CheckinSpot> spots = this.list();
        List<CheckinSpotListItem> list = new ArrayList<>(spots.size());
        for (CheckinSpot spot : spots) {
            CheckinSpotListItem item = new CheckinSpotListItem();
            item.setId(spot.getId());
            item.setName(spot.getName());
            item.setLat(spot.getLat());
            item.setLng(spot.getLng());
            item.setRadius(spot.getRadius());
            list.add(item);
        }
        return list;
    }

    @Override
    public void create(CheckinSpotCreateRequest request) {
        CheckinSpot spot = new CheckinSpot();
        spot.setId(UUID.randomUUID().toString().replace("-", ""));
        spot.setName(request.getName());
        spot.setLat(request.getLat());
        spot.setLng(request.getLng());
        spot.setRadius(request.getRadius());
        spot.setHot(0);
        this.save(spot);
    }

    @Override
    public void deleteById(String id) {
        // 检查数据是否存在
        CheckinSpot spot = this.getById(id);
        if (spot == null) {
            throw new NotFoundException("打卡点位不存在");
        }
        super.removeById(id);
    }
}

