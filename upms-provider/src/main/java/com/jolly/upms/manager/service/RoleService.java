package com.jolly.upms.manager.service;

import com.jolly.upms.manager.model.Role;
import com.jolly.upms.manager.service.base.BaseService;
import com.jolly.upms.manager.vo.RoleSaveVO;
import com.jolly.upms.manager.vo.UserRoleVO;

import java.util.List;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-25
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 根据用户名查询角色
     *
     * @param userName
     * @param applicationId
     * @return
     */
    List<Role> queryRoles(String userName, Integer applicationId);

    List<Role> queryRoles(Integer userId);

    void deleteRole(Integer roleId);


    void saveOrUpdate(RoleSaveVO roleSaveVO);

    List<UserRoleVO> queryUserRoles(Set<Integer> roleIds, Integer userId);

    void savePermitApplication(Integer roleId, String applicationIds);
}
