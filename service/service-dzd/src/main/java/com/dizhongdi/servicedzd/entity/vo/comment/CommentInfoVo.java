package com.dizhongdi.servicedzd.entity.vo.comment;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="评论的所有信息", description="包括用户头像昵称回复人点赞")
public class CommentInfoVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "父评论id")
    private String parentId;

    @ApiModelProperty(value = "帖子id")
    private String articleId;

    @ApiModelProperty(value = "回复哪个用户id")
    private String byMemberId;

    @ApiModelProperty(value = "回复哪个用户名的")
    private String byNickname;

    @ApiModelProperty(value = "子评论")
    List<CommentInfoVo> children;

    @ApiModelProperty(value = "子评论数量")
    private Integer commentCount;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "用户名")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "点赞数量")
    private Long praiseCount;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;


}
