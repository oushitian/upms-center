package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.RolePermission;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-19
 */
public interface RolePermissionDao extends BaseDao<RolePermission> {
    void deleteRolePermission(Integer roleId, String permStr);

    List<RolePermission> queryByRoleId(Integer roleId);

     Long countByLikeAttributeValueAndAttributeName(String attributeName, String attributeValue);
}
