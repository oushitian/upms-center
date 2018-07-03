package com.jolly.upms.manager.dao;


import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by dengjunbo
 * on 2016-05-19 10:51:56.
 */
public interface RbacUserDao extends BaseDao<User> {

    User queryByUserName(String userName);

    Integer getMaxUserId();

    List<Map<String,String>> querySuppliers();
}
