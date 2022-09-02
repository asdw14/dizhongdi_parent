package com.dizhongdi.servicedzd.entity.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * ClassName:CreateArticleVo
 * Package:com.dizhongdi.servicedzd.entity.vo
 * Description:
 *
 * @Date: 2022/8/9 17:33
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="帖子", description="发帖vo")
public class CreateArticleVo {
    @ApiModelProperty(value = "用户ID")
    private String memberId;

    @ApiModelProperty(value = "帖子分类父ID")
    private String subjectParentId;

    @ApiModelProperty(value = "帖子分类ID")
    private String subjectId;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "文章内容")
    private String description;

    @ApiModelProperty(value = "md内容")
    private String markdown;


    @ApiModelProperty(value = "封禁状态 0未封  1已封")
    private Integer isLock;

    @ApiModelProperty(value = "帖子状态 Draft未发布  Normal已发布")
    private String status;

    @ApiModelProperty(value = "资源价格，设置为0则可免费观看")
    private BigDecimal price;

}
