package com.dizhongdi.serviceoss.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:UploadInfo
 * Package:com.dizhongdi.serviceoss.entity.vo
 * Description:
 *
 * @Date: 2022/8/29 22:09
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="上传文件信息", description="上传文件信息")
public class UploadInfo {
    @ApiModelProperty(value = "上传用户的id")
    private String userId;

    @ApiModelProperty(value = "父层级id")
    private String parentId;

    @ApiModelProperty(value = "简短的资源描述")
    private String sourceName;

    @ApiModelProperty(value = "文件大小：MB")
    private BigDecimal fileSize;

    @ApiModelProperty(value = "md5值")
    private String md5;

    @ApiModelProperty(value = "是否为文件夹")
    private Integer isDirectory;

    @ApiModelProperty(value = "原始文件名称")
    private String originalName;

    @ApiModelProperty(value = "资源价格，设置为0则可免费下载")
    private Integer price;

    @ApiModelProperty(value = "资源状态 ：0私有，1公开")
    private Integer isPublic;
}
