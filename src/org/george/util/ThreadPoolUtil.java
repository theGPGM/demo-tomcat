package org.george.util;

import org.george.http.Constant;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 请求处理线程池
 */
public class ThreadPoolUtil {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(Constant.ThreadPoolParameter.MIN_POOL_SIZE, Constant.ThreadPoolParameter.MAX_POOL_SIZE,
            Constant.ThreadPoolParameter.MAX_WAITING_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(Constant.ThreadPoolParameter.BLOCKING_QUEUE_CAPACITY));

    public static void run(Runnable runnable){
        executor.execute(runnable);
    }
}
