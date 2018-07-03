package com.jolly.upms.manager.service;

import com.jolly.upms.manager.model.Application;
import com.jolly.upms.manager.service.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author chenjc
 * @since 2017-06-08
 */
public interface ApplicationService extends BaseService<Application> {

    int selectDuplicateCount(Application application);

    int selectEditDuplicateCount(Application application);

    /**
     * 根据id列表查询应用
     * @param applicationIds
     * @return
     */
    Map<Integer,Application> getApplicationByIds(List applicationIds);

}
