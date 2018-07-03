package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.DataDimension;
import com.jolly.upms.manager.model.MenuGroup;
import com.jolly.upms.manager.model.RoleMenuGroup;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public interface MenuGroupDao extends BaseDao<MenuGroup> {
    List<DataDimension> queryDataDimensions(Integer menuGroupId);

    List<MenuGroup> getByRoleId(Integer roleId);

    void deleteRoleMenuGroup(Integer menuGroupId, Integer roleId);

    MenuGroup getByName(String name, Integer applicationId);

    List<RoleMenuGroup> queryRoleMenuGroup(Integer menuGroupId);
}
