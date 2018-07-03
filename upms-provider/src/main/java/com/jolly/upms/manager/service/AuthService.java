package com.jolly.upms.manager.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jolly.upms.manager.dao.*;
import com.jolly.upms.manager.model.*;
import com.jolly.upms.manager.param.PermissionParam;
import com.jolly.upms.manager.result.UpmsResult;
import com.jolly.upms.manager.result.UpmsResultCode;
import com.jolly.upms.manager.util.*;
import com.jolly.upms.manager.vo.AccessTokenVO;
import com.jolly.upms.manager.vo.AuthResultVO;
import com.jolly.upms.manager.vo.BaseResponseVO;
import com.jolly.upms.manager.vo.LoginVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 认证业务
 * <p>
 * Created by dengjunbo
 * on 16/5/19.
 */
@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

    @Resource
    private RbacUserDao rbacUserDao;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserService userService;

    @Resource
    private RoleDao roleDao;

    @Resource
    private RolePermitApplicationDao rolePermitApplicationDao;

    public BaseResponseVO login(LoginVO loginVO, boolean isNew) {
        AuthResultVO resultVO = new AuthResultVO(Constant.STATUS_FAIL);
        Application application = getApplication(loginVO.getAppKey());
        if (application == null)
            return resultVO.setMessage("Not supported application");
        User user;
        if (isNew) {
            user = userDao.queryByUserName(loginVO.getUserName());
        } else {
            user = rbacUserDao.queryByUserName(loginVO.getUserName());
        }
        if (user == null) {
            return resultVO.setMessage("No account found for user '" + loginVO.getUserName() + "'");
        }

        if (!user.getPassword().equals(loginVO.getPassword())) {
            return resultVO.setMessage("Invalid password for user '" + loginVO.getUserName() + "'");
        }
        AuthUser authUser = new AuthUser();
        authUser.setUserId(user.getUserId());
        authUser.setUserName(user.getUserName());
        String token = TokenUtil.genToken(authUser, loginVO.getAppKey());
        //同一个用户在同一个应用只能登录一次
        //TODO 开发环境不做同一个用户在同一个应用只能登录一次
//        cacheService.deleteTokenByPrefix(Constant.AUTH_TOKEN_CACHE_PREFIX+"appKey_"+loginVO.getAppKey()+":userId_"+user.getUserId()+":");
        cacheService.putToken(token, authUser);
        return resultVO.setToken(token).setUserId(user.getUserId()).setUserName(user.getUserName())
                .setResult(Constant.STATUS_OK).setMessage("Login success");

    }

    public BaseResponseVO accessToken(AccessTokenVO tokenVO) {
        AuthResultVO resultVO = new AuthResultVO(Constant.STATUS_FAIL);
        Application application = getApplication(tokenVO.getAppKey());
        if (application == null)
            return resultVO.setMessage("Not supported application");

        String value = cacheService.getToken(tokenVO.getToken());
        if (StringUtils.isEmpty(value)) {
            return resultVO.setMessage("Unauthorized token");
        }

        AuthUser authUser = JSON.parseObject(value, AuthUser.class);
//        User user = userDao.get(authUser.getUserId());

        //供应商门户不允许其他系统的用户登录
//        if(tokenVO.getAppKey().equals(Constant.SUPPLIER_APPKEY)) {
//            if(user.getIsSuppUser() != Constant.USER_IS_SUPP_USER_YES) {
//                return resultVO.setMessage("Not supported application for user '" + authUser.getUserName() + "'");
//            }
//        }
        return resultVO.setToken(tokenVO.getToken()).setUserId(authUser.getUserId()).setUserName(authUser.getUserName())
                .setResult(Constant.STATUS_OK).setMessage("Access Token success");
    }

    public Map<String, Set<String>> getPermission(AuthUser authUser, Application application, String url, String token) {
        ////permissionStrings缓存
        String permissionKey = RedisKeyUtil.permissionKey(application.getApplicationId(), authUser.getUserId());
        Set<String> permissionStrings = JSON.parseObject(cacheService.get(permissionKey), new TypeReference<Set<String>>() {
        });
        if (CollectionUtils.isEmpty(permissionStrings)) {
            permissionStrings = userService.queryPermissionStrings(authUser.getUserName(), application.getApplicationId());
            if (CollectionUtils.isNotEmpty(permissionStrings))
                cacheService.set(permissionKey, permissionStrings, cacheService.getCachePermissionExpire());
        }
        String permissionString = ShiroUtils.convertPathToPermissionString(url);
        if (CollectionUtils.isEmpty(permissionStrings))
            return new HashMap<>();
        boolean hasPermission = false;
        if (CollectionUtils.isNotEmpty(permissionStrings)) {
            for (String sysPermission : permissionStrings) {
                if (permissionString.startsWith(sysPermission)) {
                    //如果是权限的url 地址则放行
                    hasPermission = true;
                    break;
                }
            }
        }
        if (!hasPermission) {
            return new HashMap<>();
        }
        String dataPermissionKey = RedisKeyUtil.dataPermissionKey(application.getApplicationId(), authUser.getUserId(), url);
        Map<String, Set<String>> dataDimensionMap = JSON.parseObject(cacheService.get(dataPermissionKey), new TypeReference<Map<String, Set<String>>>() {
        });
        if (MapUtils.isEmpty(dataDimensionMap)) {
            dataDimensionMap = userService.queryDataDimensionMap(authUser.getUserName(), application.getApplicationId(), permissionString);
            if (MapUtils.isNotEmpty(dataDimensionMap))
                cacheService.set(dataPermissionKey, dataDimensionMap, cacheService.getCacheDataPermissionExpire());
        }
        return dataDimensionMap;
    }

    public BaseResponseVO logout(String token, String appKey) {
        Map map = new HashMap();
        map.put("appKey", appKey);
        List<Application> applications = applicationDao.queryList(map);
        if (CollectionUtils.isEmpty(applications))
            return new BaseResponseVO(Constant.STATUS_FAIL, "invalid appKey");

        String value = cacheService.getToken(token);
        if (StringUtils.isBlank(value)) {
            return new BaseResponseVO(Constant.STATUS_FAIL, "invalid token");
        }
        cacheService.deleteToken(token);
        return new BaseResponseVO(Constant.STATUS_OK, "success");
    }

    public Map<String, Map<String, Set<String>>> batchGetPermissions(AuthUser authUser, Application application, Set<String> urls, String token) {
        Map<String, Map<String, Set<String>>> map = new HashMap<>();
        for (String url : urls) {
            Map<String, Set<String>> permission = getPermission(authUser, application, url, token);
            if (!permission.isEmpty())
                map.put(url, permission);
        }
        return map;
    }

    public AuthUser getAuthUser(HttpServletRequest request) {
        String cookieValue = WebUtils.getCookieValue(request, Constant.AUTH_SSO_COOKIE_NAME);
        if (StringUtils.isEmpty(cookieValue)) {
            return null;
        }
        String authToken = cacheService.getToken(cookieValue);
        if (StringUtils.isEmpty(authToken)) {
            return null;
        }
        return JSON.parseObject(authToken, AuthUser.class);
    }

    public AuthUser getAuthUser(String token) {
        String cache = cacheService.getToken(token);
        if (StringUtils.isEmpty(cache)) {
            return null;
        }
        return JSON.parseObject(cache, AuthUser.class);
    }

    /**
     * 提供给第三方获取当前用户操作当前功能的对应的数据权限维度和维度值
     *
     * @param permissionParam
     * @return
     */
    private UpmsResult getFunctionPermission(PermissionParam permissionParam) {
        UpmsResult upmsResult = new UpmsResult(UpmsResultCode.SUCCESS);
        Object tokenObj = cacheService.getToken(permissionParam.getToken());
        if (tokenObj == null) {
            return new UpmsResult(UpmsResultCode.INVALID_TOKEN, "无效的token");
        }
        AuthUser authUser = JSON.parseObject(String.valueOf(tokenObj), AuthUser.class);
        Application application = getApplication(permissionParam.getAppKey());
        if (application == null) {
            return new UpmsResult(UpmsResultCode.FAILED, "无效的appKey");
        }
        ////permissionStrings缓存
        String permissionKey = RedisKeyUtil.permissionKey(application.getApplicationId(), authUser.getUserId());
        Set<String> permissionStrings = JSON.parseObject(cacheService.get(permissionKey), new TypeReference<Set<String>>() {
        });
        if (CollectionUtils.isEmpty(permissionStrings)) {
            permissionStrings = userService.queryPermissionStrings(authUser.getUserName(), application.getApplicationId());
            cacheService.set(permissionKey, permissionStrings, cacheService.getCachePermissionExpire());
        }
        upmsResult.setData(permissionStrings);
        return upmsResult;
    }


    /**
     * 获取当前用户操作当前功能的对应的数据权限维度和维度值 权限系统自己用
     *
     * @param request
     * @return
     */
    public UpmsResult getFunctionPermission(HttpServletRequest request) {
        PermissionParam permissionParam = new PermissionParam();
        String cookieValue = WebUtils.getCookieValue(request, Constant.AUTH_SSO_COOKIE_NAME);
        permissionParam.setToken(cookieValue);
        permissionParam.setAppKey("upms");
        return getFunctionPermission(permissionParam);
    }

    public Application getApplication(String appKey) {
        Application application = null;
        String applicationKey = RedisKeyUtil.applicationKey(appKey);
        String str = cacheService.get(applicationKey);
        if (StringUtils.isBlank(str)) {
            Map params = new HashMap();
            params.put("appKey", appKey);
            List<Application> applicationList = applicationDao.queryList(params);
            if (!applicationList.isEmpty()) {
                application = applicationList.get(0);
                cacheService.set(applicationKey, application, cacheService.getCacheAppExpire());
            }
        } else {
            application = JSON.parseObject(str, Application.class);
        }
        return application;
    }

    public boolean validatePermission(AuthUser authUser, Application application, String url, String permissionString) {
        String permissionKey = RedisKeyUtil.permissionKey(application.getApplicationId(), authUser.getUserId());
        Set<String> permissionStrings = JSON.parseObject(cacheService.get(permissionKey), new TypeReference<Set<String>>() {
        });
        if (CollectionUtils.isEmpty(permissionStrings)) {
            permissionStrings = userService.queryPermissionStrings(authUser.getUserName(), application.getApplicationId());
            if (CollectionUtils.isNotEmpty(permissionStrings)) {
                cacheService.set(permissionKey, permissionStrings, cacheService.getCachePermissionExpire());
            }
        }
        if (permissionString == null) {
            // TODO: 18-4-24 后期支持自定义权限串
            permissionString = ShiroUtils.convertPathToPermissionString(url);
        }
        return CollectionUtils.isNotEmpty(permissionStrings) && permissionStrings.contains(permissionString);
    }

    public Set<Integer> getRolePermitApplicationIds(HttpServletRequest request) {
        AuthUser authUser = getAuthUser(request);
        Set<Integer> permitApplicationIds = new LinkedHashSet<>();
        String key = RedisKeyUtil.userPermitApplicationKey(authUser.getUserId());
        String str = cacheService.get(key);
        if (StringUtils.isBlank(str)) {
            Application application = getApplication(Constant.UPMS_APPKEY);
            Map map = new HashMap();
            map.put("userId", authUser.getUserId());
            map.put("applicationId", application.getApplicationId());
            //查找用户在UPMS系统下的角色
            List<Role> roleList = roleDao.queryListByUserId(map);
            //没有角色，可能是通过私有权限进入的系统
            if (roleList.isEmpty()) {
                cacheService.set(key, permitApplicationIds, cacheService.getCacheUserPermitAppExpire());
                return permitApplicationIds;
            }
            List<Integer> roleIds = new ArrayList<>();
            for (Role role : roleList) {
                roleIds.add(role.getRoleId());
            }
            map.clear();
            map.put("roleIds", roleIds);
            List<RolePermitApplication> permitApplicationList = rolePermitApplicationDao.queryList(map);
            if (permitApplicationList.isEmpty()) {
                cacheService.set(key, permitApplicationIds, cacheService.getCacheUserPermitAppExpire());
                return permitApplicationIds;
            }
            for (RolePermitApplication permitApplication : permitApplicationList) {
                permitApplicationIds.add(permitApplication.getApplicationId());
            }
            cacheService.set(key, permitApplicationIds, cacheService.getCacheUserPermitAppExpire());
            return permitApplicationIds;
        } else {
            return JSON.parseObject(str, new TypeReference<Set<Integer>>() {
            });
        }
    }
}
