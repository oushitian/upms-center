package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.DataDimensionValuesDao;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.DataDimensionValues;
import com.jolly.upms.manager.service.DataDimensionValuesService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
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
public class DataDimensionValuesServiceImpl extends BaseServiceImpl<DataDimensionValues> implements DataDimensionValuesService {

    @Resource
    private DataDimensionValuesDao dataDimensionValuesDao;


    @Override
    public BaseDao<DataDimensionValues> getDao() {
        return dataDimensionValuesDao;
    }

    @Override
    public Long countDimensionValuesByDimensionId(Integer dimensionId) {
        Map params=new HashMap();
        params.put("dimensionId",dimensionId);
        return dataDimensionValuesDao.queryCount(params);
    }

    @Override
    public List<DataDimensionValues> queryByDimensionAttributeName(String dimensionAttributeName) {
        return dataDimensionValuesDao.queryByDimensionAttributeName(dimensionAttributeName);
    }
}