package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.Role;
import com.jolly.upms.manager.model.RoleMenuGroup;
import com.jolly.upms.manager.model.UserRole;
import com.jolly.upms.manager.vo.UserRoleVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-25
 */
public interface RoleDao extends BaseDao<Role> {
    List<Role> queryListByUserId(Map map);

    void saveUserRole(UserRole userRole);

    void deleteUserRole(Integer userId, Integer roleId);

    void saveRoleMenuGroup(RoleMenuGroup roleMenuGroup);

    Role getRoleByName(String name, Integer applicationId);

    List<UserRole> queryUserRole(Integer roleId);

    List<UserRoleVO> queryUserRoles(Set<Integer> roleIds, Integer userId);

    void deleteUserRoleByApplicationId(Integer userId, Set<Integer> permitApplicationIds);
}
