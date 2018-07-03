package com.jolly.upms.manager.dao;


import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.model.UserMenu;
import com.jolly.upms.manager.vo.PermissionDO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dengjunbo
 * on 2016-05-19 10:51:56.
 */
public interface UserDao extends BaseDao<User> {

    User queryByUserName(String userName);

    List<PermissionDO> queryUserPermissions(Integer userId, Integer applicationId);

    List<PermissionDO> queryRolePermissions(Integer roleId, Integer applicationId);

    /**
     * 删除用户菜单关系
     *
     * @param userId
     */
    void deleteUserMenu(Integer userId);

    /**
     * 删除用户权限表
     *
     * @param userId
     */
    void deleteUserPermission(Integer userId);

    /**
     * 保存用户菜单关系
     *
     * @param userMenu
     */
    void saveUserMenu(UserMenu userMenu);

    List<User> queryByEmail(String email);


    /**
     * 修改密码
     */
    int updatePassword(Map<String, Object> map);

    /**
     * 按照userId列表查询use List
     * @param userIds
     * @return
     */
    List<User> getUserListByUserIds(List userIds);

    List<User> queryUserByRoleIds(Set<Integer> roleIds);

    void resumeOrDelSuppUser(String suppCode, int isDeleted);

    User getUser(Integer userId, Boolean includeDeleted);

    List<UserMenu> queryUserMenus(Integer menuId);

    void deleteUserMenuByApplicationId(Integer userId, Set<Integer> permitApplicationIds);

    void deleteUserPermissionByApplicationId(Integer userId, Set<Integer> permitApplicationIds);
}
