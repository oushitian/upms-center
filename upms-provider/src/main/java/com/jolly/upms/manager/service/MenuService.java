package com.jolly.upms.manager.service;

import com.jolly.upms.manager.model.Menu;
import com.jolly.upms.manager.service.base.BaseService;
import com.jolly.upms.manager.vo.TreeNode;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-26
 */
public interface MenuService extends BaseService<Menu> {

    /**
     * 查询用户拥有的一级和二级菜单
     *
     * @param userId
     * @param applicationId
     * @return
     */
    List<Menu> queryFirstAndSecondLevel(Integer userId, Integer applicationId);

    List<Menu> getParentMenuByType(Byte type, Integer applicationId);

    void saveOrUpdate(Menu menu, String dimensionIds);

    List<TreeNode> getMenus(Integer applicationId, Integer menuGroupId);

    List<TreeNode> getMenusGroupByApplication(Integer userId, HttpServletRequest request);

    List<TreeNode> getDimensionMenus(String menuGroupIds, Integer roleId);

    Long countByApplicationId(Integer applicationId);

    Set<String> queryUrls(Integer userId, Integer applicationId, Byte menuType, String parentMenuUrl);

    void deleteMenu(Integer menuId);
}
