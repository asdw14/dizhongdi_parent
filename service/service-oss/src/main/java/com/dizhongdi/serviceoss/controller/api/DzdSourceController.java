package com.dizhongdi.serviceoss.controller.api;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.result.R;
import com.dizhongdi.serviceoss.client.UserClient;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.dizhongdi.serviceoss.entity.vo.DirectoryVo;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.entity.vo.UploadInfo;
import com.dizhongdi.serviceoss.service.DownLogService;
import com.dizhongdi.serviceoss.service.DzdSourceService;
import com.dizhongdi.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    UserClient userClient;

    @Autowired
    DownLogService downLogService;

    @Autowired
    RedisTemplate redisTemplate;

    @ApiOperation(value = "分页获取公开资源")
    @Cacheable(value = "publicSourcePage")
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

    @ApiOperation(value = "分页获取公开资源")
    @PostMapping("getPublicQueryPageList/{page}/{limit}")
    public R getPublicQueryPageList(
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



    @ApiOperation(value = "根据文件夹id获取个人资源")
    @PostMapping("getMemberSourceByDirectoryId/{id}")
    public R getMemberSourceByDirectoryId(@PathVariable String id,
                                          @RequestBody(required = false) SourceQuery sourceQuery,
                                                HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("您还未登录哦，请先登录再查看个人空间资源^_^");
        }

        //如果父文件夹id为空直接获取顶层文件
        if (StringUtils.isEmpty(id)){
            id = "0";
        }

        List<SourceInfoVo> sourceList = sourceService.getMemberSourceByDirectoryId(id,memberId,sourceQuery);

        //根据获取的文件夹id获取文件夹父id以做返回
        String parentId = sourceService.getParentDirectoryId(id);
        return R.ok().data("items",sourceList).data("parentId",parentId);
    }

    @ApiOperation(value = "上传公开资源文件")
    @PostMapping("uploadSource/{sourceName}/{price}")
    public R upload(@ApiParam(name = "file", value = "文件", required = true) @RequestParam("file") MultipartFile file,
                    HttpServletRequest request,
                    @PathVariable(required = false) String sourceName,
                    @ApiParam(name = "price", value = "价格", required = false) @PathVariable(required = false)  Integer price) {
        //判断上传文件是否为空,为空返回报错
        boolean empty = file.isEmpty();
        if (empty) {
            return R.error().message("上传文件不能为空");
        }

        UploadInfo uploadInfo = new UploadInfo();
        //验证用户是否登录
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(userId)){
            return R.error().message("您还未进行登录哦！");
        }

        uploadInfo.setMemberId(userId);

        //用户给的名,没给就默认原始文件名
        if (StringUtils.isEmpty(sourceName)) {
            uploadInfo.setSourceName(file.getOriginalFilename());
        }
        uploadInfo.setSourceName(sourceName);

        //价格，为空默认为0
        if (StringUtils.isEmpty(price)) {
            uploadInfo.setPrice(0);
        }else {
            uploadInfo.setPrice(price);
        }

        uploadInfo.setParentId("0");

        uploadInfo.setIsPublic(1);
        //调用service方法上传
        boolean b = sourceService.uploadSource(file, uploadInfo);
        if (b) {
            return R.ok().message("上传成功");
        }
        return R.error().message("上传文件失败");

    }

    @ApiOperation(value = "个人云上传文件, 根据文件夹id上传到对应目录")
    @PostMapping("uploadByPrivate/{id}")
    public R uploadByPrivate(@ApiParam(name = "id", value = "文件夹id", required = true) @PathVariable String id , @ApiParam(name = "file", value = "文件", required = true) @RequestParam("files") MultipartFile[] files,
                          HttpServletRequest request){
        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("您还未登录，先登录后再进行上传");
        }
        UploadInfo uploadInfo = new UploadInfo();
        uploadInfo.setMemberId(memberId);
        uploadInfo.setIsPublic(0);
        if (StringUtils.isEmpty(id)){
            uploadInfo.setParentId("0");
        }
        uploadInfo.setParentId(id);

        for (int i = 0; i < files.length; i++) {

            //调用接口上传文件
            sourceService.uploadSource(files[i],uploadInfo);
        }

        return R.ok();
    }

    @ApiOperation(value = "新建文件夹")
    @PostMapping("createDirectory")
    public R newDirectory(@ApiParam(name = "directoryVo", value = "文件夹信息", required = true)
                          @RequestBody DirectoryVo directoryVo, HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("您还未登录哦，请先登录再创建文件夹^_^");
        }
        directoryVo.setMemberId(memberId);

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


    @ApiOperation(value = "根据id删除文件或文件夹")
    @DeleteMapping("deleteSource/{id}")
    public R deleteSource(@ApiParam(name = "id", value = "文件或文件夹id", required = true)
                          @PathVariable String id, HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("您还未登录哦，请先登录再删除文件^_^");
        }

        if(sourceService.deleteByMemberAndSourceId(memberId,id)){
            return R.ok();
        }else {
            return R.error().message("您的网络出现问题，请稍后重试");
        }
    }

    @ApiOperation(value = "根据id获取url")
    @PostMapping("getOssUrl/{id}")
    public R getOssUrl(@ApiParam(name = "id", value = "文件id", required = true)
                          @PathVariable String id, HttpServletRequest request){

        //验证用户是否登录
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)){
            return R.error().message("您还未登录哦，请先登录再下载文件^_^");
        }

        String url = null;
        //判断是否下载购买过
        if (downLogService.getIsBuy(id,memberId)){
            //购买过直接获取链接
            url =  sourceService.getById(id).getSourceOssUrl();
        }else {
            url = sourceService.getSourceUrl(id,memberId);
        }

        //如果能下载url不为空
        if (url!=null){
            Integer quantity = userClient.getQuantityById(memberId);

            //增加资源下载次数
            sourceService.addDownCount(id,1);

            //增加分享人一次下载次数
            userClient.addQuantityById(memberId,1);

            return R.ok().data("url",url).message("您的下载次数还剩: " + quantity + "次");
        }
        return R.error().message("下载次数不足");

    }

}

