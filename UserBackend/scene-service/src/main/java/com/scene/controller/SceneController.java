package com.scene.controller;

import com.scene.common.domain.PageDTO;
import com.scene.common.domain.R;
import com.scene.domain.dto.SceneMerchantQueryDTO;
import com.scene.domain.dto.SceneSpotQueryDTO;
import com.scene.domain.vo.SceneIntroVO;
import com.scene.domain.vo.SceneMerchantVO;
import com.scene.domain.vo.SceneSpotVO;
import com.scene.service.ISceneIntroService;
import com.scene.service.ISceneMerchantService;
import com.scene.service.ISceneSpotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 景区相关接口
 */
@Api(tags = "景区相关接口")
@RestController
@RequestMapping("api/client")
@RequiredArgsConstructor
public class SceneController {

    private final ISceneIntroService sceneIntroService;
    private final ISceneMerchantService sceneMerchantService;
    private final ISceneSpotService sceneSpotService;

    @ApiOperation("获取引领区详情")
    @GetMapping("/intro")
    public R<SceneIntroVO> getIntro() {
        return R.ok(sceneIntroService.getIntro());
    }

    @ApiOperation("获取商家列表")
    @GetMapping("/merchants")
    public R<PageDTO<SceneMerchantVO>> getMerchants(SceneMerchantQueryDTO queryDTO) {
        return R.ok(sceneMerchantService.queryMerchants(queryDTO));
    }

    @ApiOperation("获取景点列表")
    @GetMapping("/scenic-spots")
    public R<PageDTO<SceneSpotVO>> getSpots(SceneSpotQueryDTO queryDTO) {
        return R.ok(sceneSpotService.querySpots(queryDTO));
    }
}