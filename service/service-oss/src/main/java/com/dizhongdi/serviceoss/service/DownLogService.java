package com.dizhongdi.serviceoss.service;

import com.dizhongdi.serviceoss.entity.DownLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-09-21
 */
public interface DownLogService extends IService<DownLog> {

    //判断是否下载购买过
    boolean getIsBuy(String sourceId, String memberId);

    //根据用户id查询记录
    List<DownLog> getByMemberId(String memberId);
}
