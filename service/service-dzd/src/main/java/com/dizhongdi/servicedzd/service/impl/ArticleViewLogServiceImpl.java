package com.dizhongdi.servicedzd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.servicedzd.entity.ArticleViewLog;
import com.dizhongdi.servicedzd.mapper.ArticleViewLogMapper;
import com.dizhongdi.servicedzd.service.ArticleViewLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-27
 */
@Service
public class ArticleViewLogServiceImpl extends ServiceImpl<ArticleViewLogMapper, ArticleViewLog> implements ArticleViewLogService {


    //判断该用户是否已增加浏览次数 true：未增加 false：已增加
    @Override
    public boolean isAddByMemberId(String artccleId, String memberId) {
        ArticleViewLog articleViewLog = new ArticleViewLog();
        QueryWrapper<ArticleViewLog> wrapper = new QueryWrapper<ArticleViewLog>().
                eq("article_id", artccleId)
                .eq("member_id", memberId);
        //获取以该用户id和文章id查到的条数，大于0说明已经加过浏览次数
        Integer count = baseMapper.selectCount(wrapper);

        //true：未增加 false：已增加
        return count > 0 ? false : true;
    }


}
