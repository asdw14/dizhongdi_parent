package com.dizhongdi.servicedzd.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.servicebase.exceptionhandler.DzdException;
import com.dizhongdi.servicedzd.entity.DzdSubject;
import com.dizhongdi.servicedzd.entity.excel.ExcelSubjectData;
import com.dizhongdi.servicedzd.service.DzdSubjectService;
import java.util.Map;

/**
 * ClassName:SubjectExcelListener
 * Package:com.dizhongdi.serviceedu.listener
 * Description:
 *帖子分类
 * @Date: 2022/6/15 23:52
 * @Author:dizhongdi
 */
public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    public DzdSubjectService subjectService;

    public SubjectExcelListener() {}
    //创建有参数构造，传递subjectService用于操作数据库
    public SubjectExcelListener(DzdSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    //一行一行去读取excle内容
    @Override
    public void invoke(ExcelSubjectData user, AnalysisContext analysisContext) {
        if(user == null) {
            throw new DzdException(20001,"添加失败");
        }
        //添加一级分类
        if (existOneSubject(user.getOneSubjectName())==null){
            subjectService.save(new DzdSubject().setTitle(user.getOneSubjectName()).setParentId("0"));
        }

        //获取一级分类id值
        String id = existOneSubject(user.getOneSubjectName()).getId();
        //添加二级分类
        if (existTwoSubject(user.getTwoSubjectName(),id)==null){
            subjectService.save(new DzdSubject().setTitle(user.getTwoSubjectName()).setParentId(id));
        }
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {}

    //判断二级分类是否重复
    private DzdSubject existTwoSubject(String name, String pid) {
        return subjectService.getOne(new QueryWrapper<DzdSubject>().eq("title", name).eq("parent_id", pid));
    }

    //判断一级分类是否重复
    private DzdSubject existOneSubject(String name) {
        return subjectService.getOne(new QueryWrapper<DzdSubject>().eq("title", name).eq("parent_id", "0"));

    }
}
