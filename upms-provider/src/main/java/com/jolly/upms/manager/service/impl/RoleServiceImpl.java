package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.*;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.*;
import com.jolly.upms.manager.service.RoleService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.vo.RoleSaveVO;
import com.jolly.upms.manager.vo.UserRoleVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author chenjc
 * @since 2017-05-25
 */
@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private UserDao userDao;

    @Resource
    private MenuGroupDao menuGroupDao;

    @Resource
    private RolePermissionDao rolePermissionDao;

    @Resource
    private MenuDao menuDao;

    @Resource
    private RolePermitApplicationDao rolePermitApplicationDao;

    @Override
    public BaseDao<Role> getDao() {
        return roleDao;
    }

    @Override
    public List<Role> queryRoles(String userName, Integer applicationId) {
        User user = userDao.queryByUserName(userName);
        if (user != null) {
            Map map = new HashMap();
            map.put("userId", user.getUserId());
            map.put("applicationId", applicationId);
            return roleDao.queryListByUserId(map);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Role> queryRoles(Integer userId) {
        Map map = new HashMap();
        map.put("userId", userId);
        return roleDao.queryListByUserId(map);
    }

    @Override
    public void deleteRole(Integer roleId) {
        List<UserRole> list = roleDao.queryUserRole(roleId);
        if (CollectionUtils.isNotEmpty(list)) throw new RuntimeException("该角色已被用户绑定，请先解绑");
        roleDao.delete(roleId);//删除角色
        roleDao.deleteUserRole(null, roleId);//删除用户角色关系
        menuGroupDao.deleteRoleMenuGroup(null, roleId);//删除角色菜单组关系
        rolePermissionDao.deleteRolePermission(roleId, null);//删除角色权限表
    }

    @Override
    public void saveOrUpdate(RoleSaveVO roleSaveVO) {
        Integer roleId = roleSaveVO.getRoleId();
        if (roleId == null) {
            //保存角色
            Role roleInDB = roleDao.getRoleByName(roleSaveVO.getName(), roleSaveVO.getApplicationId());
            if (roleInDB != null) throw new RuntimeException("角色名已存在");
            Role role = new Role();
            role.setName(roleSaveVO.getName());
            role.setApplicationId(roleSaveVO.getApplicationId());
            role.setGmtCreated(DateUtils.getCurrentSecondIntValue());
            role.setGmtModified(DateUtils.getCurrentSecondIntValue());
            roleDao.save(role);
            roleId = role.getRoleId();
        } else {
            Role roleInDB = roleDao.getRoleByName(roleSaveVO.getName(), roleSaveVO.getApplicationId());
            if (roleInDB != null && !roleInDB.getRoleId().equals(roleId)) throw new RuntimeException("角色名已存在");
            Role role = new Role();
            role.setRoleId(roleId);
            role.setName(roleSaveVO.getName());
            role.setGmtModified(DateUtils.getCurrentSecondIntValue());
            roleDao.updateSelective(role);//更新角色
            menuGroupDao.deleteRoleMenuGroup(null, roleId);//删除角色菜单组关系
            rolePermissionDao.deleteRolePermission(roleId, null);//删除角色权限表
        }

        Set<Menu> menuSet = new HashSet<>();
        String[] menuGroupIds = StringUtils.split(roleSaveVO.getMenuGroupIds(), ",");
        for (String menuGroupId : menuGroupIds) {
            //保存角色菜单组关系
            RoleMenuGroup roleMenuGroup = new RoleMenuGroup();
            roleMenuGroup.setRoleId(roleId);
            roleMenuGroup.setMenuGroupId(Integer.valueOf(menuGroupId));
            roleDao.saveRoleMenuGroup(roleMenuGroup);
            List<Menu> menuList = menuDao.queryMenusByMenuGroupId(Integer.valueOf(menuGroupId));
            menuSet.addAll(menuList);//去重
        }
        String[] dimensionArr = StringUtils.split(roleSaveVO.getDimensionValueDetails(), ";");
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
        for (Menu menu : menuSet) {
            //一级菜单和http开头的绝对路径菜单没有权限串
            if (menu.getType() == 1 || StringUtils.isBlank(menu.getPermissionString())) continue;
            //保存角色权限表
            Map<String, Set<String>> innerMap = map.get(menu.getMenuId());
            if (innerMap != null) {
                for (Map.Entry<String, Set<String>> entry : innerMap.entrySet()) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionString(menu.getPermissionString());
                    rolePermission.setAttributeName(entry.getKey());
                    StringBuilder sb = new StringBuilder();
                    for (String value : entry.getValue()) {
                        sb.append(value).append(",");
                    }
                    sb.deleteCharAt(sb.lastIndexOf(","));
                    rolePermission.setAttributeValues(sb.toString());
                    rolePermissionDao.saveSelective(rolePermission);
                }
            } else {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionString(menu.getPermissionString());
                rolePermissionDao.saveSelective(rolePermission);
            }
        }

    }

    @Override
    public List<UserRoleVO> queryUserRoles(Set<Integer> roleIds, Integer userId) {
        return roleDao.queryUserRoles(roleIds, userId);
    }

    @Override
    public void savePermitApplication(Integer roleId, String applicationIds) {
        if (roleId == null) {
            return;
        }
        rolePermitApplicationDao.deleteByRoleId(roleId);
        if (StringUtils.isNotBlank(applicationIds)) {
            String[] arr = StringUtils.split(applicationIds, ",");
            for (String s : arr) {
                RolePermitApplication permitApplication = new RolePermitApplication();
                permitApplication.setRoleId(roleId);
                permitApplication.setApplicationId(Integer.valueOf(s));
                permitApplication.setGmtCreated(DateUtils.getCurrentSecondIntValue());
                rolePermitApplicationDao.save(permitApplication);
            }
        }
    }
}
