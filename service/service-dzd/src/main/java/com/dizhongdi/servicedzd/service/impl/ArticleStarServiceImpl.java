package com.dizhongdi.servicedzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.servicedzd.entity.ArticleStar;
import com.dizhongdi.servicedzd.mapper.ArticleStarMapper;
import com.dizhongdi.servicedzd.service.ArticleStarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-27
 */
@Service
public class ArticleStarServiceImpl extends ServiceImpl<ArticleStarMapper, ArticleStar> implements ArticleStarService {

    //根据帖子id和用户id查询是否点赞过
    @Override
    public boolean isStarByArticleAndMemberId(String articleId, String memberId) {
        QueryWrapper<ArticleStar> queryWrapper = new QueryWrapper<ArticleStar>().eq("article_id", articleId)
                .and(wrapper -> wrapper.eq("member_id", memberId));
        return this.count(queryWrapper) > 0 ? false : true;

    }

    //添加用户点赞记录
    @Override
    public boolean addStarLog(String articleId, String memberId) {
        ArticleStar articleStar = new ArticleStar(articleId, memberId);
        return this.save(articleStar);
    }

    //撤销用户点赞记录
    @Override
    public boolean deleteStarLog(String articleId, String memberId) {
        QueryWrapper<ArticleStar> queryWrapper = new QueryWrapper<ArticleStar>().eq("article_id", articleId)
                .and(wrapper -> wrapper.eq("member_id", memberId));

        //删除点赞记录
        List<ArticleStar> articleStars = baseMapper.selectList(queryWrapper);
        articleStars.stream().map(articleStar -> baseMapper.deleteById(articleStar.getId())).close();
        return true;
    }
}
