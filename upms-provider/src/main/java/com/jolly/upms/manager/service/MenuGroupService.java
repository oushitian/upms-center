package com.jolly.upms.manager.service;

import com.jolly.upms.manager.model.DataDimension;
import com.jolly.upms.manager.model.MenuGroup;
import com.jolly.upms.manager.service.base.BaseService;
import com.jolly.upms.manager.vo.MenuGroupSaveVO;

import java.util.List;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public interface MenuGroupService extends BaseService<MenuGroup> {
    List<DataDimension> queryDataDimensions(Integer menuGroupId);

    void deleteMenuGroup(Integer menuGroupId);

    List<MenuGroup> getByRoleId(Integer roleId);

    void saveOrUpdate(MenuGroupSaveVO menuGroupSaveVO);
}
