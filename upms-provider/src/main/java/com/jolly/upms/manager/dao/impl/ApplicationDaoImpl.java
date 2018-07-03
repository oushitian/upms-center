package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.ApplicationDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.Application;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Created by dengjunbo
* on 2016-05-19 16:00:43.
*/

@Repository
public class ApplicationDaoImpl extends BaseDaoImpl<Application> implements ApplicationDao {
    @Override
    public int selectDuplicateCount(Application application) {
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.selectDuplicateCount", application);
    }

    @Override
    public int selectEditDuplicateCount(Application application) {
        return getSqlSession().selectOne(this.clazz.getName() + "Mapper.selectEditDuplicateCount", application);
    }

    @Override
    public List<Application> getApplicationByIds(List applicationIds) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.getApplicationByIds", applicationIds);
    }
}
