package com.jolly.upms.manager.service;

import com.jolly.upms.manager.model.Favorite;
import com.jolly.upms.manager.service.base.BaseService;

/**
 * @author chenjc
 * @since 2017-12-14
 */
public interface FavoriteService extends BaseService<Favorite> {
    void deleteFavorite(Integer menuId, Integer userId, Integer applicationId);
}
