package com.dizhongdi.servicedzd.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="文章", description="帖子")
public class DzdArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "帖子ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String memberId;

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

    @ApiModelProperty(value = "乐观锁")
    private Long version;

    @ApiModelProperty(value = "帖子状态 Draft未发布  Normal已发布")
    private String status;

    @ApiModelProperty(value = "封禁状态 0未封  1已封")
    private Integer isLock;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
