package com.dizhongdi.servicedzd.entity.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ClassName:AticleQuery
 * Package:com.dizhongdi.servicedzd.entity.vo.article
 * Description:
 *
 * @Date: 2022/8/9 23:11
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="帖子查询", description="帖子查询条件vo")
public class AticleQuery {

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

    @ApiModelProperty(value = "封禁状态 0未封  1已封")
    private Integer isLock;

    @ApiModelProperty(value = "帖子状态 Draft未发布  Normal已发布")
    private String status;

    @ApiModelProperty(value = "起始时间")
    private String begin;

    @ApiModelProperty(value = "结束时间")
    private String end;


}
