package com.scene.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.scene.common.domain.R;
import com.scene.common.utils.UserContext;
import com.scene.domain.dto.CommentDynamicDTO;
import com.scene.domain.vo.CommentResultVO;
import com.scene.domain.vo.DynamicVO;
import com.scene.domain.vo.RecommendCheckinSpotVO;
import com.scene.domain.vo.RecommendMerchantVO;
import com.scene.service.IDynamicCommentService;
import com.scene.service.IDynamicService;
import com.scene.service.IRecommendCheckinService;
import com.scene.service.IRecommendMerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 动态与推荐接口控制器
 */
@Api(tags = "动态与推荐接口")
@RestController
@RequestMapping("/api/client")
public class DynamicController {

    @Autowired
    private IRecommendCheckinService recommendCheckinService;

    @Autowired
    private IRecommendMerchantService recommendMerchantService;

    @Autowired
    private IDynamicService dynamicService;

    @Autowired
    private IDynamicCommentService dynamicCommentService;

    /**
     * 获取推荐打卡点
     */
    @ApiOperation(value = "获取推荐打卡点", notes = "根据用户当前位置，推荐附近的热门打卡点")
    @GetMapping("/recommend/checkin")
    public R<Page<RecommendCheckinSpotVO>> getRecommendCheckinSpots(
            @RequestParam String sort,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Double lat,
            @RequestParam Double lng) {
        Page<RecommendCheckinSpotVO> result = recommendCheckinService.getRecommendCheckinSpots(sort, page, size, lat, lng);
        return R.ok(result);
    }

    /**
     * 获取推荐商家
     */
    @ApiOperation(value = "获取推荐商家", notes = "推荐景区内的热门商家")
    @GetMapping("/recommend/merchants")
    public R<Page<RecommendMerchantVO>> getRecommendMerchants(
            @RequestParam String sort,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Double lat,
            @RequestParam Double lng) {
        Page<RecommendMerchantVO> result = recommendMerchantService.getRecommendMerchants(sort, page, size, lat, lng);
        return R.ok(result);
    }

    /**
     * 获取动态流
     */
    @ApiOperation(value = "获取动态流", notes = "获取所有用户最新的打卡动态，支持分页")
    @GetMapping("/dynamic")
    public R<Page<DynamicVO>> getDynamicFlow(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<DynamicVO> result = dynamicService.getDynamicFlow(page, size);
        return R.ok(result);
    }

    /**
     * 评论动态
     */
    @ApiOperation(value = "评论动态", notes = "对动态流中的内容进行评论，评论者可获得积分")
    @PostMapping("/dynamic/comment")
    public R<CommentResultVO> commentDynamic(
            @RequestHeader("Authorization") String token,
            @RequestBody CommentDynamicDTO commentDTO) {
        // 从token中获取用户ID（这里简化处理，实际项目中需要解析JWT）
        String userId = UserContext.getUser().toString();
        
        CommentResultVO result = dynamicCommentService.commentDynamic(userId, commentDTO);
        return R.ok(result);
    }

}