package com.scene.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.scene.domain.dto.CommentDynamicDTO;
import com.scene.domain.po.CheckinRecord;
import com.scene.domain.po.DynamicComment;
import com.scene.domain.po.User;
import com.scene.domain.vo.CommentResultVO;
import com.scene.mapper.CheckinRecordMapper;
import com.scene.mapper.DynamicCommentMapper;
import com.scene.mapper.UserMapper;
import com.scene.service.IDynamicCommentService;
import com.scene.util.SnowflakeIdUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 动态评论服务实现类
 */
@Service
public class DynamicCommentServiceImpl implements IDynamicCommentService {

    @Autowired
    private DynamicCommentMapper dynamicCommentMapper;

    @Autowired
    private CheckinRecordMapper checkinRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public CommentResultVO commentDynamic(String userId, CommentDynamicDTO commentDTO) {
        // 检查打卡记录是否存在
        CheckinRecord record = checkinRecordMapper.selectById(commentDTO.getDynamicId());
        if (record == null) {
            throw new RuntimeException("动态不存在");
        }

        // 获取用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 创建评论记录
        DynamicComment comment = new DynamicComment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setId(SnowflakeIdUtil.generateId());
        comment.setCheckinId(commentDTO.getDynamicId());
        comment.setUserId(userId);
        comment.setNickname(user.getNickname());
        comment.setCommentTime(LocalDateTime.now());
        comment.setStatus(1);

        // 保存评论
        dynamicCommentMapper.insert(comment);

        // 返回结果（评论获得5积分）
        CommentResultVO resultVO = new CommentResultVO();
        resultVO.setPoints(5);

        return resultVO;
    }
}