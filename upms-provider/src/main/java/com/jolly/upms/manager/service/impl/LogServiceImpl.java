package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.LogDao;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.Log;
import com.jolly.upms.manager.service.LogService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author chenjc
 * @since 2017-05-25
 */
@Service
@Transactional
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService {

    @Resource
    private LogDao logDao;


    @Override
    public BaseDao<Log> getDao() {
        return logDao;
    }
}
