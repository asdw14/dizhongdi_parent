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
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
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
    public List<SourceInfoVo> getPublicPageList(Page<DzdSource> sourcePage, SourceQuery sourceQuery,Boolean isAdmin) {
        List<SourceInfoVo> sourcesList = new ArrayList<>();
        QueryWrapper<DzdSource> wrapper = new QueryWrapper<>();
        //用户id
        String userId = sourceQuery.getUserId();
        //文件大小范围
        BigDecimal minfileSize = sourceQuery.getMinfileSize();
        BigDecimal maxfileSize = sourceQuery.getMaxfileSize();
        String sourceName = sourceQuery.getSourceName();
        Boolean isCharge = sourceQuery.getIsCharge();

        //公开资源
        wrapper.eq("is_public",1);

        //如果是管理员
        if (isAdmin==true){
            //管理员算法：根据最新上传来排序
            wrapper.orderByDesc("gmt_create");
        }   //如果是用户，获取热度最高的
        else {
            wrapper.orderByDesc("down_count");
            //获取未封禁资源
            wrapper.eq("is_ban",0);
        }

        if (sourceQuery!=null){
            //if查询条件里的上传用户id不为空
            if (!StringUtils.isEmpty(userId))
                wrapper.eq("user_id",userId);

            //if查询条件里的文件大小范围不为空
            if (!(StringUtils.isEmpty(minfileSize)&&StringUtils.isEmpty(maxfileSize)))
                wrapper.between("file_size",minfileSize,maxfileSize);

            //if查询条件里的文件名不为空
            if (!StringUtils.isEmpty(userId))
                wrapper.eq("source_name",sourceName);

            //if查询条件里的是否免费不为空
            if (isCharge!=null) {
                if (isCharge) {
                    wrapper.eq("is_charge", 0);
                }else {
                    wrapper.eq("is_charge", 1);
                }
            }

        }

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
