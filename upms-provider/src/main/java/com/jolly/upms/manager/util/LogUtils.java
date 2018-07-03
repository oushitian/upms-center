package com.jolly.upms.manager.util;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.model.AuthUser;
import com.jolly.upms.manager.model.Log;
import com.jolly.upms.manager.service.CacheService;
import com.jolly.upms.manager.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author berkeley
 * @author chenjc
 * @since 2017-11-28
 */
public class LogUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtils.class);

    private static LogService logService = SpringUtil.getBean(LogService.class);
    private static CacheService cacheService = SpringUtil.getBean(CacheService.class);

    private static final ExecutorService insertDBThreadPool = new ThreadPoolExecutor(1, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1500), new DaemonThreadFactory());

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex) {
        if (ex == null && handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            if (method.isAnnotationPresent(SysLog.class)) {
                Log log = new Log();
                try {
                    String cookieValue = WebUtils.getCookieValue(request, Constant.AUTH_SSO_COOKIE_NAME);
                    if (StringUtils.isBlank(cookieValue)) {
                        return;
                    }
                    String authToken = cacheService.getToken(cookieValue);
                    if (StringUtils.isBlank(authToken)) {
                        return;
                    }
                    AuthUser authUser = JSON.parseObject(authToken, AuthUser.class);
                    log.setBasePath(RequestUtil.getBasePath(request));
                    log.setIp(RequestUtil.getIpAddr(request));
                    log.setMethod(request.getMethod());
                    log.setGmtCreated(DateUtils.getCurrentSecondIntValue());
                    log.setUri(request.getRequestURI());
                    log.setUrl(request.getRequestURL().toString());
                    log.setOperResult("1");
                    log.setResultMsg("success");
                    log.setUserId(String.valueOf(authUser.getUserId()));
                    SysLog sysLog = method.getAnnotation(SysLog.class);
                    SysLog.Operate operate = sysLog.value();
                    //填充编号
                    log.setOperName(operate.getCode());
                    //格式化入参
                    Map<String, String> paramMap = new LinkedHashMap<>();
                    Map<String, String[]> parameterMap = request.getParameterMap();
                    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                        String key = entry.getKey();
                        String[] values = entry.getValue();
                        if (values != null && values.length > 0) {
                            StringBuilder valueSb = new StringBuilder();
                            for (String value : values) {
                                valueSb.append(value).append(",");
                            }
                            valueSb.deleteCharAt(valueSb.lastIndexOf(","));
                            paramMap.put(key, valueSb.toString());
                        }
                    }
                    log.setContent(JSON.toJSONString(paramMap));
                    insertDBThreadPool.execute(new SaveLogRunnable(log));
                } catch (Exception e) {
                    LOGGER.error("记录日志异常，log={}", JSON.toJSONString(log), e);
                }
            }
        }
    }


    /**
     * 保存日志线程
     */
    public static class SaveLogRunnable implements Runnable {
        private Log log;

        public SaveLogRunnable(Log log) {
            this.log = log;
        }

        @Override
        public void run() {
            logService.save(log);
        }
    }


    private static class DaemonThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DaemonThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "insertDBThreadPool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

}
