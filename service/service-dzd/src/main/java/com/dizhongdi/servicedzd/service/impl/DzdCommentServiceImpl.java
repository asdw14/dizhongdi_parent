package com.dizhongdi.servicedzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.model.UcenterMember;
import com.dizhongdi.servicedzd.client.UserClient;
import com.dizhongdi.servicedzd.entity.DzdArticle;
import com.dizhongdi.servicedzd.entity.DzdComment;
import com.dizhongdi.servicedzd.entity.vo.comment.CommentInfoVo;
import com.dizhongdi.servicedzd.entity.vo.comment.PushCommentVo;
import com.dizhongdi.servicedzd.mapper.DzdCommentMapper;
import com.dizhongdi.servicedzd.service.CommentStarService;
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

    @Autowired
    CommentStarService commentStarService;

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
                        and(wrapper -> wrapper.eq("parent_id", id)).orderByDesc("gmt_create"));

        List<DzdComment> comments = selectPage.getRecords();

        //获取所有用户
        List<UcenterMember> allMember = userClient.getAllMember();

        comments.stream().map(comment ->{
            CommentInfoVo commentInfo = new CommentInfoVo();
            //一级评论基本信息
            BeanUtils.copyProperties(comment,commentInfo);

            //获取评论的用户信息：头像昵称
            if (allMember!=null){
                allMember.forEach( user ->{
                    if (user.getId().equals(commentInfo.getMemberId())){
                        commentInfo.setAvatar(user.getAvatar()).setNickname(user.getNickname());

                    }
                } );
            }
//
//            AdminGetUserVo userInfo = userClient.getAllInfoId(commentInfo.getMemberId());
//            if (userInfo!=null)
//                commentInfo.setAvatar(userInfo.getAvatar()).setNickname(userInfo.getNickname());

            //获取子评论数量
            Integer commentCount = baseMapper.selectCount(new QueryWrapper<DzdComment>().eq("parent_id", comment.getId()));
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
        //获取所有用户
        List<UcenterMember> allMember = userClient.getAllMember();

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
//            AdminGetUserVo userInfo = userClient.getAllInfoId(children.getMemberId());
            if (allMember!=null){
                allMember.forEach( user ->{
                    if (user.getId().equals(commentInfo.getMemberId())){
                        childrenInfo.setAvatar(user.getAvatar()).setNickname(user.getNickname());
                    }
                } );
            }

            //获取被回复的那个用户昵称
            String byMemberId = children.getByMemberId();

            //设置对谁回复的被回复人昵称
            if (!StringUtils.isEmpty(byMemberId)){
                if (allMember!=null){
                    allMember.forEach( user ->{
                        if (user.getId().equals(commentInfo.getMemberId())){
                            childrenInfo.setByNickname(user.getNickname());
                        }
                    } );
                }

//                AdminGetUserVo by = userClient.getAllInfoId(byMemberId);
//                if (by!=null)
//                    childrenInfo.setByNickname(by.getNickname());
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
        String memberId = commentVo.getMemberId();

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

        //判断写评论的用户id是否为空
        if (StringUtils.isEmpty(memberId)){
            return false;
        }
        comment.setMemberId(memberId);

        //判断上一级评论id是否为空
        if (StringUtils.isEmpty(parentId)){
            comment.setParentId(articleId);
        }
        comment.setParentId(parentId);

        //判断对哪个用户评论是否为空
        if (!StringUtils.isEmpty(byMemberId)){
            comment.setByMemberId(byMemberId);
        }

        //添加评论
        return this.save(comment);
    }

    //根据帖子id获取评论
    @Override
    public List<CommentInfoVo> getCommentByArticleId(String id) {
        return this.getCommentInfo(id,1L,20L);
    }


    //对帖子点赞，一用户一次
    @Override
    public boolean commentStar(String commentId, String memberId) {
        boolean flag = commentStarService.isStarByCommentAndMemberId(commentId, memberId);
        //没有点赞记录
        if (flag){
            //异步执行添加点赞记录，因为此数据不重要
//            threadPool.execute(() ->{
            DzdComment comment = this.getById(commentId);
            if (comment!=null){
                //加点赞记录
                commentStarService.addStarLog(commentId,memberId);
                //对帖子表加 1点赞量
                comment.setPraiseCount(comment.getPraiseCount()+1);
                this.updateById(comment);
            }
//            });
            return true;
        }
        return false;
    }

    //撤销对帖子点赞
    public boolean rollbackStar(String commentId, String memberId){
        boolean flag = commentStarService.isStarByCommentAndMemberId(commentId, memberId);
        if (!flag){
            //异步执行添加点赞记录，因为此数据不重要
//            threadPool.execute(() -> {
            DzdComment comment = baseMapper.selectById(commentId);
            //删除点赞记录
            commentStarService.deleteStarLog(commentId, memberId);
            //减少点赞数量
            this.updateById(comment.setPraiseCount(comment.getPraiseCount() - 1));
//            });
            return true;
        }
        return true;
    }


    //删除评论
    @Override
    public boolean deleteComment(String id) {
        return this.removeById(id);
    }

    //根据评论父id和当前评论条数，每次增加10条获取子评论
    @Override
    public List<CommentInfoVo> getCommentChildrenByParentId(String parentId, Integer count) {
        //返回的数据集合
        ArrayList<CommentInfoVo> comments = new ArrayList<>();

        //last拼接根据父id获取前n条子评论，有SQL注入风险，图方便用的
        QueryWrapper<DzdComment> wrapper = new QueryWrapper<DzdComment>().eq("parent_id", parentId).last("limit " + count);
        List<DzdComment> dzdComments = baseMapper.selectList(wrapper);
        dzdComments.stream().map(dzdComment -> {
            CommentInfoVo commentInfo = new CommentInfoVo();
            //基本评论信息塞入返回
            BeanUtils.copyProperties(dzdComment,commentInfo);
            //获取评论人信息
            AdminGetUserVo userInfo = userClient.getAllInfoId(dzdComment.getMemberId());
            if (userInfo!=null)
                commentInfo.setAvatar(userInfo.getAvatar()).setNickname(userInfo.getNickname());

            String byMemberId = dzdComment.getByMemberId();
            //设置对谁回复的被回复人昵称
            if (!StringUtils.isEmpty(byMemberId)){
                AdminGetUserVo by = userClient.getAllInfoId(byMemberId);
                commentInfo.setByNickname(by.getNickname());
            }
            return commentInfo;
        }).forEach(commentInfo -> comments.add(commentInfo));
        return comments;
    }

}
