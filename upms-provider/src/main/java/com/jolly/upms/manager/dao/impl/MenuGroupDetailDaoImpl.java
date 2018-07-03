package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.MenuGroupDetailDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.MenuGroupDetail;
import org.springframework.stereotype.Repository;

/**
 * @author chenjc
 * @since 2017-06-12
 */
@Repository
public class MenuGroupDetailDaoImpl extends BaseDaoImpl<MenuGroupDetail> implements MenuGroupDetailDao {
    @Override
    public void deleteByMenuGroupId(Integer menuGroupId) {
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteByMenuGroupId", menuGroupId);
    }
}