package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.CheckinSpotCreateRequest;
import com.scene.model.dto.CheckinSpotListItem;
import com.scene.service.CheckinSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/checkin/spots")
public class CheckinSpotAdminController {

    @Autowired
    private CheckinSpotService checkinSpotService;

    @GetMapping
    public R<List<CheckinSpotListItem>> list() {
        // 获取全部打卡点位
        List<CheckinSpotListItem> list = checkinSpotService.listAll();
        return new R<>(200, "OK", list);
    }

    @PostMapping
    public R<Void> create(@Valid @RequestBody CheckinSpotCreateRequest request) {
        // 新增打卡点位
        checkinSpotService.create(request);
        return new R<>(200, "操作成功", null);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id) {
        // 删除打卡点位
        checkinSpotService.deleteById(id);
        return new R<>(200, "操作成功", null);
    }
}

