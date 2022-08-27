package com.dizhongdi.servicedzd.controller.api;

import com.dizhongdi.result.R;
import com.dizhongdi.servicedzd.entity.vo.comment.CommentInfoVo;
import com.dizhongdi.servicedzd.entity.vo.comment.PushCommentVo;
import com.dizhongdi.servicedzd.service.CommentStarService;
import com.dizhongdi.servicedzd.service.DzdCommentService;
import com.dizhongdi.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ClassName:DzdCommentController
 * Package:com.dizhongdi.servicedzd.controller.api
 * Description:
 *
 * @Date: 2022/8/26 22:15
 * @Author:dizhongdi
 */
@RestController
@RequestMapping("/api/dzd/comment")
@Api(description="帖子评论前台")
public class DzdCommentController {

    @Autowired
    DzdCommentService commentService;

    @Autowired
    CommentStarService commentStarService;

    @PostMapping("/commentPush")
    @ApiOperation(value = "发布评论")
    public R commentPush(@ApiParam(name = "page", value = "当前页码", required = true)
                             @RequestBody  PushCommentVo commentVo, HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberId);
        System.out.println(commentVo.toString());
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行评论！");
        }

        commentVo.setMemberId(memberId);

        //调用评论
        if (commentService.commentPush(commentVo)){
            return R.ok().message("回复成功！");
        }
        return R.error().message("评论失败");
    }

    //根据帖子id获取评论
    @GetMapping("getCommentByArticleId/{id}")
    @ApiOperation(value = "根据帖子id获取评论")
    public R getCommentByArticleId(@PathVariable String id) {
        List<CommentInfoVo> comments = commentService.getCommentByArticleId(id);
        return R.ok().data("items",comments);
    }



    @GetMapping("commentStar/{commentId}")
    @ApiOperation(value = "点赞")
    public R commentStar(@PathVariable String commentId , HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberId);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行点赞！");
        }

        //调用方法点赞
        if (commentService.commentStar(commentId,memberId)){
            return R.ok().message("点赞成功");
        }
        return R.error().message("您已点赞过！");
    }

    @GetMapping("rollbackStar/{commentId}")
    @ApiOperation(value = "撤回点赞")
    public R rollbackStar( @PathVariable String commentId , HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        System.out.println(memberId);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("请先登录后在进行点赞！");
        }

        //调用方法点赞
        if (commentService.rollbackStar(commentId,memberId)){
            return R.ok();
        }
        return R.error();
    }
}
