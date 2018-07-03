package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.FavoriteDao;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.Favorite;
import com.jolly.upms.manager.service.FavoriteService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author chenjc
 * @since 2017-12-14
 */
@Service
@Transactional
public class FavoriteServiceImpl extends BaseServiceImpl<Favorite> implements FavoriteService {

    @Resource
    private FavoriteDao favoriteDao;

    @Override
    public BaseDao<Favorite> getDao() {
        return favoriteDao;
    }

    @Override
    public void deleteFavorite(Integer menuId, Integer userId, Integer applicationId) {
        favoriteDao.deleteFavorite(menuId, userId, applicationId);
    }
}
