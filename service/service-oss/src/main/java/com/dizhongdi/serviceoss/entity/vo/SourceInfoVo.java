package com.dizhongdi.serviceoss.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:SourceInfoVo
 * Package:com.dizhongdi.serviceoss.entity.vo
 * Description:
 *
 * @Date: 2022/8/21 22:43
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="资源查询条件对象", description="资源查询条件对象")
public class SourceInfoVo {
    @ApiModelProperty(value = "资源id")
    private String id;

    @ApiModelProperty(value = "上传用户的id")
    private String memberId;

    @ApiModelProperty(value = "用户名")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

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

    @ApiModelProperty(value = "下载数量")
    private Long downCount;

    @ApiModelProperty(value = "资源状态 ：0私有，1公开")
    private String isPublic;

    @ApiModelProperty(value = "是否封禁 ：0未封，1封禁")
    private Integer isBan;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;
}
