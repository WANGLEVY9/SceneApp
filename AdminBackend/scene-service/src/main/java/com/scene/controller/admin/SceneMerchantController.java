package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.SceneMerchantListResponse;
import com.scene.model.dto.SceneMerchantUpsertRequest;
import com.scene.service.SceneMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/merchants")
public class SceneMerchantController {

    @Autowired
    private SceneMerchantService sceneMerchantService;

    @GetMapping
    public R<SceneMerchantListResponse> list(@RequestParam(defaultValue = "1") long page,
                                             @RequestParam(defaultValue = "10") long size) {
        // 分页查询商家
        SceneMerchantListResponse resp = sceneMerchantService.pageList(page, size);
        return new R<>(200, "OK", resp);
    }

    @PostMapping
    public R<Void> create(@Valid @RequestBody SceneMerchantUpsertRequest request) {
        // 新增商家
        sceneMerchantService.create(request);
        return new R<>(200, "操作成功", null);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable String id) {
        // 按ID删除商家
        sceneMerchantService.deleteById(id);
        return new R<>(200, "操作成功", null);
    }
}

