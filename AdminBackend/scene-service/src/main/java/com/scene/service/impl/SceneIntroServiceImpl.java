package com.scene.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scene.common.exception.NotFoundException;
import com.scene.mapper.SceneIntroMapper;
import com.scene.model.dto.SceneIntroResponse;
import com.scene.model.dto.SceneIntroUpdateRequest;
import com.scene.model.entity.SceneIntro;
import com.scene.service.SceneIntroService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SceneIntroServiceImpl extends ServiceImpl<SceneIntroMapper, SceneIntro> implements SceneIntroService {

    @Override
    public void upsertIntro(SceneIntroUpdateRequest request) {
        // 将 images 列表转为逗号分隔字符串
        List<String> images = request.getImages();
        String imagesStr = (images == null || images.isEmpty()) ? "" : images.stream().collect(Collectors.joining(","));

        SceneIntro intro = new SceneIntro();
        intro.setId("1");
        intro.setTitle(request.getTitle());
        intro.setText(request.getText());
        intro.setImages(imagesStr);
        intro.setVideo(request.getVideo());

        // 直接按主键做保存或更新
        this.saveOrUpdate(intro);
    }

    @Override
    public SceneIntroResponse getIntro() {
        // 取第一条引领区信息
        SceneIntro intro = this.lambdaQuery().last("limit 1").one();
        if (intro == null) {
            throw new NotFoundException("引领区内容不存在");
        }
        SceneIntroResponse resp = new SceneIntroResponse();
        resp.setId(intro.getId());
        resp.setTitle(intro.getTitle());
        resp.setText(intro.getText());
        // 将逗号分隔的 images 转回列表
        if (intro.getImages() != null && !intro.getImages().isEmpty()) {
            resp.setImages(Arrays.asList(intro.getImages().split(",")));
        }
        resp.setVideo(intro.getVideo());
        return resp;
    }
}

