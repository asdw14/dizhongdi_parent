package com.dizhongdi.serviceoss.service;

import com.dizhongdi.serviceoss.entity.DownLog;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
