package com.dizhongdi.serviceoss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dizhongdi.serviceoss.client.UserClient;
import com.dizhongdi.serviceoss.entity.DzdSource;
import com.dizhongdi.serviceoss.entity.vo.SourceInfoVo;
import com.dizhongdi.serviceoss.entity.vo.SourceQuery;
import com.dizhongdi.serviceoss.mapper.DzdSourceMapper;
import com.dizhongdi.serviceoss.service.DzdSourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-21
 */
@Service
public class DzdSourceServiceImpl extends ServiceImpl<DzdSourceMapper, DzdSource> implements DzdSourceService {

    @Autowired
    UserClient userClient;

    @Override
    public List<SourceInfoVo> getPublicPageList(Page<DzdSource> sourcePage, SourceQuery userQuery) {
        List<SourceInfoVo> sourcesList = new ArrayList<>();
        QueryWrapper<DzdSource> wrapper = new QueryWrapper<>();
        wrapper.eq("is_public",1);
        //公开资源
        wrapper.eq("parent_id","0");
        wrapper.orderByDesc("gmt_create");
        List<DzdSource> records = this.page(sourcePage, wrapper).getRecords();
        records.stream().map(record ->{
            SourceInfoVo source = new SourceInfoVo();
            BeanUtils.copyProperties(record,source);
            //获取用户头像昵称
            BeanUtils.copyProperties(userClient.getAllInfoId(record.getUserId()),source);
            return source;
        }).forEach(source -> sourcesList.add(source));

        return sourcesList;
    }
}
