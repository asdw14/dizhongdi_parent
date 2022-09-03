package com.dizhongdi.servicedzd.mapper;

import com.dizhongdi.servicedzd.entity.ArticleStar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-27
 */
public interface ArticleStarMapper extends BaseMapper<ArticleStar> {

    //根据帖子id和用户id获取点赞记录，包括逻辑删除的数据，用以恢复
    List<ArticleStar> getStarByArticleAndMemberId(@Param("articleId") String articleId,@Param("memberId") String memberId);
}
