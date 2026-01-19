package com.scene.controller.admin;

import com.scene.common.domain.R;
import com.scene.model.dto.SceneIntroResponse;
import com.scene.model.dto.SceneIntroUpdateRequest;
import com.scene.service.SceneIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/intro")
public class SceneIntroController {

    @Autowired
    private SceneIntroService sceneIntroService;

    @PutMapping
    public R<Void> updateIntro(@Valid @RequestBody SceneIntroUpdateRequest request) {
        // 创建或更新景区引领区介绍
        sceneIntroService.upsertIntro(request);
        return new R<>(200, "操作成功", null);
    }

    @GetMapping
    public R<SceneIntroResponse> getIntro() {
        // 查询景区引领区介绍（包含图文视频）
        SceneIntroResponse resp = sceneIntroService.getIntro();
        return new R<>(200, "OK", resp);
    }
}

