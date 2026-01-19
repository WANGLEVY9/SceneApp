package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.domain.po.SceneIntro;
import com.scene.domain.vo.SceneIntroVO;
import com.scene.mapper.SceneIntroMapper;
import com.scene.service.ISceneIntroService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 景区引领区服务实现类
 */
@Service
@RequiredArgsConstructor
public class SceneIntroServiceImpl extends ServiceImpl<SceneIntroMapper, SceneIntro> implements ISceneIntroService {

    @Override
    public SceneIntroVO getIntro() {
        // 查询景区引领区信息，默认查询ID为1的记录
        SceneIntro sceneIntro = getOne(new LambdaQueryWrapper<SceneIntro>()
                .eq(SceneIntro::getId, "1")
                .last("LIMIT 1"));
        
        if (sceneIntro == null) {
            return null;
        }
        
        // 转换为VO
        SceneIntroVO sceneIntroVO = new SceneIntroVO();
        BeanUtils.copyProperties(sceneIntro, sceneIntroVO);
        
        // 处理图片列表
        if (StringUtils.hasText(sceneIntro.getImages())) {
            List<String> imageList = Arrays.stream(sceneIntro.getImages().split(","))
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toList());
            sceneIntroVO.setImages(imageList);
        }
        
        return sceneIntroVO;
    }
}