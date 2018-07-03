package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.DataDimensionValues;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-16
 */
public interface DataDimensionValuesDao extends BaseDao<DataDimensionValues> {
    List<DataDimensionValues> queryByDimensionAttributeName(String dimensionAttributeName);
}
