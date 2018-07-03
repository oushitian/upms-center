package jolly.upms.admin.web.controller.user;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.manager.annotation.SysLog;
import com.jolly.upms.manager.dao.RbacUserDao;
import com.jolly.upms.manager.dao.RoleDao;
import com.jolly.upms.manager.dao.UserPermissionDao;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.model.AuthUser;
import com.jolly.upms.manager.model.Role;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.service.*;
import com.jolly.upms.manager.util.Constant;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.util.MD5Util;
import com.jolly.upms.manager.util.PasswordUtil;
import com.jolly.upms.manager.vo.Pagination;
import jolly.upms.admin.web.controller.user.vo.UserParamVO;
import jolly.upms.admin.web.controller.user.vo.UserVO;
import jolly.upms.admin.web.vo.PageRespResult;
import jolly.upms.admin.web.vo.RespResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenjc
 * @since 2017-03-16
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final Pattern BLANK_PATTERN = Pattern.compile("[\\u00A0]+");

    @Resource
    private UserService userService;

    @Resource
    private RoleDao roleDao;

    @Autowired
    private AuthService authService;

    @Resource
    private RoleService roleService;
    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserPermissionDao userPermissionDao;

    @Resource
    private RbacUserDao rbacUserDao;

    @Resource
    private CacheService cacheService;

    private ConcurrentHashMap<Integer, String> cachedApplications = new ConcurrentHashMap<>();

    @RequestMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        Set<String> permissionStrings = (Set<String>) authService.getFunctionPermission(request).getData();
        model.addAttribute("permissionStrings", permissionStrings);
        List<Map<String, String>> list = rbacUserDao.querySuppliers();
        Map<String, String> supplierMap = new LinkedHashMap<>();
        for (Map<String, String> map : list) {
            supplierMap.put(map.get("code"), "[" + map.get("code") + "] " + map.get("suppName"));
        }
        model.addAttribute("applicationList", applicationService.queryList(null));
        model.addAttribute("supplierMap", supplierMap);
        return "user/userList";
    }

    @RequestMapping("/listData")
    @ResponseBody
    public PageRespResult listData(UserParamVO userParamVO) {
        Map params = new HashMap();
        params.put("fullUserName", userParamVO.getUserName());
        params.put("roleName", userParamVO.getRoleName());
        params.put("email", userParamVO.getEmail());
        params.put("applicationId", userParamVO.getApplicationId());
        params.put("privilege", userParamVO.getPrivilege());
        Pagination<User> userPagination = userService.queryList(params, userParamVO.getStart(), userParamVO.getRows());
        List<User> userList = userPagination.getResult();
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            Map map = new HashMap();
            map.put("userId", user.getUserId());
            List<Role> roleList = roleDao.queryListByUserId(map);
            if (!roleList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Role role : roleList) {
                    Integer applicationId = role.getApplicationId();
                    String appKey = cachedApplications.get(applicationId);
                    if (appKey == null) {
                        Application application = applicationService.get(applicationId);
                        appKey = application.getAppKey();
                        cachedApplications.putIfAbsent(applicationId, appKey);
                    }
                    sb.append("[" + appKey + "]" + role.getName()).append(",");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                userVO.setOwnRoles(sb.toString());
            }
            userVOList.add(userVO);
        }
        return new PageRespResult(userVOList, userPagination.getTotalCount());
    }

    @RequestMapping("/edit")
    @ResponseBody
    @SysLog(SysLog.Operate.USER_EDIT)
    public RespResult edit(User user, String roleIds, HttpServletRequest request) {
        String pwd = null;
        try {
            if (user.getUserId() == null) {
                String userName = user.getUserName();
                Matcher matcher = BLANK_PATTERN.matcher(userName);
                while (matcher.find()) {
                    return new RespResult(RespResult.RespCode.PARAM_EXCEPTION.getCode(), "用户名空格格式错误，请手动输入！");
                }
                pwd = PasswordUtil.randomPassword();
                user.setPassword(MD5Util.getMD5(pwd));
                user.setAddTime(DateUtils.getCurrentSecondIntValue());
            }
            userService.saveUser(user, roleIds, request);
        } catch (Exception e) {
            logger.error("新增或编辑用户异常，user=" + JSON.toJSONString(user), e);
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        if (pwd != null)
            return new RespResult(RespResult.RespCode.PWD_PROMPT.getCode(), pwd);
        return new RespResult();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @SysLog(SysLog.Operate.USER_DELETE)
    public RespResult delete(Integer userId) {
        try {
            userService.deleteUser(userId);
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

    @RequestMapping("/batchDelete")
    @ResponseBody
    @SysLog(SysLog.Operate.USER_BATCH_DELETE)
    public Map<String, Object> batchDelete(MultipartFile batchDelUserFile) {
        return userService.batchDelete(batchDelUserFile);
    }

    @RequestMapping("/resetPwd")
    @ResponseBody
    @SysLog(SysLog.Operate.USER_PASSWORD_UPDATE)
    public RespResult resetPwd(Integer userId, String password) {
        try {
            User user = userService.get(userId);
            if (user == null) {
                return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), "用户不存在");
            }
            User user4Update = new User();
            user4Update.setUserId(userId);
            user4Update.setPassword(password);
            userService.updateSelective(user4Update);
            try {
                //同步到who_rbac_user库，兼容jcm系统
                rbacUserDao.updateSelective(user4Update);
            } catch (Exception e) {
                logger.error("同步重置who_rbac_user表密码异常，username={}", user.getUserName(), e);
            }
            //删除redis中已存在的该用户的token
            cacheService.deleteTokenByPrefix(Constant.AUTH_TOKEN_CACHE_PREFIX + "appKey_upms:userId_" + userId + ":");
        } catch (Exception e) {
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), e.getMessage());
        }
        return new RespResult();
    }

    @RequestMapping("/saveUserPermissions")
    @ResponseBody
    @SysLog(SysLog.Operate.USER_PERMISSION_UPDATE)
    public RespResult saveUserPermissions(Integer userId, String menuIds, String dimensionValueDetails, HttpServletRequest request) {
        try {
            if (userId == null) throw new RuntimeException("参数不能为空");
            userService.saveUserPermissions(userId, menuIds, dimensionValueDetails, request);
        } catch (Exception e) {
            logger.error("分派私有权限异常，userId={}，menuIds={}，dimensionValueDetails={}", userId, menuIds, dimensionValueDetails, e);
            return new RespResult(RespResult.RespCode.SYS_EXCEPTION.getCode(), "操作异常，私有权限过多，请联系管理员！");
        }
        return new RespResult();
    }

    /**
     * 获取用户开通权限的应用
     */
    @RequestMapping("/getPermitApp")
    @SysLog(SysLog.Operate.USER_APP_SELECT)
    public String getPermitApp(HttpServletRequest request, ModelMap map, @CookieValue(value = "auth_token_new") String token) {
        Set<Application> permitApp = new HashSet<>();
        AuthUser authUser = authService.getAuthUser(request);
        List<Role> roleList = roleService.queryRoles(authUser.getUserId());
        List<Integer> applicationIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(roleList)) {
            for (Role role : roleList) {
                applicationIds.add(role.getApplicationId());
            }
        }
        Map<Integer, Application> applicationByIds = applicationService.getApplicationByIds(applicationIds);
        List<Integer> applicationIdsByUserId = userPermissionDao.getApplicationIdsByUserId(authUser.getUserId());
        Set<Integer> applicationIdsSet = new HashSet<>(applicationIdsByUserId);
        Map<Integer, Application> twoApplicationIds = applicationService.getApplicationByIds(new ArrayList(applicationIdsSet));
        permitApp.addAll(applicationByIds.values());
        permitApp.addAll(twoApplicationIds.values());
        //只有一个应用时直接跳转
        if (permitApp.size() == 1) {
            Iterator<Application> iterator = permitApp.iterator();
            Application application = iterator.next();
            return "redirect:" + application.getDomainName();
        }
        map.addAttribute(permitApp);
        map.addAttribute(authUser);
        map.addAttribute("token", token);
        return "authenticationAccess/selectapp";
    }
}
