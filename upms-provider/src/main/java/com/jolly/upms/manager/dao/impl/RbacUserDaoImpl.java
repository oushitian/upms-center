package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.RbacUserDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.vo.DataSourceType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenjc
 * @since 2017-07-17
 */
@Repository
public class RbacUserDaoImpl extends BaseDaoImpl<User> implements RbacUserDao {

    @Override
    public SqlSession getSqlSession() {
        return getSqlSession(DataSourceType.WHO_BRAND);
    }

    @Override
    public User queryByUserName(String userName) {
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.queryByUserName", userName);
    }

    @Override
    public Integer getMaxUserId() {
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.getMaxUserId");
    }

    @Override
    public List<Map<String, String>> querySuppliers() {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.querySuppliers");
    }

    @Override
    public boolean save(User user) {
        int result = getSqlSession(DataSourceType.WHO_BRAND_WRITE).insert(this.clazz.getName() + "Mapper.insert", user);
        return result > 0;
    }

    @Override
    public boolean updateSelective(User user) {
        int result = getSqlSession(DataSourceType.WHO_BRAND_WRITE).update(this.clazz.getName() + "Mapper.updateByPrimaryKeySelective", user);
        return result > 0;
    }
}
