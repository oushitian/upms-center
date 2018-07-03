package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.UserPermissionDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.UserPermission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-16
 */
@Repository
public class UserPermissionDaoImpl extends BaseDaoImpl<UserPermission> implements UserPermissionDao {
    @Override
    public List<Integer> getApplicationIdsByUserId(Integer userId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.getApplicationIdsByUserId", userId);
    }
}
