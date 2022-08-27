package com.dizhongdi.servicedzd.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.model.EsArticleVo;
import com.dizhongdi.rabbit.config.MqConst;
import com.dizhongdi.rabbit.service.RabbitService;
import com.dizhongdi.servicedzd.client.UserClient;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.DzdArticleDescription;
import com.dizhongdi.servicedzd.entity.DzdComment;
import com.dizhongdi.servicedzd.entity.vo.article.*;
import com.dizhongdi.servicedzd.entity.vo.comment.CommentInfoVo;
import com.dizhongdi.servicedzd.mapper.DzdArticleMapper;
import com.dizhongdi.servicedzd.service.DzdArticleDescriptionService;
import com.dizhongdi.servicedzd.service.DzdArticleService;
import com.dizhongdi.servicedzd.service.DzdCommentService;
import com.dizhongdi.servicedzd.utils.MyCachedThread;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

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

    //评论
    @Autowired
    DzdCommentService commentService;

    @Autowired
    RabbitService rabbitService;

    //feign远程调用user服务
    @Autowired
    UserClient userClient;

    //线程池
    ExecutorService threadPool = MyCachedThread.getThreadPool();

    //发布文章
    @Override
    public boolean posting(CreateArticleVo articleVo) {
        DzdArticle article = new DzdArticle();
        BeanUtils.copyProperties(articleVo,article);

        if (baseMapper.insert(article) > 0) {
            //将正文添加到正文表
            descriptionService.save(new DzdArticleDescription().
                    setDescription(articleVo.getDescription()).setId(article.getId()));

            //如果是发布才加入rabbit消息队列让es保存
            if ("Normal".equals(articleVo.getStatus())) {
                //通过多线程执行
                threadPool.execute(()->{
                    //创建给ES发送的vo
                    EsArticleVo esArticleVo = new EsArticleVo().setId(article.getId());
                    BeanUtils.copyProperties(articleVo,esArticleVo);
                    String jsonArticle = JSONObject.toJSONString(esArticleVo);
                    rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ARTICLE, MqConst.ROUTING_ARTICLE_SORU, jsonArticle);
            });

            }
            return true;
        }
        return false;

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
        //通过多线程执行
        threadPool.execute(()-> {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ARTICLE, MqConst.ROUTING_ARTICLE_DELETE, id);
        });
        return falg1 && falg2;
    }

    @Override
    public Map<String, Object> pageList(Page<DzdArticle> pageParam, AticleQueryVo query) {
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
        //创建给ES发送的vo
        EsArticleVo esArticleVo = new EsArticleVo().setId(article.getId());
        BeanUtils.copyProperties(articleVo,esArticleVo);
        String jsonArticle = JSONObject.toJSONString(esArticleVo);
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ARTICLE, MqConst.ROUTING_ARTICLE_SORU, jsonArticle);

        return flag1 && flag2;
    }

    //分页获取管理员帖子
    @Override
    public IPage<DzdArticle> pageQuery(Page<DzdArticle> articlePage, AticleQueryVo articleQuery) {
        QueryWrapper<DzdArticle> wrapper = new QueryWrapper<>();

        //按最新发布排序
        wrapper.orderByDesc("gmt_modified");

        //获取管理员发布的
        wrapper.eq("member_id","1");

        System.out.println(articleQuery.getTitle());
        if (!StringUtils.isEmpty(articleQuery.getTitle())){
            wrapper.like("title",articleQuery.getTitle());
        }
        System.out.println(articleQuery.getBegin());

        //是否有起始时间和终止时间同时存在
        if (!(StringUtils.isEmpty(articleQuery.getBegin()) && StringUtils.isEmpty(articleQuery.getEnd()))){
            //都有则做发布时间段
            wrapper.between("gmt_modified",articleQuery.getBegin(),articleQuery.getEnd());

            //小于终止日期
        }else if (!StringUtils.isEmpty(articleQuery.getEnd())){
            wrapper.le("gmt_modified",articleQuery.getEnd());

            //大于开始日期
        }else if (!StringUtils.isEmpty(articleQuery.getBegin())){
            wrapper.ge("gmt_modified",articleQuery.getBegin());
        }

        //是否发布
        if (!StringUtils.isEmpty(articleQuery.getStatus())){
            wrapper.eq("status",articleQuery.getStatus());

        }

        //文章类别
        if (!StringUtils.isEmpty(articleQuery.getSubjectId())){
            wrapper.eq("subject_id",articleQuery.getSubjectId());
        }else if (!StringUtils.isEmpty(articleQuery.getSubjectParentId())){
            wrapper.eq("subject_parent_id",articleQuery.getSubjectParentId());

        }

        return baseMapper.selectPage(articlePage,wrapper);

    }

    //修改发布状态
    @Override
    public boolean updateStatus(String id) {
        DzdArticle article = baseMapper.selectById(id);
        return this.updateById(
                "Draft".equals(article.getStatus()) ?
                        article.setStatus("Normal") :
                        article.setStatus("Draft"));
    }

    //更新帖子
    @Override
    public boolean updateInfo(String id, CreateArticleVo articleVo) {
        DzdArticle article = new DzdArticle();
        BeanUtils.copyProperties(articleVo,article);
        article.setId(id);
        DzdArticleDescription articleDescription = new DzdArticleDescription();

        return this.updateById(article) && descriptionService.updateById(articleDescription.setId(id).setDescription(articleVo.getDescription()));
    }

    //分页获取用户帖子
    @Override
    public List<GetrUserAticleVo> pageUserQuery(Page<DzdArticle> articlePage, AticleQueryVo articleQuery) {
        QueryWrapper<DzdArticle> wrapper = new QueryWrapper<>();

        List<GetrUserAticleVo> userAticleVos = new ArrayList<>();

        //按最新发布排序
        wrapper.orderByDesc("gmt_modified");
        System.out.println(articleQuery.getTitle());
        if (!StringUtils.isEmpty(articleQuery.getTitle())){
            wrapper.like("title",articleQuery.getTitle());
        }
        System.out.println(articleQuery.getBegin());

        wrapper.ne("member_id","1");

        //是否有起始时间和终止时间同时存在
        if (!(StringUtils.isEmpty(articleQuery.getBegin()) && StringUtils.isEmpty(articleQuery.getEnd()))){
            //都有则做发布时间段
            wrapper.between("gmt_modified",articleQuery.getBegin(),articleQuery.getEnd());

            //小于终止日期
        }else if (!StringUtils.isEmpty(articleQuery.getEnd())){
            wrapper.le("gmt_modified",articleQuery.getEnd());

            //大于开始日期
        }else if (!StringUtils.isEmpty(articleQuery.getBegin())){
            wrapper.ge("gmt_modified",articleQuery.getBegin());
        }

        //是否发布
        if (!StringUtils.isEmpty(articleQuery.getStatus())){
            wrapper.eq("status",articleQuery.getStatus());

        }

        //是否封禁
        if (!StringUtils.isEmpty(articleQuery.getIsLock())){
            wrapper.eq("is_lock",articleQuery.getIsLock());

        }

        //根据用户id或文章id搜索
        if (!StringUtils.isEmpty(articleQuery.getId())){
            wrapper.eq("id",articleQuery.getId()).or().eq("member_id",articleQuery.getId());

        }

        //文章类别
        if (!StringUtils.isEmpty(articleQuery.getSubjectId())){
            wrapper.eq("subject_id",articleQuery.getSubjectId());
        }else if (!StringUtils.isEmpty(articleQuery.getSubjectParentId())){
            wrapper.eq("subject_parent_id",articleQuery.getSubjectParentId());

        }

        //远程调用获取帖子发布用户头像和昵称
        IPage<DzdArticle> selectPage = baseMapper.selectPage(articlePage, wrapper);
        selectPage.getRecords().stream().map(article -> {
            GetrUserAticleVo userAticleVo = new GetrUserAticleVo();
            AdminGetUserVo userinfo = userClient.getAllInfoId(article.getMemberId());
            BeanUtils.copyProperties(article,userAticleVo);
            if (userinfo!=null)
                userAticleVo.setAvatar(userinfo.getAvatar()).setNickname(userinfo.getNickname());
            return userAticleVo;
        }).forEach( userAticleVo -> userAticleVos.add(userAticleVo));

        return userAticleVos;
    }

    //修改帖子封禁状态
    @Override
    public boolean updateLock(String id) {
        DzdArticle article = baseMapper.selectById(id);
        //0改为1，1改为0
        return this.updateById(
                article.setIsLock(article.getIsLock()==0 ? 1 : 0));
    }

    //前台使用的分页获取所有帖子
    @Override
    public List<GetAllAticleVo> pageAllArticleQuery(Page<DzdArticle> articlePage, AticleQueryVo articleQuery) {
        QueryWrapper<DzdArticle> wrapper = new QueryWrapper<>();

        List<GetAllAticleVo> articles = new ArrayList<>();

        //按最新发布排序
        wrapper.orderByDesc("gmt_modified");
        System.out.println(articleQuery.getTitle());
        if (!StringUtils.isEmpty(articleQuery.getTitle())){
            wrapper.like("title",articleQuery.getTitle());
        }
        System.out.println(articleQuery.getBegin());


        //是否有起始时间和终止时间同时存在
        if (!(StringUtils.isEmpty(articleQuery.getBegin()) && StringUtils.isEmpty(articleQuery.getEnd()))){
            //都有则做发布时间段
            wrapper.between("gmt_modified",articleQuery.getBegin(),articleQuery.getEnd());

            //小于终止日期
        }else if (!StringUtils.isEmpty(articleQuery.getEnd())){
            wrapper.le("gmt_modified",articleQuery.getEnd());

            //大于开始日期
        }else if (!StringUtils.isEmpty(articleQuery.getBegin())){
            wrapper.ge("gmt_modified",articleQuery.getBegin());
        }

        //是否发布
        if (!StringUtils.isEmpty(articleQuery.getStatus())){
            wrapper.eq("status",articleQuery.getStatus());

        }

        //是否封禁
        if (!StringUtils.isEmpty(articleQuery.getIsLock())){
            wrapper.eq("is_lock",articleQuery.getIsLock());

        }

        //根据用户id或文章id搜索
        if (!StringUtils.isEmpty(articleQuery.getId())){
            wrapper.eq("id",articleQuery.getId()).or().eq("member_id",articleQuery.getId());

        }

        //文章类别
        if (!StringUtils.isEmpty(articleQuery.getSubjectId())){
            wrapper.eq("subject_id",articleQuery.getSubjectId());
        }else if (!StringUtils.isEmpty(articleQuery.getSubjectParentId())){
            wrapper.eq("subject_parent_id",articleQuery.getSubjectParentId());

        }

        //远程调用获取帖子发布用户头像和昵称
        IPage<DzdArticle> selectPage = baseMapper.selectPage(articlePage, wrapper);
        selectPage.getRecords().stream().map(article -> {
            GetAllAticleVo AticleVo = new GetAllAticleVo();
            AdminGetUserVo userinfo = userClient.getAllInfoId(article.getMemberId());
            BeanUtils.copyProperties(article,AticleVo);
            AticleVo.setAvatar(userinfo.getAvatar()).setNickname(userinfo.getNickname());
            //获取评论总数
            int commentCount = commentService.count(new QueryWrapper<DzdComment>().eq("article_id", article.getId()));
            AticleVo.setCommentCount(commentCount);
            return AticleVo;
        }).forEach( userAticleVo -> articles.add(userAticleVo));

        return articles;
    }

    //获取文章所有信息
    @Override
    public ArticleInfoAllVo getAticleInfo(String id) {
        ArticleInfoAllVo articleInfo = new ArticleInfoAllVo();
        //文章大多信息
        DzdArticle article = this.getById(id);
        BeanUtils.copyProperties(article,articleInfo);
        //文章正文
        DzdArticleDescription description = descriptionService.getById(id);
        articleInfo.setDescription(description.getDescription());
        //评论总数
        Integer commentCount = commentService.getCount(id);
        articleInfo.setCommentCount(commentCount);

        //写文章的用户头像和昵称
        AdminGetUserVo userInfo = userClient.getAllInfoId(article.getMemberId());
        articleInfo.setAvatar(userInfo.getAvatar()).setNickname(userInfo.getNickname());

        //默认获取前10条评论及其前两条子评论
        List<CommentInfoVo> commentInfos = commentService.getCommentInfo(id,1L,10L);
        articleInfo.setComments(commentInfos);
        return articleInfo;
    }


}
