package com.dizhongdi.serviceoss.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.serviceoss.entity.DownLog;
import com.dizhongdi.serviceoss.mapper.DownLogMapper;
import com.dizhongdi.serviceoss.service.DownLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-09-21
 */
@Service
public class DownLogServiceImpl extends ServiceImpl<DownLogMapper, DownLog> implements DownLogService {

    //判断是否下载购买过
    @Override
    public boolean getIsBuy(String sourceId, String memberId) {
        QueryWrapper<DownLog> wrapper = new QueryWrapper<>();
        wrapper.eq("source_id",sourceId).eq("member_id",memberId);
        return baseMapper.selectCount(wrapper) > 0 ? true : false;

    }

    @Override
    //根据用户id查询记录
    public List<DownLog> getByMemberId(String memberId) {
        Wrapper wrapper = new QueryWrapper<DownLog>().eq("member_id",memberId).orderByDesc("gmt_modified");

        return baseMapper.selectList(wrapper);
    }
}
