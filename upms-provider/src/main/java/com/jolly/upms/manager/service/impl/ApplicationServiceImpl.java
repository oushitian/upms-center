package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.ApplicationDao;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.service.ApplicationService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjc
 * @since 2017-06-08
 */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<Application> implements ApplicationService {

    @Resource
    private ApplicationDao applicationDao;

    @Override
    public BaseDao<Application> getDao() {
        return applicationDao;
    }


    @Override
    public int selectDuplicateCount(Application application) {
        return applicationDao.selectDuplicateCount(application);
    }

    @Override
    public int selectEditDuplicateCount(Application application) {
        return applicationDao.selectEditDuplicateCount(application);
    }

    @Override
    public Map<Integer, Application> getApplicationByIds(List applicationIds) {
        Map<Integer, Application> appMap=new HashMap<>();
        if(CollectionUtils.isEmpty(applicationIds)){
            return appMap;
        }
        List<Application> appList= applicationDao.getApplicationByIds(applicationIds);

        if(CollectionUtils.isEmpty(appList)){
            return appMap;
        }
        for(Application app:appList){
            appMap.put(app.getApplicationId(),app);
        }
        return appMap;
    }
}