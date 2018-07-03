package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.RolePermissionDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.RolePermission;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjc
 * @since 2017-06-19
 */
@Repository
public class RolePermissionDaoImpl extends BaseDaoImpl<RolePermission> implements RolePermissionDao {
    @Override
    public void deleteRolePermission(Integer roleId, String permStr) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("permStr", permStr);
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteRolePermission", map);
    }

    @Override
    public List<RolePermission> queryByRoleId(Integer roleId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryByRoleId", roleId);
    }

    @Override
    public Long countByLikeAttributeValueAndAttributeName(String attributeName, String attributeValue) {
        Map<String, Object> map = new HashMap<>();
        map.put("attributeName", attributeName);
        map.put("attributeValue", attributeValue);
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.countByLikeAttributeValueAndAttributeName", map);
    }
}
