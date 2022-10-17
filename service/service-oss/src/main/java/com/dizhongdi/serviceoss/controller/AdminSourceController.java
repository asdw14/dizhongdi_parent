package com.dizhongdi.serviceoss.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.result.R;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.dizhongdi.serviceoss.entity.vo.OperationVo;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.service.DzdSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  资源文件上传下载
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/admin/oss/source")
@Api(description="后台资源管理")
public class AdminSourceController {

    @Autowired
    DzdSourceService sourceService;

    @ApiOperation(value = "后台分页获取公开资源")
    @PostMapping("getPublicPageList/{page}/{limit}")
    public R getPublicPageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "UserQuery", value = "查询对象", required = false)
            @RequestBody SourceQuery userQuery){
        Page<DzdSource> sourcePage = new Page<>(page,limit);
        List<SourceInfoVo> queryList = sourceService.getPublicPageList(sourcePage,userQuery,true);
        return R.ok().data("items" , queryList).data("total",sourcePage.getTotal());
    }

    @ApiOperation(value = "更新资源信息，如资源名称之类")
    @PutMapping("update/{id}")
    public R getById(@PathVariable String id,@RequestBody DzdSource source){
        boolean falg = sourceService.updateInfo(id,source);
        return R.ok();
    }

    @ApiOperation(value = "根据id获取资源")
    @GetMapping("sourceInfo/{id}")
    public R getSourceInfoById(@PathVariable String id){
        SourceInfoVo sourceInfo = sourceService.getInfoById(id);
        return R.ok().data("item",sourceInfo);
    }

    @ApiOperation(value = "根据id修改封禁状态，封禁改为未封禁，未封禁改为封禁")
    @PutMapping("banById/{id}")
    public R banById(@PathVariable String id){
        if (sourceService.updateBan(id)){
            return R.ok();
        }
        return R.error();
    }

    @ApiOperation(value = "根据id删除资源,包括oss上保存的文件")
    @DeleteMapping("removeById/{id}")
    public R deleteById(@PathVariable String id){
        if (sourceService.deleteByid(id)){
            return R.ok().message("删除成功");
        }
        return R.error().message("删除失败");
    }

    @ApiOperation(value = "根据id暗箱操作包括 下载次数 购买次数 价格")
    @PutMapping("operation")
    public R operation(@RequestBody OperationVo operation){
        if (operation != null && operation.getId() != null){
            DzdSource source = sourceService.getById(operation.getId());
            if (source != null){
                BeanUtils.copyProperties(operation,source);
                return sourceService.updateById(source) == true? R.ok().message("更改成功") : R.error().message("更改失败");
            }
            return R.error().message("资源不存在");

        }else {
            return R.error().message("资源不存在");
        }
    }
    
    
}

