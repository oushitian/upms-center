package com.jolly.upms.manager.dao.base;

import com.jolly.upms.manager.vo.Pagination;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by djb
 * on 2014/11/10.
 */
public interface BaseDao<T> {

    boolean save(T t);

    boolean saveSelective(T t);

    boolean delete(Serializable id);

    boolean update(T t);

    boolean updateSelective(T t);

    T get(Serializable id);

    List<T> queryList(Map params);

    T query(Map params);

    Pagination<T> queryList(Map params, int offset, int max);

    Long queryCount(Map params);
}
