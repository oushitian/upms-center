package com.jolly.upms.facade.tag;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chenjc
 * @since 2018-04-25
 */
class CacheManager {

    /**
     * 存储权限串和数字的映射关系，以节约内存
     */
    private static final ConcurrentHashMap<String, Short> MAPPING_MAP = new ConcurrentHashMap<>();

    private static final AtomicInteger SERIAL_NO = new AtomicInteger(0);

    /**
     * <userId, <key, CacheData>>
     */
    private static final ConcurrentHashMap<Integer, Map<Short, CacheData>> CACHE_DATA = new ConcurrentHashMap<>();

    /**
     * 缓存数据存活时间
     */
    private static final int ALIVE_TIME_MIN = 5;

    /**
     * 定时任务执行间隔
     */
    private static final long PERIOD_MIN = 30;

    static {
        //启动定时任务，清除缓存超期的数据
        Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory()).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Map<Short, CacheData> cacheDataMap : CACHE_DATA.values()) {
                        Iterator<Short> iterator = cacheDataMap.keySet().iterator();
                        while (iterator.hasNext()) {
                            Short key = iterator.next();
                            CacheData cacheData = cacheDataMap.get(key);
                            if (cacheData == null) {
                                //已被另一个线程并发清除
                                continue;
                            }
                            if (cacheData.aliveTime <= System.currentTimeMillis()) {
                                iterator.remove();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, PERIOD_MIN, PERIOD_MIN, TimeUnit.MINUTES);
    }

    private static Byte getData(Integer userId, Short key, Load load) {
        Byte data = getData(userId, key);
        if (data == null) {
            data = load.load();
            if (data != null) {
                setData(userId, key, data);
            }
        }
        return data;
    }

    private static Byte getData(Integer userId, Short key) {
        Map<Short, CacheData> cacheDataMap = CACHE_DATA.get(userId);
        if (cacheDataMap != null) {
            CacheData cacheData = cacheDataMap.get(key);
            if (cacheData != null) {
                //清除缓存超期的数据
                if (cacheData.aliveTime <= System.currentTimeMillis()) {
                    cacheDataMap.remove(key);
                    return null;
                }
                return cacheData.data;
            }
        }
        return null;
    }

    private static void setData(Integer userId, Short key, Byte data) {
        Map<Short, CacheData> cacheDataMap = CACHE_DATA.get(userId);
        if (cacheDataMap == null) {
            CACHE_DATA.putIfAbsent(userId, new ConcurrentHashMap<Short, CacheData>());
            cacheDataMap = CACHE_DATA.get(userId);
        }
        //缓存5分钟
        cacheDataMap.put(key, new CacheData(data, ALIVE_TIME_MIN));
    }

    public static Byte getData(Integer userId, String permissionString, Load load) {
        Short key = MAPPING_MAP.get(permissionString);
        if (key == null) {
            //这里可能会浪费一次计数，并发量不大的情况下无影响
            MAPPING_MAP.putIfAbsent(permissionString, (short) SERIAL_NO.incrementAndGet());
            key = MAPPING_MAP.get(permissionString);
        }
        return getData(userId, key, load);
    }

    public interface Load {
        /**
         * 加载数据
         *
         * @return
         */
        Byte load();
    }

    /**
     * 缓存对象
     */
    private static class CacheData {

        CacheData(Byte data, int expire) {
            this.data = data;
            this.aliveTime = System.currentTimeMillis() + expire * 60 * 1000;
        }

        private Byte data;
        /**
         * 有效数据截止时间，单位毫秒
         */
        private long aliveTime;
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public DaemonThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "upms-cache-evictTimer-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
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
