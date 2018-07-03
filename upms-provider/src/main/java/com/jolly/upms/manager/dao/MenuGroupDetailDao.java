package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.MenuGroupDetail;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public interface MenuGroupDetailDao extends BaseDao<MenuGroupDetail> {
    void deleteByMenuGroupId(Integer menuGroupId);
}
