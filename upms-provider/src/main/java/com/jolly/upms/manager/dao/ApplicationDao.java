package com.jolly.upms.manager.dao;


import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.Application;

import java.util.List;

/**
* Created by dengjunbo
* on 2016-05-19 16:00:43.
*/
public interface ApplicationDao extends BaseDao<Application> {


    /**
     * 新增记录的时候 查询属性不能重复
     * @param application
     * @return
     */
    int selectDuplicateCount(Application application);

    /**
     * 修改的时候  属性不能修改成和其他记录重复
     * @param application
     * @return
     */
    int selectEditDuplicateCount(Application application);

    /**
     * 通过applicationIds批量查询应用
     * @param applicationIds
     * @return
     */
    List<Application> getApplicationByIds(List applicationIds);
}
