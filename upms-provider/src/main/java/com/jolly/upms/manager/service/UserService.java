package com.jolly.upms.manager.service;

import com.jolly.upms.manager.model.AuthUser;
import com.jolly.upms.manager.model.User;
import com.jolly.upms.manager.service.base.BaseService;
import com.jolly.upms.manager.vo.BaseResponseVO;
import com.jolly.upms.manager.vo.PermissionVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenjc
 * @since 2017-05-25
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据用户名获取用户的权限串和数据权限
     *
     * @param userName
     * @param applicationId
     * @return
     */
    List<PermissionVO> queryPermissions(String userName, Integer applicationId);

    /**
     * 根据用户名获取用户的权限串
     *
     * @param userName
     * @param applicationId
     * @return
     */
    Set<String> queryPermissionStrings(String userName, Integer applicationId);

    /**
     * 根据用户名和权限串查询用户的数据权限
     * <p>
     * map的key存放数据维度属性名
     * map的value存放数据维度属性值集合
     *
     * @param userName
     * @param applicationId
     * @param permissionString
     * @return
     */
    Map<String, Set<String>> queryDataDimensionMap(String userName, Integer applicationId, String permissionString);

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    User queryByUserName(String userName);


    void saveUser(User user, String roleIds, HttpServletRequest request);

    /**
     * 保存用户菜单关系，以及用户权限（权限串和数据维度）
     *  @param userId
     * @param menuIds
     * @param dimensionValueDetails
     * @param request
     */
    void saveUserPermissions(Integer userId, String menuIds, String dimensionValueDetails, HttpServletRequest request);


    /**
     * 修改密码
     * @param userId       用户ID
     * @param password     原密码
     * @param newPassword  新密码
     */
    int updatePassword(Long userId, String password, String newPassword);


    /**
     * 根据用户id列表查询用户
     *
     * @param userIds
     * @return
     */
    Map<Integer,User> getUserMapByUserIds(List userIds);

    /**
     * 给用户发邮件
     * @param user
     * @param templateId
     * @param language
     */
    void sendFindPasswordMail(AuthUser user, String templateId, String language);

    void deleteUser(Integer userId);

    List<User> queryUserByRoleIds(Set<Integer> roleIds);

    void resumeOrDelSuppUser(String suppCode, int isDeleted);

    User getUser(Integer userId, Boolean includeDeleted);

    BaseResponseVO addUser(User user);

    Map<String,Object> batchDelete(MultipartFile batchDelUserFile);
}
