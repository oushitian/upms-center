package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.FavoriteDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.Favorite;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenjc
 * @since 2017-12-14
 */
@Repository
public class FavoriteDaoImpl extends BaseDaoImpl<Favorite> implements FavoriteDao {
    @Override
    public void deleteFavorite(Integer menuId, Integer userId, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("menuId", menuId);
        map.put("userId", userId);
        map.put("applicationId", applicationId);
        getSqlSession().update(this.clazz.getName() + "Mapper.deleteFavorite", map);
    }
}
