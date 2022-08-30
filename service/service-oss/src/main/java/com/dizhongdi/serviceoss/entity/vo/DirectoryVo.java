package com.dizhongdi.serviceoss.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ClassName:DirectoryVo
 * Package:com.dizhongdi.serviceoss.entity.vo
 * Description:
 *
 * @Date: 2022/8/30 22:46
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="新建文件夹对象", description="新建文件夹对象")
public class DirectoryVo {
    @ApiModelProperty(value = "父层级id")
    private String parentId;

    @ApiModelProperty(value = "简短的资源描述")
    private String sourceName;

    @ApiModelProperty(value = "是否为文件夹")
    private Integer isDirectory;

    @ApiModelProperty(value = "资源状态 ：0私有，1公开")
    private String isPublic;

    @ApiModelProperty(value = "上传用户的id")
    private String userId;
}
