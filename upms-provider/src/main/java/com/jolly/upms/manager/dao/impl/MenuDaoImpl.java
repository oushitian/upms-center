package com.jolly.upms.manager.dao.impl;

import com.jolly.upms.manager.dao.MenuDao;
import com.jolly.upms.manager.dao.base.impl.BaseDaoImpl;
import com.jolly.upms.manager.model.Menu;
import com.jolly.upms.manager.model.MenuDataDimension;
import com.jolly.upms.manager.vo.DataDimensionVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-26
 */
@Repository
public class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDao {
    @Override
    public List<Menu> queryMenusByRoleId(Integer roleId, Integer applicationId, Byte... types) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("applicationId", applicationId);
        map.put("types", types);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryMenusByRoleId", map);
    }

    @Override
    public List<Menu> queryMenusByUserId(Integer userId, Integer applicationId, Byte... types) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("applicationId", applicationId);
        map.put("types", types);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryMenusByUserId", map);
    }

    @Override
    public List<Menu> getParentMenuByType(Byte type, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("applicationId", applicationId);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.getParentMenuByType", map);
    }

    @Override
    public List<Menu> queryMenusByApplicationId(Integer applicationId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryMenusByApplicationId", applicationId);
    }

    @Override
    public List<DataDimensionVO> queryMenuDataDimensions(Set<Integer> menuIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("menuIds", menuIds);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryMenuDataDimensions", map);
    }

    @Override
    public List<Menu> queryMenusByMenuGroupId(Integer menuGroupId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryMenusByMenuGroupId", menuGroupId);
    }

    @Override
    public void saveMenuDataDimension(MenuDataDimension menuDataDimension) {
        getSqlSession().insert(this.clazz.getName() + "Mapper.saveMenuDataDimension", menuDataDimension);
    }

    @Override
    public List<Menu> queryByName(String name, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("applicationId", applicationId);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryByName", map);
    }

    @Override
    public void deleteMenuDimension(Integer menuId) {
        getSqlSession().delete(this.clazz.getName() + "Mapper.deleteMenuDimension", menuId);
    }

    @Override
    public List<Menu> queryByUrl(String url, Integer applicationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        map.put("applicationId", applicationId);
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.queryByUrl", map);
    }

    @Override
    public List<Menu> getChildren(Integer menuId) {
        return getSqlSession().selectList(this.clazz.getName() + "Mapper.getChildren", menuId);
    }
}
