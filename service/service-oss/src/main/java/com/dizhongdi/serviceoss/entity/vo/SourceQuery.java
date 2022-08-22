package com.dizhongdi.serviceoss.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName:SourceQuery
 * Package:com.dizhongdi.serviceoss.entity.vo
 * Description:
 *
 * @Date: 2022/8/21 22:41
 * @Author:dizhongdi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="资源查询条件对象", description="资源查询条件对象")
public class SourceQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "上传用户的id")
    private String userId;

    @ApiModelProperty(value = "简短的资源描述")
    private String sourceName;

    @ApiModelProperty(value = "最小文件大小：MB")
    private BigDecimal minfileSize;
    @ApiModelProperty(value = "最大文件大小：MB")
    private BigDecimal maxfileSize;


    @ApiModelProperty(value = "原始文件名称")
    private String originalName;

    @ApiModelProperty(value = "资源价格，设置为0则可免费下载")
    private BigDecimal price;

    @ApiModelProperty(value = "销售数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long downCount;

    @ApiModelProperty(value = "资源状态 ：0私有，1公开")
    private String isPublic;

    @ApiModelProperty(value = "是否封禁 ：0未封，1封禁")
    private Boolean isBan;

    @ApiModelProperty(value = "是否收费 ：0免费，1收费")
    private Boolean isCharge;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;


}