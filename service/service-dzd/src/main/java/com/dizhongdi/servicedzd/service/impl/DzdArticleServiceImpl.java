package com.dizhongdi.servicedzd.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.DzdArticleDescription;
import com.dizhongdi.servicedzd.entity.vo.article.AticleQuery;
import com.dizhongdi.servicedzd.entity.vo.article.CreateArticleVo;
import com.dizhongdi.servicedzd.entity.vo.article.GetrAticleVo;
import com.dizhongdi.servicedzd.mapper.DzdArticleMapper;
import com.dizhongdi.servicedzd.service.DzdArticleDescriptionService;
import com.dizhongdi.servicedzd.service.DzdArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-09
 */
@Service
public class DzdArticleServiceImpl extends ServiceImpl<DzdArticleMapper, DzdArticle> implements DzdArticleService {

    //文章正文
    @Autowired
    DzdArticleDescriptionService descriptionService;

    //发布文章
    @Override
    public boolean posting(CreateArticleVo articleVo) {
        DzdArticle article = new DzdArticle();
        BeanUtils.copyProperties(articleVo,article);
        return baseMapper.insert(article) <= 0 ?  false :
                descriptionService.save(new DzdArticleDescription().
                        setDescription(articleVo.getDescription()).setId(article.getId()));
    }

    //根据id获得帖子
    @Override
    public GetrAticleVo queryById(String id) {
        GetrAticleVo getrAticleVo = new GetrAticleVo();
        DzdArticle article = baseMapper.selectById(id);
        BeanUtils.copyProperties(article,getrAticleVo);
        //获取文章内容
        getrAticleVo.setDescription(descriptionService.getById(id).getDescription());
        return getrAticleVo;
    }

    //根据id删除帖子
    @Override
    public boolean deleteByid(String id) {
        boolean falg1 = this.removeById(id);
        boolean falg2 = descriptionService.removeById(id);
        return falg1 && falg2;
    }

    @Override
    public Map<String, Object> pageList(Page<DzdArticle> pageParam, AticleQuery query) {
        QueryWrapper<DzdArticle> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("gmt_modified");
        if (query!=null){

        }
        IPage<DzdArticle> selectPage = baseMapper.selectPage(pageParam, wrapper);
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        List<DzdArticle> records = selectPage.getRecords();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public boolean updateArticle(CreateArticleVo articleVo) {
        DzdArticle article = new DzdArticle();
        BeanUtils.copyProperties(articleVo,article);
        //更新帖子
        boolean flag1 = this.updateById(article);
        //更新帖子正文
        boolean flag2 = descriptionService.updateById(
                new DzdArticleDescription().setId(article.getId()).
                        setDescription(articleVo.getDescription()));
        return flag1 && flag2;
    }
}
