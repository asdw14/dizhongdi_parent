package com.dizhongdi.servicedzd.utils;

import java.util.concurrent.*;

/**
 * ClassName:MyCachedThread
 * Package:com.dizhongdi.servicedzd.utils
 * Description:
 *
 * @Date: 2022/8/15 19:19
 * @Author:dizhongdi
 */
public class MyCachedThread {
    public static ExecutorService getThreadPool() {
        return  new ThreadPoolExecutor(
                1,
                //根据cpu核心数分配线程数
                Runtime.getRuntime().availableProcessors()+1,
                2L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(Runtime.getRuntime().availableProcessors()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }
}
