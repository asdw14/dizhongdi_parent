package com.dizhongdi.serviceoss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;

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
}
