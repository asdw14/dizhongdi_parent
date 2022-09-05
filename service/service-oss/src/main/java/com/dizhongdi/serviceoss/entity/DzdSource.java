package com.dizhongdi.serviceoss.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DzdSource对象", description="")
public class DzdSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "上传用户的id")
    private String memberId;

    @ApiModelProperty(value = "父层级id")
    private String parentId;

    @ApiModelProperty(value = "简短的资源描述")
    private String sourceName;

    @ApiModelProperty(value = "文件大小：MB")
    private Double fileSize;

    @ApiModelProperty(value = "md5值")
    private String md5;

    @ApiModelProperty(value = "是否为文件夹")
    private Integer isDirectory;

    @ApiModelProperty(value = "OSS云端保存资源URL")
    private String sourceOssUrl;

    @ApiModelProperty(value = "原始文件名称")
    private String originalName;

    @ApiModelProperty(value = "资源价格，设置为0则可免费下载")
    private Integer price;

    @ApiModelProperty(value = "销售数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long downCount;

    @ApiModelProperty(value = "资源状态 ：0私有，1公开")
    private Integer isPublic;

    @ApiModelProperty(value = "是否封禁 ：0未封，1封禁")
    private Integer isBan;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
