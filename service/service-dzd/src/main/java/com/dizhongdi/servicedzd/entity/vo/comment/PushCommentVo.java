package com.dizhongdi.servicedzd.entity.vo.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ClassName:PushCommentVo
 * Package:com.dizhongdi.servicedzd.entity.vo.comment
 * Description:
 *
 * @Date: 2022/8/26 23:12
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="发送评论用的", description="发送评论用的")
public class PushCommentVo {

    @ApiModelProperty(value = "父评论id")
    private String parentId;

    @ApiModelProperty(value = "帖子id")
    private String articleId;

    @ApiModelProperty(value = "回复哪个用户id")
    private String byMemberId;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "评论内容")
    private String content;
}
