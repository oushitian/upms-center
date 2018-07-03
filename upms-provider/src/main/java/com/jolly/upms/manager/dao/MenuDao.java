package com.jolly.upms.manager.dao;

import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.Menu;
import com.jolly.upms.manager.model.MenuDataDimension;
import com.jolly.upms.manager.vo.DataDimensionVO;

import java.util.List;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-26
 */
public interface MenuDao extends BaseDao<Menu> {
    List<Menu> queryMenusByRoleId(Integer roleId, Integer applicationId, Byte... types);

    List<Menu> queryMenusByUserId(Integer userId, Integer applicationId, Byte... types);

    List<Menu> getParentMenuByType(Byte type, Integer applicationId);

    List<Menu> queryMenusByApplicationId(Integer applicationId);

    /**
     * 查询菜单数据维度
     *
     * @return
     * @param menuIds
     */
    List<DataDimensionVO> queryMenuDataDimensions(Set<Integer> menuIds);

    List<Menu> queryMenusByMenuGroupId(Integer menuGroupId);

    void saveMenuDataDimension(MenuDataDimension menuDataDimension);

    List<Menu> queryByName(String name, Integer applicationId);

    void deleteMenuDimension(Integer menuId);

    List<Menu> queryByUrl(String url, Integer applicationId);

    List<Menu> getChildren(Integer menuId);
}
