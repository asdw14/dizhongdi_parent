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
}
