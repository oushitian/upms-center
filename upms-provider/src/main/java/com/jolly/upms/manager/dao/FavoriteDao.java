package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.Favorite;

/**
 * @author chenjc
 * @since 2017-12-14
 */
public interface FavoriteDao extends BaseDao<Favorite> {
    void deleteFavorite(Integer menuId, Integer userId, Integer applicationId);
}
