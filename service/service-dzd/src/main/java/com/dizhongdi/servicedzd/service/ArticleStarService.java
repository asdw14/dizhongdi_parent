package com.dizhongdi.servicedzd.service;

import com.dizhongdi.servicedzd.entity.ArticleStar;
import com.baomidou.mybatisplus.extension.service.IService;

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

    //添加用户点赞记录
    boolean addStarLog(String articleId, String memberId);

    //撤销用户点赞记录
    boolean deleteStarLog(String articleId, String memberId);
}
