package com.dizhongdi.servicedzd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.vo.article.AticleQuery;
import com.dizhongdi.servicedzd.entity.vo.article.CreateArticleVo;
import com.dizhongdi.servicedzd.entity.vo.article.GetrAticleVo;

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

    Map<String, Object> pageList(Page<DzdArticle> pageParam, AticleQuery query);

    //修改帖子
    boolean updateArticle(CreateArticleVo articleVo);

    //分页条件查询
    IPage<DzdArticle> pageQuery(Page<DzdArticle> articlePage, AticleQuery articleQuery);

    //修改发布状态
    boolean updateStatus(String id);
}
