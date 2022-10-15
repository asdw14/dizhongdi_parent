package com.dizhongdi.servicedzd.service;

import com.dizhongdi.servicedzd.entity.DzdComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.servicedzd.entity.vo.comment.CommentInfoVo;
import com.dizhongdi.servicedzd.entity.vo.comment.PushCommentVo;

import java.util.List;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-24
 */
public interface DzdCommentService extends IService<DzdComment> {

    //获取评论总数
    Integer getCount(String id);

    //分页获取评论所有信息，包括前两条子评论
    List<CommentInfoVo> getCommentInfo(String id, Long current, Long size);

    //发布评论
    boolean commentPush(PushCommentVo comment);

    //根据帖子id获取评论
    List<CommentInfoVo> getCommentByArticleId(String id);


    //对帖子点赞，一用户一次
    boolean commentStar(String commentId, String memberId);

    //撤销对帖子点赞
    boolean rollbackStar(String commentId, String memberId);

    //删除评论
    boolean deleteComment(String id);

    //根据评论父id和当前评论条数，每次增加10条获取子评论
    List<CommentInfoVo> getCommentChildrenByParentId(String parentId, Integer count);

    //根据父评论id删除所有子评论
    void removeByParentId(String id);

    //根据帖子id获取所有评论
    List<CommentInfoVo> getAllByArticle(String articleId);
}

