package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.RoleDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.Role;
import com.jolly.upms.manager.model.RoleMenuGroup;
import com.jolly.upms.manager.model.UserRole;
import com.jolly.upms.manager.vo.UserRoleVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-25
 */
@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {
    @Override
    public List<Role> queryListByUserId(Map map) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryListByUserId", map);
    }

    @Override
    public void saveUserRole(UserRole userRole) {
        getSqlSession().insert(this.clazz.getName() + "Mapper.saveUserRole", userRole);
    }

    @Override
    public void deleteUserRole(Integer userId, Integer roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleId", roleId);
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteUserRole", map);
    }

    @Override
    public void saveRoleMenuGroup(RoleMenuGroup roleMenuGroup) {
        getSqlSession().insert(this.clazz.getName() + "Mapper.saveRoleMenuGroup", roleMenuGroup);
    }

    @Override
    public Role getRoleByName(String name, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("applicationId", applicationId);
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.getRoleByName", map);
    }

    @Override
    public List<UserRole> queryUserRole(Integer roleId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryUserRole", roleId);
    }

    @Override
    public List<UserRoleVO> queryUserRoles(Set<Integer> roleIds, Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleIds", roleIds);
        map.put("userId", userId);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryUserRoles", map);
    }

    @Override
    public void deleteUserRoleByApplicationId(Integer userId, Set<Integer> permitApplicationIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("permitApplicationIds", permitApplicationIds);
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteUserRoleByApplicationId", map);
    }
}
