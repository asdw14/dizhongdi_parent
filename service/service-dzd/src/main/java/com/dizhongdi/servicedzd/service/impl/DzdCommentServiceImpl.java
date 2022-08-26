package com.dizhongdi.servicedzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.servicedzd.client.UserClient;
import com.dizhongdi.servicedzd.entity.DzdComment;
import com.dizhongdi.servicedzd.entity.vo.comment.CommentInfoVo;
import com.dizhongdi.servicedzd.entity.vo.comment.PushCommentVo;
import com.dizhongdi.servicedzd.mapper.DzdCommentMapper;
import com.dizhongdi.servicedzd.service.DzdCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-24
 */
@Service
public class DzdCommentServiceImpl extends ServiceImpl<DzdCommentMapper, DzdComment> implements DzdCommentService {
    @Autowired
    UserClient userClient;

    //获取评论总数
    @Override
    public Integer getCount(String id) {
        return baseMapper.selectCount(new QueryWrapper<DzdComment>().eq("article_id",id));
    }

    //分页获取评论所有信息，包括前两条子评论，包括前两条子评论
    @Override
    public List<CommentInfoVo>  getCommentInfo(String id , Long current, Long size) {
        ArrayList<CommentInfoVo> commentInfos = new ArrayList<>();
        //一级评论基本信息
        Page<DzdComment> page = new Page<>(current,size);
        IPage<DzdComment> selectPage = baseMapper.selectPage(page,
                new QueryWrapper<DzdComment>().eq("article_id", id).
                        and(wrapper -> wrapper.eq("parent_id", id)));

        List<DzdComment> comments = selectPage.getRecords();

        comments.stream().map(comment ->{
            CommentInfoVo commentInfo = new CommentInfoVo();
            //一级评论基本信息
            BeanUtils.copyProperties(comment,commentInfo);

            //获取评论的用户信息：头像昵称
            AdminGetUserVo userInfo = userClient.getAllInfoId(commentInfo.getMemberId());
            commentInfo.setAvatar(userInfo.getAvatar()).setNickname(userInfo.getNickname());

            //获取子评论数量
            Integer commentCount = baseMapper.selectCount(new QueryWrapper<DzdComment>().eq("parent_id", comment.getParentId()));
            commentInfo.setCommentCount(commentCount);

            //获取嵌套评论列表
            List<CommentInfoVo> children = this.getChildrenList(commentInfo);
            commentInfo.setChildren(children);

            return commentInfo;
        }).forEach(commentInfo -> commentInfos.add(commentInfo));

        return commentInfos;
    }

    //获取嵌套评论列表
    public List<CommentInfoVo> getChildrenList( CommentInfoVo commentInfo){
        //子评论列表
        ArrayList<CommentInfoVo> childrenInfos = new ArrayList<>();

        //父评论id
        String parentId = commentInfo.getId();

        //获取2条子评论基本信息
        IPage<DzdComment> selectPage = baseMapper.selectPage(
                new Page<DzdComment>(1, 2),
                new QueryWrapper<DzdComment>()
                        .eq("article_id", commentInfo.getArticleId())
                        .and(wrapper -> wrapper.eq("parent_id", parentId)));
        List<DzdComment> childrens = selectPage.getRecords();
        //遍历获取其他信息
        childrens.stream().map(children ->{
            //子评论对象
            CommentInfoVo childrenInfo = new CommentInfoVo();

            BeanUtils.copyProperties(children,childrenInfo);

            //获取评论人头像昵称
            AdminGetUserVo userInfo = userClient.getAllInfoId(children.getMemberId());
            childrenInfo.setAvatar(userInfo.getAvatar()).setNickname(userInfo.getNickname());

            //获取被回复的那个用户昵称
            String byMemberId = children.getByMemberId();
            if (byMemberId!=null || !byMemberId.equals("0")) {
                AdminGetUserVo byMemberInfo = userClient.getAllInfoId(byMemberId);
                childrenInfo.setByNickname(byMemberInfo.getNickname());
            }
            return childrenInfo;
            //将数据添加进子评论集合
        }).forEach( childrenInfo ->childrenInfos.add(childrenInfo));

        return childrenInfos;

    }


    //发布评论
    @Override
    public boolean commentPush(PushCommentVo commentVo) {
        String content = commentVo.getContent();
        String parentId = commentVo.getParentId();
        String articleId = commentVo.getArticleId();
        String byMemberId = commentVo.getByMemberId();

        DzdComment comment = new DzdComment();

        //判断评论内容是否为空
        if (StringUtils.isEmpty(content)){
            return false;
        }
        comment.setContent(content);

        //判断评论文章id是否为空
        if (StringUtils.isEmpty(articleId)){
            return false;
        }
        comment.setArticleId(articleId);

        //判断上一级评论id是否为空
        if (StringUtils.isEmpty(parentId)){
            comment.setParentId(articleId);
        }
        comment.setParentId(parentId);

        //判断对哪个用户评论是否为空
        if (!StringUtils.isEmpty(byMemberId)){
            comment.setMemberId(byMemberId);
        }

        //添加评论
        return  this.save(comment);
    }


}
