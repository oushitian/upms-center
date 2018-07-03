package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.MenuDao;
import com.jolly.upms.manager.dao.MenuGroupDao;
import com.jolly.upms.manager.dao.MenuGroupDetailDao;
import com.jolly.upms.manager.dao.RolePermissionDao;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.*;
import com.jolly.upms.manager.service.MenuGroupService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.vo.MenuGroupSaveVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-06-12
 */
@Service
@Transactional
public class MenuGroupServiceImpl extends BaseServiceImpl<MenuGroup> implements MenuGroupService {

    @Resource
    private MenuGroupDao menuGroupDao;

    @Resource
    private MenuGroupDetailDao menuGroupDetailDao;

    @Resource
    private RolePermissionDao rolePermissionDao;

    @Resource
    private MenuDao menuDao;

    @Override
    public BaseDao<MenuGroup> getDao() {
        return menuGroupDao;
    }

    @Override
    public List<DataDimension> queryDataDimensions(Integer menuGroupId) {
        return menuGroupDao.queryDataDimensions(menuGroupId);
    }

    @Override
    public void deleteMenuGroup(Integer menuGroupId) {
        List<RoleMenuGroup> roleMenuGroupList = menuGroupDao.queryRoleMenuGroup(menuGroupId);
        if (CollectionUtils.isNotEmpty(roleMenuGroupList)) throw new RuntimeException("该权限组已被角色绑定，请先解绑");
        menuGroupDao.delete(menuGroupId);//删除菜单组
        menuGroupDetailDao.deleteByMenuGroupId(menuGroupId);//删除菜单组明细
        menuGroupDao.deleteRoleMenuGroup(menuGroupId, null);//删除角色菜单组关系
    }

    @Override
    public List<MenuGroup> getByRoleId(Integer roleId) {
        return menuGroupDao.getByRoleId(roleId);
    }

    @Override
    public void saveOrUpdate(MenuGroupSaveVO saveVO) {
        Integer menuGroupId = saveVO.getMenuGroupId();
        boolean edit = false;
        if (menuGroupId == null) {
            MenuGroup menuGroupInDB = menuGroupDao.getByName(saveVO.getName(), saveVO.getApplicationId());
            if (menuGroupInDB != null) throw new RuntimeException("权限组名称已存在");
            MenuGroup menuGroup = new MenuGroup();
            BeanUtils.copyProperties(saveVO, menuGroup);
            menuGroup.setGmtCreated(DateUtils.getCurrentSecondIntValue());
            menuGroupDao.save(menuGroup);
            menuGroupId = menuGroup.getMenuGroupId();
        } else {
            edit = true;
            MenuGroup menuGroupInDB = menuGroupDao.getByName(saveVO.getName(), saveVO.getApplicationId());
            if (menuGroupInDB != null && !menuGroupInDB.getMenuGroupId().equals(menuGroupId))
                throw new RuntimeException("权限组名称已存在");
            MenuGroup menuGroup = new MenuGroup();
            BeanUtils.copyProperties(saveVO, menuGroup);
            menuGroupDao.updateSelective(menuGroup);
            menuGroupDetailDao.deleteByMenuGroupId(menuGroupId);//删除菜单组明细
        }
        //保存权限组菜单关系
        String menuIds = saveVO.getMenuIds();
        String[] menuIdArr = StringUtils.split(menuIds, ",");
        for (String menuId : menuIdArr) {
            MenuGroupDetail detail = new MenuGroupDetail();
            detail.setMenuGroupId(menuGroupId);
            detail.setMenuId(Integer.valueOf(menuId));
            menuGroupDetailDao.save(detail);
        }

        //如果是编辑，需要处理角色权限表
        if (edit) {
            //查询绑定了该权限组的角色
            List<RoleMenuGroup> roleMenuGroupList = menuGroupDao.queryRoleMenuGroup(menuGroupId);
            if (!roleMenuGroupList.isEmpty()) {
                //查询权限组菜单
                List<Menu> menuList = menuDao.queryMenusByMenuGroupId(menuGroupId);
                Set<String> permissionStrs = new HashSet<>();
                for (Menu menu : menuList) {
                    if (StringUtils.isNotBlank(menu.getPermissionString())) {
                        permissionStrs.add(menu.getPermissionString());
                    }
                }
                for (RoleMenuGroup roleMenuGroup : roleMenuGroupList) {
                    Integer roleId = roleMenuGroup.getRoleId();
                    //查询角色权限表
                    List<RolePermission> rolePermissionList = rolePermissionDao.queryByRoleId(roleId);
                    Set<String> permSet = new HashSet<>();
                    for (RolePermission rolePermission : rolePermissionList) {
                        permSet.add(rolePermission.getPermissionString());
                    }
                    ///////////////////////处理权限组新增菜单///////////////////////
                    Set<String> copySet = new HashSet<>(permissionStrs);
                    copySet.removeAll(permSet);//求差集
                    //如果求完差集以后，copySet不为空，说明权限组新增了菜单，则对应角色权限表也要新增
                    for (String permissionStr : copySet) {
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionString(permissionStr);
                        rolePermissionDao.saveSelective(rolePermission);
                    }

                    ///////////////////////处理权限组减少菜单///////////////////////
                    List<MenuGroup> menuGroupList = menuGroupDao.getByRoleId(roleId);//查询角色绑定的全部权限组
                    Set<String> permissionSet = new HashSet<>();//保存除此次权限组之外的其他权限组权限串
                    for (MenuGroup menuGroup : menuGroupList) {
                        if (menuGroup.getMenuGroupId().equals(menuGroupId)) continue;
                        //查询权限组菜单
                        List<Menu> menus = menuDao.queryMenusByMenuGroupId(menuGroup.getMenuGroupId());
                        for (Menu menu : menus) {
                            if (StringUtils.isNotBlank(menu.getPermissionString())) {
                                permissionSet.add(menu.getPermissionString());
                            }
                        }
                    }
                    permSet.removeAll(permissionStrs);//计算角色剩余权限串
                    for (String s : permSet) {
                        //剩余权限串不是其他权限组带来的，说明是此次权限组带来的，而此次权限组去掉了该菜单
                        if (!permissionSet.contains(s)) {
                            rolePermissionDao.deleteRolePermission(roleId, s);
                        }
                    }
                }
            }
        }
    }
}
