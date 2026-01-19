package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.SceneSpotListResponse;
import com.scene.model.dto.SceneSpotUpsertRequest;
import com.scene.service.SceneSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/scenic-spots")
public class SceneSpotController {

    @Autowired
    private SceneSpotService sceneSpotService;

    @GetMapping
    public R<SceneSpotListResponse> list(@RequestParam(required = false) String keyword,
                                         @RequestParam(defaultValue = "1") long page,
                                         @RequestParam(defaultValue = "10") long size) {
        // 搜索景点，支持分页
        SceneSpotListResponse resp = sceneSpotService.search(keyword, page, size);
        return new R<>(200, "OK", resp);
    }

    @PostMapping
    public R<Void> upsert(@Valid @RequestBody SceneSpotUpsertRequest request) {
        // 新增景点
        sceneSpotService.create(request);
        return new R<>(200, "操作成功", null);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id) {
        // 按ID删除景点
        sceneSpotService.removeById(id);
        return new R<>(200, "操作成功", null);
    }
}

