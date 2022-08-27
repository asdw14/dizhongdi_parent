package com.dizhongdi.servicedzd.service;

import com.dizhongdi.servicedzd.entity.CommentStar;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-27
 */
public interface CommentStarService extends IService<CommentStar> {
    //根据帖子id和用户id查询是否点赞过
    boolean isStarByCommentAndMemberId(String commentId,String memberId);

    //添加用户点赞记录
    boolean addStarLog(String commentId, String memberId);

    //撤销用户点赞记录
    boolean deleteStarLog(String commentId, String memberId);
}
