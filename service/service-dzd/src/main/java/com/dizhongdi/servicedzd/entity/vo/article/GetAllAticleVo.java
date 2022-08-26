package com.dizhongdi.servicedzd.entity.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:GetAllAticleVo
 * Package:com.dizhongdi.servicedzd.entity.vo.article
 * Description:
 *
 * @Date: 2022/8/24 20:38
 * @Author:dizhongdi
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="前台获取帖子列表时使用的vo", description="包括用户，评论点赞浏览数量")
public class GetAllAticleVo {

    @ApiModelProperty(value = "帖子ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String memberId;

    @ApiModelProperty(value = "用户名")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "文章内容")
    private String description;

    @ApiModelProperty(value = "文章前几句大概")
    private String summary;

    @ApiModelProperty(value = "帖子分类父ID")
    private String subjectParentId;


    @ApiModelProperty(value = "帖子分类ID")
    private String subjectId;

    @ApiModelProperty(value = "帖子标题")
    private String title;

    @ApiModelProperty(value = "资源价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "点赞数量")
    private Long praiseCount;

    @ApiModelProperty(value = "购买数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;

    @ApiModelProperty(value = "评论数量")
    private Integer commentCount;

    @ApiModelProperty(value = "帖子状态 Draft未发布  Normal已发布")
    private String status;

    @ApiModelProperty(value = "封禁状态 0未封  1已封")
    private Integer isLock;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

}
