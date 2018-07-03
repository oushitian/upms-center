package com.jolly.upms.manager.dao.base.impl;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.base.BaseEntity;
import com.jolly.upms.manager.vo.DataSourceType;
import com.jolly.upms.manager.vo.Pagination;
import org.apache.ibatis.session.SqlSession;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baiqi
 * on 2015/05/27.
 */
public class BaseDaoImpl<T extends BaseEntity> implements BaseDao<T> {

    protected Class<T> clazz;

    public BaseDaoImpl() {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }


    @Resource(name = "sqlSession")
    protected SqlSession sqlSession;

    @Resource(name = "whoBrandSqlSession")
    private SqlSession whoBrandSqlSession;

    @Resource(name = "whoBrandWriteSqlSession")
    private SqlSession whoBrandWriteSqlSession;

    protected DataSourceType getSqlSessionType() {
        return DataSourceType.WRITE;
    }

    public SqlSession getSqlSession(DataSourceType dataSourceType) {
        switch (dataSourceType) {
            case WRITE:
                return sqlSession;
            case WHO_BRAND:
                return whoBrandSqlSession;
            case WHO_BRAND_WRITE:
                return whoBrandWriteSqlSession;
            default:
                return sqlSession;
        }
    }

    public SqlSession getSqlSession() {
        return getSqlSession(getSqlSessionType());
    }

    /**
     * edit by baiqi
     * 增加写入结果判断
     *
     * @param t
     * @return
     */
    public boolean save(T t) {
        int result = getSqlSession(DataSourceType.WRITE).insert(this.clazz.getName() + "Mapper.insert", t);
        if (result > 0)
            return true;

        return false;
    }

    /**
     * edit by djb
     * 新增T中非null的字段
     *
     * @param t 实体
     * @return true
     */
    public boolean saveSelective(T t) {
        int result = getSqlSession(DataSourceType.WRITE).insert(this.clazz.getName() + "Mapper.insertSelective", t);
        if (result > 0)
            return true;

        return false;
    }

    /**
     * edit by baiqi
     *
     * @date 2014-11-27
     * 增加返回结果判断
     */
    public boolean delete(Serializable id) {
        int result = getSqlSession(DataSourceType.WRITE).delete(this.clazz.getName() + "Mapper.deleteByPrimaryKey", id);
        if (result > 0)
            return true;

        return false;
    }

    /**
     * edit by baiqi
     *
     * @param t
     * @date 2014-11-27
     * 增加返回结果判断
     */
    public boolean update(T t) {
        int result = getSqlSession(DataSourceType.WRITE).update(this.clazz.getName() + "Mapper.updateByPrimaryKey", t);
        if (result > 0)
            return true;

        return false;
    }

    /**
     * 仅更新T中非null的字段
     * @param t
     * @return
     */
    public boolean updateSelective(T t) {
        int result = getSqlSession(DataSourceType.WRITE).update(this.clazz.getName() + "Mapper.updateByPrimaryKeySelective", t);
        if (result > 0)
            return true;

        return false;
    }

    @Override
    public T get(Serializable id) {
        return (T) getSqlSession().selectOne(this.clazz.getName() + "Mapper.selectByPrimaryKey", id);
    }

    @Override
    public List<T> queryList(Map params) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryList", params);
    }

    @Override
    public T query(Map params) {
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.queryList", params);
    }

    @Override
    public Pagination<T> queryList(Map params, int offset, int max) {
        if (params == null) {
            params = new HashMap();
        }
        params.put("offset", offset);
        params.put("max", max);

        List<T> result = queryList(params);
        Long count = queryCount(params);

        return new Pagination<>(result, count.intValue());
    }

    public Long queryCount(Map params) {
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.queryCount", params);
    }
}
