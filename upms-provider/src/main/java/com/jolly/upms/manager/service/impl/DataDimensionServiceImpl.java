package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.DataDimensionDao;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.DataDimension;
import com.jolly.upms.manager.service.DataDimensionService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chenjc
 * @since 2017-06-08
 */
@Service
public class DataDimensionServiceImpl extends BaseServiceImpl<DataDimension> implements DataDimensionService {

    @Resource
    private DataDimensionDao dataDimensionDao;

    public DataDimensionDao getDataDimensionDao() {
        return dataDimensionDao;
    }

    public void setDataDimensionDao(DataDimensionDao dataDimensionDao) {
        this.dataDimensionDao = dataDimensionDao;
    }

    @Override
    public BaseDao<DataDimension> getDao() {
        return dataDimensionDao;
    }
}