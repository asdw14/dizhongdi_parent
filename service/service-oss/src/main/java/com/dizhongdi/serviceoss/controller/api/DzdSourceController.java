package com.dizhongdi.serviceoss.controller.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.model.AdminGetUserVo;
import com.dizhongdi.result.R;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.service.DzdSourceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/oss/source")
public class DzdSourceController {

    @Autowired
    DzdSourceService sourceService;

    @ApiOperation(value = "分页获取公开资源")
    @PostMapping("getPublicPageList/{page}/{limit}")
    public R getPublicPageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "UserQuery", value = "查询对象", required = false)
            @RequestBody SourceQuery userQuery){
        Page<DzdSource> sourcePage = new Page<>(page,limit);
        List<SourceInfoVo> queryList = sourceService.getPublicPageList(sourcePage,userQuery);
        return R.ok().data("items" , queryList).data("total",sourcePage.getTotal());
    }
}

