package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.UserPermission;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-16
 */
public interface UserPermissionDao extends BaseDao<UserPermission> {
    /**
     * 根据userId查询upms_user_permission表的applicationId列表
     * @param userId
     * @return
     */
    List<Integer> getApplicationIdsByUserId(Integer userId);
}
