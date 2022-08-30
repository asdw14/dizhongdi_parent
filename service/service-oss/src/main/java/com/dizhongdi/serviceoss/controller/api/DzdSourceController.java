package com.dizhongdi.serviceoss.controller.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.result.R;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.dizhongdi.serviceoss.entity.vo.DirectoryVo;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.entity.vo.UploadInfo;
import com.dizhongdi.serviceoss.service.DzdSourceService;
import com.dizhongdi.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 资源文件上传下载
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/api/oss/source")
@Api(description = "前台资源api")
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
            @RequestBody SourceQuery userQuery) {
        Page<DzdSource> sourcePage = new Page<>(page, limit);
        List<SourceInfoVo> queryList = sourceService.getPublicPageList(sourcePage, userQuery, false);
        return R.ok().data("items", queryList).data("total", sourcePage.getTotal());
    }

    @ApiOperation(value = "上传资源文件")
    @PostMapping("uploadSource")
    public R upload(@ApiParam(name = "file", value = "文件", required = true) @RequestParam("file") MultipartFile file,
                    HttpServletRequest request,
                    @ApiParam(name = "sourceName", value = "用户写的文件名", required = false) @RequestParam(value = "sourceName", required = false) String sourceName,
                    @ApiParam(name = "parentId", value = "父id", required = false) @RequestParam(value = "parentId", required = false) String parentId,
                    @ApiParam(name = "price", value = "价格", required = false) @RequestParam(value = "price", required = false) Integer price,
                    @ApiParam(name = "isPublic", value = "是否公开", required = false) @RequestParam(value = "isPublic", required = false) Integer isPublic

    ) {
        //判断上传文件是否为空,为空返回报错
        boolean empty = file.isEmpty();
        if (empty) {
            return R.error().message("上传文件不能为空");
        }

        UploadInfo uploadInfo = new UploadInfo();
        //验证用户是否登录
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)){
            uploadInfo.setUserId(userId);
        }

        //用户给的名,没给就默认原始文件名
        if (!StringUtils.isEmpty(sourceName)) {
            uploadInfo.setSourceName(sourceName);
        }
        uploadInfo.setSourceName(file.getOriginalFilename());

        //价格，为空默认为0
        if (!StringUtils.isEmpty(price)) {
            uploadInfo.setPrice(price);
        }
        uploadInfo.setPrice(0);

        //是否公开，为空表示默认公开
        if (!StringUtils.isEmpty(isPublic)) {
            uploadInfo.setIsPublic(isPublic);
        }

        //上层文件夹id，默认为0
        if (!StringUtils.isEmpty(parentId)) {
            uploadInfo.setParentId(parentId);
        }
        uploadInfo.setParentId("0");
        //调用service方法上传
        boolean b = sourceService.uploadSource(file, uploadInfo);
        if (b) {
            return R.ok();
        }
        return R.error().message("上传文件失败");

    }


    @ApiOperation(value = "新建文件夹")
    @PostMapping("newDirectory")
    public R newDirectory(@ApiParam(name = "directoryVo", value = "文件夹信息", required = true)
                        @RequestBody DirectoryVo directoryVo, HttpServletRequest request){

        //验证用户是否登录
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)){
            directoryVo.setUserId(userId);
        }

        //文件夹名不能为空
        if (StringUtils.isEmpty(directoryVo.getSourceName())){
            return R.error().message("文件夹名不能为空！");
        }

        //父层级
        String parentId = directoryVo.getParentId();
        if (StringUtils.isEmpty(parentId)){
            directoryVo.setParentId("0");
        }

        directoryVo.setIsDirectory(1);
        if (sourceService.newDirectory(directoryVo)){
            return R.ok();

        }
        return R.error().message("新建文件夹失败");
    }
}

