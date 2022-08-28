package com.dizhongdi.servicedzd.service;

import com.dizhongdi.servicedzd.entity.ArticleViewLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-27
 */
public interface ArticleViewLogService extends IService<ArticleViewLog> {

   //判断该用户是否已增加浏览次数 true：未增加 false：已增加
    boolean isAddByMemberId(String artccleId, String memberId);
}
