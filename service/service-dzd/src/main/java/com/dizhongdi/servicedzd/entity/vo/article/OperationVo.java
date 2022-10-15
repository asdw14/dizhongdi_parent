package com.dizhongdi.servicedzd.entity.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ClassName:OperationVo
 * Package:com.dizhongdi.servicedzd.entity.vo.article
 * Description:
 *
 * @Date: 2022/10/15 18:40
 * @Author:dizhongdi
 */
@Data
@Accessors(chain = true)
@ApiModel(value="暗箱操作条件对象", description="暗箱操作条件对象")
public class OperationVo {
    @ApiModelProperty(value = "资源id")
    private String id;

    @ApiModelProperty(value = "资源价格，设置为0则可免费下载")
    private Integer price;

    @ApiModelProperty(value = "点赞数量")
    private Long praiseCount;

    @ApiModelProperty(value = "购买数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;

}
