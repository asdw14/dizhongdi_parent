package com.dizhongdi.servicedzd.service;

import com.dizhongdi.servicedzd.entity.ArticleStar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  帖子点赞记录
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-27
 */
public interface ArticleStarService extends IService<ArticleStar> {
    //根据帖子id和用户id查询是否点赞过
    boolean isStarByArticleAndMemberId(String articleId,String memberId);

    //根据帖子id和用户id获取点赞记录，包括逻辑删除的数据，用以恢复
    ArticleStar getStarByArticleAndMemberId(String articleId, String memberId);

    //添加用户点赞记录
    boolean addStarLog(String articleId, String memberId);

    //撤销用户点赞记录
    boolean deleteStarLog(String articleId, String memberId);

    //查询用户是否点赞过该帖子
    boolean getIsStar(String id, String memberId);

    void setIsDeleted(String id, int i);
}
