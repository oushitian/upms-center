package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.RolePermitApplicationDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.RolePermitApplication;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenjc
 * @since 2018-04-02
 */
@Repository
public class RolePermitApplicationDaoImpl extends BaseDaoImpl<RolePermitApplication> implements RolePermitApplicationDao {
    @Override
    public List<RolePermitApplication> getByRoleId(Integer roleId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.getByRoleId", roleId);
    }

    @Override
    public void deleteByRoleId(Integer roleId) {
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteByRoleId", roleId);
    }
}
