package com.dizhongdi.servicebase.exceptionhandler;

import com.dizhongdi.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName:GlobalExceptionHandler
 * Package:com.dizhongdi.com.dizhongdi.servicebase.handler
 * Description:
 *
 * @Date: 2022/5/9 21:52
 * @Author:dizhongdi
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("全局异常");
    }
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("特定异常处理");
    }

    @ExceptionHandler(DzdException.class)
    @ResponseBody
    public R error(DzdException e){
        log.error(e.getMsg());
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
