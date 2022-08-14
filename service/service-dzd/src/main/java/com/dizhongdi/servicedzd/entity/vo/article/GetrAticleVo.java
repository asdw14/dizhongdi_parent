package com.dizhongdi.servicedzd.entity.vo.article;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:GetrAticleVo
 * Package:com.dizhongdi.servicedzd.entity.vo.article
 * Description:
 *
 * @Date: 2022/8/9 22:56
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="帖子", description="单条帖子信息")
public class GetrAticleVo {

    @ApiModelProperty(value = "帖子ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String memberId;

    @ApiModelProperty(value = "文章内容")
    private String description;

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
