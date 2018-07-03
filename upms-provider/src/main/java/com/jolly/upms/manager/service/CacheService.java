package com.jolly.upms.manager.service;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.manager.model.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存操作
 *
 * @author chenjc
 * @since 2018-05-25
 */
@Service
public class CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

    @Value("${cache.app.expire}")
    private Long cacheAppExpire;

    @Value("${cache.permission.expire}")
    private Long cachePermissionExpire;

    @Value("${cache.data.permission.expire}")
    private Long cacheDataPermissionExpire;

    @Value("${auth.token.expire.minute}")
    private Long cacheTokenExpire;

    @Value("${cache.user.permit.app.expire}")
    private Long cacheUserPermitAppExpire;

    @Value("${upms.deploy.overseas}")
    private boolean overseas;

    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate redisTemplateOverseas;

    private StringRedisTemplate getTemplate() {
        if (overseas) {
            return redisTemplateOverseas;
        }
        return redisTemplate;
    }

    public String get(String key) {
        ValueOperations<String, String> operations = getTemplate().opsForValue();
        return operations.get(key);
    }

    public void set(String key, Object value, Long expireTime) {
        ValueOperations<String, String> operations = getTemplate().opsForValue();
        operations.set(key, JSON.toJSONString(value), expireTime, TimeUnit.SECONDS);
    }

    public void putToken(String token, AuthUser authUser) {
        try {
            ValueOperations<String, String> operations1 = redisTemplate.opsForValue();
            operations1.set(token, JSON.toJSONString(authUser), cacheTokenExpire, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOGGER.error("国内redis连接失败", e);
        }
        try {
            ValueOperations<String, String> operations2 = redisTemplateOverseas.opsForValue();
            operations2.set(token, JSON.toJSONString(authUser), cacheTokenExpire, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOGGER.error("国外redis连接失败", e);
        }
    }

    public String getToken(String token) {
        ValueOperations<String, String> operations = getTemplate().opsForValue();
        String value = operations.get(token);
        if (value != null) {
            expireToken(token);
        }
        return value;
    }

    private void expireToken(String token) {
        getTemplate().expire(token, cacheTokenExpire, TimeUnit.MINUTES);
    }

    public void deleteToken(String token) {
        try {
            redisTemplate.delete(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            redisTemplateOverseas.delete(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTokenByPrefix(String tokenPrefix) {
        removePattern(tokenPrefix + "*");
    }

    private void removePattern(String pattern) {
        Set<String> keys = getTemplate().keys(pattern);
        if (keys.size() > 0) {
            try {
                redisTemplate.delete(keys);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                redisTemplateOverseas.delete(keys);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Long getCacheAppExpire() {
        return cacheAppExpire;
    }

    public Long getCachePermissionExpire() {
        return cachePermissionExpire;
    }

    public Long getCacheDataPermissionExpire() {
        return cacheDataPermissionExpire;
    }

    public Long getCacheUserPermitAppExpire() {
        return cacheUserPermitAppExpire;
    }
}