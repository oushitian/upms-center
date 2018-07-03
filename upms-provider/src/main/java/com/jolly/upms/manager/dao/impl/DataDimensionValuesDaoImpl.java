package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.DataDimensionValuesDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.DataDimensionValues;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-16
 */
@Repository
public class DataDimensionValuesDaoImpl extends BaseDaoImpl<DataDimensionValues> implements DataDimensionValuesDao {
    @Override
    public List<DataDimensionValues> queryByDimensionAttributeName(String dimensionAttributeName) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryByDimensionAttributeName", dimensionAttributeName);
    }
}
