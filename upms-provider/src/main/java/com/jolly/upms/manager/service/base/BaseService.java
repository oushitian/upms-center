package com.jolly.upms.manager.service.base;


import com.jolly.upms.manager.model.base.BaseEntity;
import com.jolly.upms.manager.vo.Pagination;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by djb
 * on 2014/11/8.
 */
public interface BaseService<T extends BaseEntity> {
    void save(T t);

    void delete(Serializable id);

    void update(T t);

    boolean updateSelective(T t);

    T get(Serializable id);

    List<T> queryList(Map params);

    Pagination<T> queryList(Map params, Integer offset, Integer max);

    Long queryCount(Map params);

}
