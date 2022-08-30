package com.dizhongdi.serviceoss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.serviceoss.entity.vo.DirectoryVo;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.entity.vo.UploadInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-21
 */
public interface DzdSourceService extends IService<DzdSource> {

    //分页获取公开资源，根据用户还是管理员分配不同推荐算法
    List<SourceInfoVo> getPublicPageList(Page<DzdSource> sourcePage, SourceQuery sourceQuery, Boolean isAdmin);

//    更新资源信息，如资源名称之类
    boolean updateInfo(String id, DzdSource source);

    //根据id修改封禁状态，封禁改为未封禁，未封禁改为封禁
    boolean updateBan(String id);

    //根据id获取资源
    SourceInfoVo getInfoById(String id);

    //根据id删除资源,包括oss上保存的文件
    boolean deleteByid(String id);

    //上传资源文件
    boolean uploadSource(MultipartFile file, UploadInfo uploadInfo);

    //新建文件夹
    boolean newDirectory(DirectoryVo directoryVo);

}
