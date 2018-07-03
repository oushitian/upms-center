package com.jolly.upms.manager.service;

import com.jolly.upms.manager.model.DataDimensionValues;
import com.jolly.upms.manager.service.base.BaseService;

import java.util.List;

/**
 * @author berkeley
 * @since 2017-06-19
 */
public interface DataDimensionValuesService extends BaseService<DataDimensionValues> {
     Long countDimensionValuesByDimensionId(Integer dimensionId);


    List<DataDimensionValues> queryByDimensionAttributeName(String dimensionAttributeName);
}
