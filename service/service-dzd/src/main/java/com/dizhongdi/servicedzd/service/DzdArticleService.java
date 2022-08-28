package com.dizhongdi.servicedzd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.vo.article.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-09
 */
public interface DzdArticleService extends IService<DzdArticle> {

    //发帖
    boolean posting(CreateArticleVo articleVo);

    //根据id获得帖子
    GetrAticleVo queryById(String id);

//    根据id删除帖子
    boolean deleteByid(String id);

    Map<String, Object> pageList(Page<DzdArticle> pageParam, AticleQueryVo query);

    //修改帖子
    boolean updateArticle(CreateArticleVo articleVo);

    //分页条件查询
    IPage<DzdArticle> pageQuery(Page<DzdArticle> articlePage, AticleQueryVo articleQuery);

    //修改发布状态
    boolean updateStatus(String id);

    //更新帖子
    boolean updateInfo(String id, CreateArticleVo articleVo);

//    分页获取用户帖子
    List<GetrUserAticleVo> pageUserQuery(Page<DzdArticle> articlePage, AticleQueryVo articleQuery);

//    修改帖子封禁状态
    boolean updateLock(String id);

    //前台使用的分页获取所有帖子
    List<GetAllAticleVo> pageAllArticleQuery(Page<DzdArticle> articlePage, AticleQueryVo articleQuery);

//    前台获取帖子所有信息
    ArticleInfoAllVo getAticleInfo(String id);

    //对帖子点赞，一用户一次
    boolean articleStar(String articleId, String memberId);

    //撤销对帖子点赞
    boolean rollbackStar(String articleId, String memberId);

    //根据用户id增加浏览次数，一个用户增加一次
    void addArticleViewCountByMemberId(String articleId, String memberId);
}
