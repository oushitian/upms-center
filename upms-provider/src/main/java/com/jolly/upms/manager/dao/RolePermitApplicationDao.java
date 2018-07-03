package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.RolePermitApplication;

import java.util.List;

/**
 * @author chenjc
 * @since 2018-04-02
 */
public interface RolePermitApplicationDao extends BaseDao<RolePermitApplication> {
    List<RolePermitApplication> getByRoleId(Integer roleId);

    void deleteByRoleId(Integer roleId);
}
