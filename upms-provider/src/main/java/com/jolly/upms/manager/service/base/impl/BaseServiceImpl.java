package com.jolly.upms.manager.service.base.impl;


import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.base.BaseEntity;
import com.jolly.upms.manager.service.base.BaseService;
import com.jolly.upms.manager.vo.Pagination;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by djb
 * on 2014/11/8.
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    public abstract BaseDao<T> getDao();

    @Override
    public void save(T t) {
        this.getDao().save(t);
    }

    @Override
    public void delete(Serializable id) {
        this.getDao().delete(id);
    }

    @Override
    public void update(T t) {
        this.getDao().update(t);
    }

    @Override
    public boolean updateSelective(T t) {
        return this.getDao().updateSelective(t);
    }

    @Override
    public T get(Serializable id) {
        return this.getDao().get(id);
    }

    @Override
    public List<T> queryList(Map params) {
        return this.getDao().queryList(params);
    }

    public Pagination<T> queryList(Map params, Integer offset, Integer max) {
        return this.getDao().queryList(params, offset, max);
    }

    @Override
    public Long queryCount(Map params) {
        return getDao().queryCount(params);
    }
}
