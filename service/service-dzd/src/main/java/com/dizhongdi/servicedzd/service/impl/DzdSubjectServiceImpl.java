package com.dizhongdi.servicedzd.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dizhongdi.result.R;
import com.dizhongdi.servicebase.exceptionhandler.DzdException;
import com.dizhongdi.servicedzd.entity.DzdSubject;
import com.dizhongdi.servicedzd.entity.excel.ExcelSubjectData;
import com.dizhongdi.servicedzd.entity.vo.SubjectOneVo;
import com.dizhongdi.servicedzd.entity.vo.SubjectTwoVo;
import com.dizhongdi.servicedzd.listener.SubjectExcelListener;
import com.dizhongdi.servicedzd.mapper.DzdSubjectMapper;
import com.dizhongdi.servicedzd.service.DzdSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 帖子分类 服务实现类
 * </p>
 *
 * @author dizhongdi
 * @since 2022-08-16
 */
@Service
public class DzdSubjectServiceImpl extends ServiceImpl<DzdSubjectMapper, DzdSubject> implements DzdSubjectService {

    @Override
    public void importSubjectData(MultipartFile file, DzdSubjectService subjectService) {
        try {
            //1 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
            EasyExcel.read(inputStream, ExcelSubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch(Exception e) {
            e.printStackTrace();
            throw new DzdException(20002,"添加课程分类失败");
        }
    }

    //获取嵌套课程list列表
    @Override
    public List<SubjectOneVo> nestedList() {
        long start = System.currentTimeMillis();
        ArrayList<SubjectOneVo> subjectOneVos = new ArrayList<>();
        List<DzdSubject> DzdSubjectList = baseMapper.selectList(new QueryWrapper<DzdSubject>().eq("parent_id", "0"));
        for (DzdSubject DzdSubject: DzdSubjectList) {
            List<DzdSubject> subjectTwoList = baseMapper.selectList(new QueryWrapper<DzdSubject>().eq("parent_id", DzdSubject.getId()));
            ArrayList<SubjectTwoVo> twoVos = new ArrayList<>();
            for (DzdSubject DzdSubject1 : subjectTwoList) {
                twoVos.add(new SubjectTwoVo().setId(DzdSubject1.getId()).setTitle(DzdSubject1.getTitle()));
            }
            subjectOneVos.add(new SubjectOneVo().setId(DzdSubject.getId()).setTitle(DzdSubject.getTitle()).setChildren(twoVos));
        }
        long end = System.currentTimeMillis();

        System.out.println("运行时间："+(end-start));
        return subjectOneVos;
    }
}
