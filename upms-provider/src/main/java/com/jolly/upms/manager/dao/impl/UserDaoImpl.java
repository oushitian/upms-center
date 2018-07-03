package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.UserDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.model.UserMenu;
import com.jolly.upms.manager.vo.PermissionDO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dengjunbo
 * on 2016-05-19 10:51:56.
 */

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    @Override
    public User queryByUserName(String userName) {
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.queryByUserName", userName);
    }

    @Override
    public List<PermissionDO> queryUserPermissions(Integer userId, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("applicationId", applicationId);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryUserPermissions", map);
    }

    @Override
    public List<PermissionDO> queryRolePermissions(Integer roleId, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("applicationId", applicationId);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryRolePermissions", map);
    }

    @Override
    public void deleteUserMenu(Integer userId) {
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteUserMenu", userId);
    }

    @Override
    public void deleteUserPermission(Integer userId) {
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteUserPermission", userId);
    }

    @Override
    public void saveUserMenu(UserMenu userMenu) {
        getSqlSession().insert(this.clazz.getName() + "Mapper.saveUserMenu", userMenu);
    }

    @Override
    public List<User> queryByEmail(String email) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryByEmail", email);
    }

    @Override
    public int updatePassword(Map<String, Object> map) {
        return getSqlSession().update(this.clazz.getName() + "Mapper.updatePassword", map);
    }

    @Override
    public List<User> getUserListByUserIds(List userIds) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.getUserListByUserIds", userIds);
    }

    @Override
    public List<User> queryUserByRoleIds(Set<Integer> roleIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleIds", roleIds);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryUserByRoleIds", map);
    }

    @Override
    public void resumeOrDelSuppUser(String suppCode, int isDeleted) {
        Map<String, Object> map = new HashMap<>();
        map.put("suppCode", suppCode);
        map.put("isDeleted", isDeleted);
        getSqlSession().update(this.clazz.getName() + "Mapper.resumeOrDelSuppUser", map);
    }

    @Override
    public User getUser(Integer userId, Boolean includeDeleted) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("includeDeleted", includeDeleted);
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.getUser", map);
    }

    @Override
    public List<UserMenu> queryUserMenus(Integer menuId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryUserMenus", menuId);
    }

    @Override
    public void deleteUserMenuByApplicationId(Integer userId, Set<Integer> permitApplicationIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("permitApplicationIds", permitApplicationIds);
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteUserMenuByApplicationId", map);
    }

    @Override
    public void deleteUserPermissionByApplicationId(Integer userId, Set<Integer> permitApplicationIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("permitApplicationIds", permitApplicationIds);
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteUserPermissionByApplicationId", map);
    }
}
