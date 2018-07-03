package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.MenuGroupDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.DataDimension;
import com.jolly.upms.manager.model.MenuGroup;
import com.jolly.upms.manager.model.RoleMenuGroup;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjc
 * @since 2017-06-12
 */
@Repository
public class MenuGroupDaoImpl extends BaseDaoImpl<MenuGroup> implements MenuGroupDao {
    @Override
    public List<DataDimension> queryDataDimensions(Integer menuGroupId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryDataDimensions", menuGroupId);
    }

    @Override
    public List<MenuGroup> getByRoleId(Integer roleId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.getByRoleId", roleId);
    }

    @Override
    public void deleteRoleMenuGroup(Integer menuGroupId, Integer roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("menuGroupId", menuGroupId);
        map.put("roleId", roleId);
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteRoleMenuGroup", map);
    }

    @Override
    public MenuGroup getByName(String name, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("applicationId", applicationId);
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.getByName", map);
    }

    @Override
    public List<RoleMenuGroup> queryRoleMenuGroup(Integer menuGroupId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryRoleMenuGroup", menuGroupId);
    }
}
