package com.jolly.upms.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.jolly.upms.manager.dao.*;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.*;
import com.jolly.upms.manager.param.EmailForwardMqParam;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.UserService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import com.jolly.upms.manager.util.*;
import com.jolly.upms.manager.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author chenjc
 * @since 2017-05-25
 */
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Resource
    private RbacUserDao rbacUserDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private MenuDao menuDao;

    @Resource
    private UserPermissionDao userPermissionDao;

    @Resource
    private AuthService authService;

    @Override
    public BaseDao<User> getDao() {
        return userDao;
    }

    @Override
    public List<PermissionVO> queryPermissions(String userName, Integer applicationId) {
        User user = userDao.queryByUserName(userName);
        List<PermissionVO> permissionVOList = new ArrayList<>();
        if (user != null) {
            List<PermissionDO> permissionDOAll = new ArrayList<>();
            //用户权限查询
            List<PermissionDO> userPermissions = userDao.queryUserPermissions(user.getUserId(), applicationId);
            permissionDOAll.addAll(userPermissions);
            Map map = new HashMap();
            map.put("userId", user.getUserId());
            map.put("applicationId", applicationId);
            List<Role> roles = roleDao.queryListByUserId(map);
            for (Role role : roles) {
                //用户所属角色权限查询
                List<PermissionDO> rolePermissions = userDao.queryRolePermissions(role.getRoleId(), applicationId);
                permissionDOAll.addAll(rolePermissions);
            }
            for (PermissionDO permissionDO : permissionDOAll) {
                PermissionVO permissionVO = new PermissionVO(permissionDO.getPermissionString());
                String attributeName = permissionDO.getAttributeName();
                if (permissionVOList.contains(permissionVO)) {//如果permissionVOList已经包含该权限串
                    if (StringUtils.isNotBlank(attributeName)) {
                        String[] values = StringUtils.split(permissionDO.getAttributeValues(), ",");
                        Set<String> set = new HashSet<>(Arrays.asList(values));
                        PermissionVO existVO = permissionVOList.get(permissionVOList.indexOf(permissionVO));//拿到已经存在的PermissionVO
                        Map<String, Set<String>> dataDimensionMap = existVO.getDataDimensionMap();
                        if (dataDimensionMap == null) {//如果没有数据权限，就新增一个数据权限
                            dataDimensionMap = new HashMap<>();
                            dataDimensionMap.put(attributeName, set);
                            existVO.setDataDimensionMap(dataDimensionMap);
                        } else {//已经有数据权限
                            Set<String> existValues = dataDimensionMap.get(attributeName);
                            if (existValues == null) {//没有找到相同的数据维度，就新增一个数据维度
                                dataDimensionMap.put(attributeName, set);
                            } else {//找到了相同的数据维度，就合并数据维度值
                                existValues.addAll(set);
                            }
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(attributeName)) {
                        String[] values = StringUtils.split(permissionDO.getAttributeValues(), ",");
                        Set<String> set = new HashSet<>(Arrays.asList(values));
                        Map<String, Set<String>> dataDimensionMap = new HashMap<>();
                        dataDimensionMap.put(attributeName, set);
                        permissionVO.setDataDimensionMap(dataDimensionMap);
                    }
                    permissionVOList.add(permissionVO);
                }
            }
        }
        return permissionVOList;
    }

    @Override
    public Set<String> queryPermissionStrings(String userName, Integer applicationId) {
        User user = userDao.queryByUserName(userName);
        Set<String> set = new HashSet<>();
        if (user != null) {
            List<PermissionDO> permissionDOAll = new ArrayList<>();
            //用户权限查询
            List<PermissionDO> userPermissions = userDao.queryUserPermissions(user.getUserId(), applicationId);
            permissionDOAll.addAll(userPermissions);
            Map map = new HashMap();
            map.put("userId", user.getUserId());
            map.put("applicationId", applicationId);
            List<Role> roles = roleDao.queryListByUserId(map);
            for (Role role : roles) {
                //用户所属角色权限查询
                List<PermissionDO> rolePermissions = userDao.queryRolePermissions(role.getRoleId(), applicationId);
                permissionDOAll.addAll(rolePermissions);
            }
            for (PermissionDO permissionDO : permissionDOAll) {
                if (StringUtils.isNotBlank(permissionDO.getPermissionString())) {
                    set.add(permissionDO.getPermissionString());
                }
            }
        }
        return set;
    }

    @Override
    public Map<String, Set<String>> queryDataDimensionMap(String userName, Integer applicationId, String permissionString) {
        User user = userDao.queryByUserName(userName);
        Map<String, Set<String>> dataDimensionMap = new HashMap<>();
        if (user != null) {
            List<PermissionDO> permissionDOAll = new ArrayList<>();
            //用户权限查询
            List<PermissionDO> userPermissions = userDao.queryUserPermissions(user.getUserId(), applicationId);
            userPermissions.retainAll(Arrays.asList(new PermissionDO(permissionString)));//只保留和permissionString相同的对象
            permissionDOAll.addAll(userPermissions);
            Map map = new HashMap();
            map.put("userId", user.getUserId());
            map.put("applicationId", applicationId);
            List<Role> roles = roleDao.queryListByUserId(map);
            for (Role role : roles) {
                //用户所属角色权限查询
                List<PermissionDO> rolePermissions = userDao.queryRolePermissions(role.getRoleId(), applicationId);
                rolePermissions.retainAll(Arrays.asList(new PermissionDO(permissionString)));//只保留和permissionString相同的对象
                permissionDOAll.addAll(rolePermissions);
            }
            for (PermissionDO permissionDO : permissionDOAll) {
                String attributeName = permissionDO.getAttributeName();
                if (StringUtils.isNotBlank(attributeName)) {
                    String[] values = StringUtils.split(permissionDO.getAttributeValues(), ",");
                    Set<String> set = new HashSet<>(Arrays.asList(values));
                    Set<String> existValues = dataDimensionMap.get(attributeName);
                    if (existValues == null) {//没有找到相同的数据维度，就新增一个数据维度
                        dataDimensionMap.put(attributeName, set);
                    } else {//找到了相同的数据维度，就合并数据维度值
                        existValues.addAll(set);
                    }
                }
            }
        }
        return dataDimensionMap;
    }

    @Override
    public User queryByUserName(String userName) {
        return userDao.queryByUserName(userName);
    }

    @Override
    public void saveUser(User user, String roleIds, HttpServletRequest request) {
        String userName = user.getUserName();
        Integer userId = user.getUserId();
        String[] roleIdArr = StringUtils.split(roleIds, ",");
        Map map = new HashMap();
        map.put("roleIds", roleIdArr);
        List<Role> roleList = roleDao.queryList(map);
        List<Integer> applicationIds = new ArrayList<>();
        for (Role role : roleList) {
            if (applicationIds.contains(role.getApplicationId())) throw new RuntimeException("同一系统下只能绑定一个角色");
            applicationIds.add(role.getApplicationId());
        }
        if (userId == null) {
            if (user.getIsSuppUser() == 0) {
                user.setSuppCode("");
            } else {
                if (StringUtils.isBlank(user.getSuppCode())) {
                    throw new RuntimeException("供应商用户请选择供应商");
                }
            }
            User userInDB = userDao.queryByUserName(userName);
            if (userInDB != null) throw new RuntimeException("用户名已存在");
            List<User> userList = userDao.queryByEmail(user.getEmail());
            if (!userList.isEmpty()) throw new RuntimeException("邮箱已存在");
            Integer maxUserId = rbacUserDao.getMaxUserId();
            //先保存upms_user表，再保存who_rbac_user表，如果who_rbac_user表失败抛出异常，刚好将upms_user表的事务回滚
            user.setUserId(maxUserId + 2);
            userDao.save(user);
        } else {
            User userInDB = userDao.queryByUserName(userName);
            if (userInDB != null && !userInDB.getUserId().equals(userId)) throw new RuntimeException("用户名已存在");
            List<User> userList = userDao.queryByEmail(user.getEmail());
            for (User u : userList) {
                if (!u.getUserId().equals(userId)) throw new RuntimeException("邮箱已存在");
            }
            userDao.updateSelective(user);
            //只删除属于当前用户能访问的应用的角色
            Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
            applicationIds.removeAll(permitApplicationIds);
            if (!applicationIds.isEmpty()) {
                throw new RuntimeException("应用许可已过期，请先刷新页面");
            }
            roleDao.deleteUserRoleByApplicationId(userId, permitApplicationIds);
        }
        for (String roleId : roleIdArr) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(Integer.valueOf(roleId));
            roleDao.saveUserRole(userRole);
        }
        //同步到who_rbac_user库，兼容jcm系统
        if (userId == null) {
            User user1 = userDao.get(user.getUserId());
            user1.setNavList("");
            try {
                rbacUserDao.save(user1);
            } catch (Exception e) {
                logger.error("新增用户异常", e);
                String keyId = StringUtils.substringBetween(e.getMessage(), "Duplicate entry ", " for key 'PRIMARY'");
                throw new RuntimeException("主键 " + keyId + " 出现冲突，请再试一次！");
            }
        } else {
            rbacUserDao.updateSelective(user);
        }
    }

    @Override
    public void saveUserPermissions(Integer userId, String menuIds, String dimensionValueDetails, HttpServletRequest request) {
        String[] menuIdArr = StringUtils.split(menuIds, ",");
        String[] dimensionArr = StringUtils.split(dimensionValueDetails, ";");
        Map<Integer, Map<String, Set<String>>> map = new HashMap<>();//外层map的key=菜单ID，内层map的key=数据维度，value=数据维度值集合
        if (dimensionArr.length > 0) {
            for (String str : dimensionArr) {
                String[] detailArr = StringUtils.split(str, ",");
                Integer menuId = Integer.valueOf(detailArr[0]);
                String attrName = detailArr[1];
                String attrValue = detailArr[2];
                Map<String, Set<String>> innerMap = map.get(menuId);
                if (innerMap == null) {
                    innerMap = new HashMap<>();
                    Set<String> innerSet = new HashSet<>();
                    innerSet.add(attrValue);
                    innerMap.put(attrName, innerSet);
                    map.put(menuId, innerMap);
                } else {
                    Set<String> innerSet = innerMap.get(attrName);
                    if (innerSet == null) {
                        innerSet = new HashSet<>();
                        innerSet.add(attrValue);
                        innerMap.put(attrName, innerSet);
                    } else {
                        innerSet.add(attrValue);
                    }
                }

            }
        }
        List<Menu> menuList = new ArrayList<>();
        Set<Integer> applicationIds = new HashSet<>();
        if (menuIdArr.length > 0) {
            Map params = new HashMap();
            params.put("menuIds", menuIdArr);
            menuList = menuDao.queryList(params);
            for (Menu menu : menuList) {
                applicationIds.add(menu.getApplicationId());
            }
        }
        //只删除属于当前用户能访问的应用的菜单和权限串
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        applicationIds.removeAll(permitApplicationIds);
        if (permitApplicationIds.isEmpty() || !applicationIds.isEmpty()) {
            throw new RuntimeException("应用许可已过期，请先刷新页面");
        }
        userDao.deleteUserMenuByApplicationId(userId, permitApplicationIds);//删除用户菜单关系
        userDao.deleteUserPermissionByApplicationId(userId, permitApplicationIds);//删除用户权限表

        for (Menu menu : menuList) {
            //保存用户菜单关系
            UserMenu userMenu = new UserMenu();
            userMenu.setUserId(userId);
            userMenu.setMenuId(menu.getMenuId());
            userDao.saveUserMenu(userMenu);

            //一级菜单和http开头的绝对路径菜单没有权限串
            if (menu.getType() == 1 || StringUtils.isBlank(menu.getPermissionString())) continue;

            //保存用户权限表
            Map<String, Set<String>> innerMap = map.get(menu.getMenuId());
            if (innerMap != null) {
                for (Map.Entry<String, Set<String>> entry : innerMap.entrySet()) {
                    UserPermission userPermission = new UserPermission();
                    userPermission.setUserId(userId);
                    userPermission.setApplicationId(menu.getApplicationId());
                    userPermission.setPermissionString(menu.getPermissionString());
                    userPermission.setAttributeName(entry.getKey());
                    StringBuilder sb = new StringBuilder();
                    for (String value : entry.getValue()) {
                        sb.append(value).append(",");
                    }
                    sb.deleteCharAt(sb.lastIndexOf(","));
                    userPermission.setAttributeValues(sb.toString());
                    userPermissionDao.saveSelective(userPermission);
                }
            } else {
                UserPermission userPermission = new UserPermission();
                userPermission.setUserId(userId);
                userPermission.setApplicationId(menu.getApplicationId());
                userPermission.setPermissionString(menu.getPermissionString());
                userPermissionDao.saveSelective(userPermission);
            }
        }

    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        return userDao.updatePassword(map);
    }

    @Override
    public Map<Integer, User> getUserMapByUserIds(List userIds) {
        Map<Integer, User> userMap = new HashMap<>();
        if (CollectionUtils.isEmpty(userIds)) {
            return userMap;
        }
        List<User> userList = userDao.getUserListByUserIds(userIds);
        if (CollectionUtils.isEmpty(userList)) {
            return userMap;
        }
        for (User user : userList) {
            userMap.put(user.getUserId(), user);
        }
        return userMap;
    }


    public void sendFindPasswordMail(AuthUser user, String templateId, String language) {
        User dbUser = get(user.getUserId());
        EmailForwardMqParam emailParam = new EmailForwardMqParam(dbUser.getEmail(), MailTemplate.FIND_PASSWORD_MAIL, language);
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(Constant.MAIL_PARAM.get("userName"), user.getUserName());
        paramMap.put(Constant.MAIL_PARAM.get("validateCode"), MD5Util.getMD5(dbUser.getUserName() + dbUser.getPassword()));
        emailParam.setParamsJson(paramMap);
        MqUtil.sendMessage(MqUtil.getDefaultExchange(), Constant.DEFAULT_ROUTE_KEY, JSON.toJSONString(emailParam));
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = new User();
        OriginalObjectHolder.putOriginal(userDao.get(userId));
        user.setUserId(userId);
        user.setIsDeleted((byte) 1);
        userDao.updateSelective(user);
        //删除用户角色表
        roleDao.deleteUserRole(userId, null);
        //删除用户菜单表
        userDao.deleteUserMenu(userId);
        //删除用户权限表
        userDao.deleteUserPermission(userId);
        //同步到who_rbac_user库，兼容jcm系统
        rbacUserDao.updateSelective(user);
    }

    @Override
    public List<User> queryUserByRoleIds(Set<Integer> roleIds) {
        return userDao.queryUserByRoleIds(roleIds);
    }

    @Override
    public void resumeOrDelSuppUser(String suppCode, int isDeleted) {
        userDao.resumeOrDelSuppUser(suppCode, isDeleted);
    }

    @Override
    public User getUser(Integer userId, Boolean includeDeleted) {
        return userDao.getUser(userId, includeDeleted);
    }

    @Override
    public BaseResponseVO addUser(User user) {
        String userName = user.getUserName();
        User userInDB = userDao.queryByUserName(userName);
        if (userInDB != null) throw new RuntimeException("用户名已存在");
        Integer maxUserId = rbacUserDao.getMaxUserId();
        //先保存upms_user表，再保存who_rbac_user表，如果who_rbac_user表失败抛出异常，刚好将upms_user表的事务回滚
        user.setUserId(maxUserId + 2);
        userDao.save(user);
        //同步到who_rbac_user库，兼容jcm系统
        User user1 = userDao.get(user.getUserId());
        user1.setNavList("");
        try {
            rbacUserDao.save(user1);
        } catch (Exception e) {
            logger.error("新增用户异常", e);
            String keyId = StringUtils.substringBetween(e.getMessage(), "Duplicate entry ", " for key 'PRIMARY'");
            throw new RuntimeException("主键 " + keyId + " 出现冲突，请再试一次！");
        }
        return new AuthResultVO(Constant.STATUS_OK).setUserId(user.getUserId()).setUserName(user.getUserName()).setMessage("新增用户成功");
    }

    @Override
    public Map<String, Object> batchDelete(MultipartFile batchDelUserFile) {
        List<List<UserVO4BatchDelete>> list;
        try {
            list = ExcelUtil.importExcel(batchDelUserFile.getInputStream(), UserVO4BatchDelete.class);
        } catch (Exception e) {
            logger.error("excel文件转换失败！", e);
            Map<String, Object> resultMap = new HashMap<>(2);
            resultMap.put("code", -1);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }

        List<UserVO4BatchDelete> successList = list.get(0);//转换成功列表
        List<UserVO4BatchDelete> failList = list.get(1);//转换失败列表

        //数据处理
        updateFailListReason(failList);
        filterImportSuccessList(successList, failList);

        //构造返回信息
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("failCount", failList.size());
        resultMap.put("successCount", successList.size());
        resultMap.put("failList", failList);
        return resultMap;
    }

    private void filterImportSuccessList(List<UserVO4BatchDelete> successList, List<UserVO4BatchDelete> failList) {
        Set<String> repeatSet = new HashSet<>();
        Iterator<UserVO4BatchDelete> iterator = successList.iterator();
        while (iterator.hasNext()) {
            UserVO4BatchDelete userVO4BatchDelete = iterator.next();
            //导入数据合法性校验
            boolean isFlg = checkImportLegal(userVO4BatchDelete, failList);
            if (!isFlg) {
                iterator.remove();
                continue;
            }

            String userName = userVO4BatchDelete.getUserName().trim();
            String email = userVO4BatchDelete.getEmail().trim();
            String key = (userName + "_" + email).toLowerCase();
            //校验excel中是否有重复分类数据
            if (repeatSet.contains(key)) {
                userVO4BatchDelete.setFailReason("EXCEL数据重复！");
                failList.add(userVO4BatchDelete);
                iterator.remove();
                continue;
            }
            repeatSet.add(key);
            Map params = new HashMap();
            params.put("fullUserName", userName);
            params.put("email", email);
            List<User> users = userDao.queryList(params);
            if (users.isEmpty()) {
                userVO4BatchDelete.setFailReason("用户不存在！");
                failList.add(userVO4BatchDelete);
                iterator.remove();
                continue;
            }
            //删除
            deleteUser(users.get(0).getUserId());
        }
    }

    private boolean checkImportLegal(UserVO4BatchDelete userVO4BatchDelete, List<UserVO4BatchDelete> failList) {
        String userName = userVO4BatchDelete.getUserName();
        String email = userVO4BatchDelete.getEmail();
        if (StringUtils.isBlank(userName)) {
            userVO4BatchDelete.setFailReason("用户名不能为空！");
            failList.add(userVO4BatchDelete);
            return false;
        }
        if (StringUtils.isBlank(email)) {
            userVO4BatchDelete.setFailReason("邮箱地址不能为空！");
            failList.add(userVO4BatchDelete);
            return false;
        }
        return true;
    }

    private void updateFailListReason(List<UserVO4BatchDelete> failList) {
        if (CollectionUtils.isEmpty(failList)) {
            return;
        }
        for (UserVO4BatchDelete userVO4BatchDelete : failList) {
            userVO4BatchDelete.setFailReason("Excel数据填写有误,读取失败！");
        }
    }

}
