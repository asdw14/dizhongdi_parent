package com.dizhongdi.servicedzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.servicedzd.entity.ArticleStar;
import com.dizhongdi.servicedzd.entity.CommentStar;
import com.dizhongdi.servicedzd.mapper.CommentStarMapper;
import com.dizhongdi.servicedzd.service.CommentStarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-27
 */
@Service
public class CommentStarServiceImpl extends ServiceImpl<CommentStarMapper, CommentStar> implements CommentStarService {

    //根据评论id和用户id查询是否点赞过
    @Override
    public boolean isStarByCommentAndMemberId(String commentId, String memberId) {
        QueryWrapper<CommentStar> queryWrapper = new QueryWrapper<CommentStar>().eq("comment_id", commentId)
                .and(wrapper -> wrapper.eq("member_id", memberId));
        return this.count(queryWrapper) > 0 ? false : true;

    }

    //添加用户点赞记录
    @Override
    public boolean addStarLog(String commentId, String memberId) {
        CommentStar commentStar = new CommentStar(commentId, memberId);
        return this.save(commentStar);
    }

    //撤销用户点赞记录
    @Override
    public boolean deleteStarLog(String commentId, String memberId) {
        QueryWrapper<CommentStar> queryWrapper = new QueryWrapper<CommentStar>().eq("comment_id", commentId)
                .and(wrapper -> wrapper.eq("member_id", memberId));

        //删除点赞记录
        baseMapper.deleteById(baseMapper.selectOne(queryWrapper).getId());
        return true;
    }
}
