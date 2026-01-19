package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.exception.NotFoundException;
import com.scene.mapper.SceneSpotMapper;
import com.scene.model.dto.SceneSpotListItem;
import com.scene.model.dto.SceneSpotListResponse;
import com.scene.model.dto.SceneSpotUpsertRequest;
import com.scene.model.entity.SceneSpot;
import com.scene.service.SceneSpotService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SceneSpotServiceImpl extends ServiceImpl<SceneSpotMapper, SceneSpot> implements SceneSpotService {

    @Override
    public SceneSpotListResponse search(String keyword, long page, long size) {
        Page<SceneSpot> mpPage = new Page<>(page, size);
        this.lambdaQuery()
                .like(keyword != null && !keyword.isEmpty(), SceneSpot::getName, keyword)
                .page(mpPage);

        List<SceneSpot> records = mpPage.getRecords();
        List<SceneSpotListItem> list = new ArrayList<>(records.size());
        for (SceneSpot spot : records) {
            SceneSpotListItem item = new SceneSpotListItem();
            item.setId(spot.getId());
            item.setName(spot.getName());
            item.setLat(spot.getLat());
            item.setLng(spot.getLng());
            item.setDesc(spot.getDesc());
            list.add(item);
        }
        SceneSpotListResponse resp = new SceneSpotListResponse();
        resp.setList(list);
        resp.setTotal(mpPage.getTotal());
        return resp;
    }

    @Override
    public void create(SceneSpotUpsertRequest request) {
        SceneSpot spot = new SceneSpot();
        spot.setId(UUID.randomUUID().toString().replace("-", ""));
        spot.setName(request.getName());
        spot.setLat(request.getLat());
        spot.setLng(request.getLng());
        spot.setDesc(request.getDesc());
        this.save(spot);
    }

    @Override
    public void removeById(String id) {
        // 检查数据是否存在
        SceneSpot spot = this.getById(id);
        if (spot == null) {
            throw new NotFoundException("景点不存在");
        }
        super.removeById(id);
    }
}

