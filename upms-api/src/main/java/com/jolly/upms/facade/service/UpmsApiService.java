package com.jolly.upms.facade.service;

import com.jolly.upms.facade.dto.*;
import com.jolly.upms.facade.tag.UpmsHasPermissionDirectiveModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mars
 * @create 2017-06-22 11:03 AM
 **/
public interface UpmsApiService {

    /**
     * 获取用户一级和二级菜单
     *
     * @param requestDTO
     * @return
     */
    BaseResponseDTO<List<MenuRespDTO>> getUserMenus(BaseRequestDTO requestDTO);

    /**
     * 登出
     *
     * @param requestDTO
     * @return
     */
    BaseResponseDTO logout(BaseRequestDTO requestDTO);

    /**
     * 验证访问token有效性
     *
     * @param requestDTO
     * @return
     */
    BaseResponseDTO accessToken(BaseRequestDTO requestDTO);

    /**
     * 根据url获取用户数据权限，map的key=数据维度，value=数据维度值的集合
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<Map<String, Set<String>>> getDataPermissions(PermissionReqDTO reqDTO);

    /**
     * 批量获取用户数据权限
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<List<BatchDataPermissionRespDTO>> batchGetDataPermissions(BatchDataPermissionReqDTO reqDTO);

    /**
     * 验证用户是否有指定权限
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO validatePermission(PermissionReqDTO reqDTO);

    /**
     * 查询用户拥有的角色ID，老项目需要用到
     *
     * @param requestDTO
     * @return
     */
    BaseResponseDTO<Set<String>> findRoleIds(BaseRequestDTO requestDTO);

    /**
     * 获取用户信息和用户角色名集合
     *
     * @param requestDTO
     * @return
     */
    BaseResponseDTO<UserRespDTO> getUser(GetUserReqDTO requestDTO);

    /**
     * 根据角色ID查询用户，老项目需要用到
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<List<UserRespDTO>> queryUserByRoleIds(UserReqDTO reqDTO);

    /**
     * 根据角色ID查询用户角色关系，老项目需要用到
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<List<UserRoleRespDTO>> queryUserRoleByRoleIds(UserReqDTO reqDTO);

    /**
     * 条件查询用户，老项目需要用到
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<List<UserRespDTO>> queryUserByParams(QueryUserReqDTO reqDTO);

    /**
     * 删除或恢复供应商用户，老项目需要用到
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO resumeOrDelSuppUser(SuppUserReqDTO reqDTO);

    /**
     * 获取用户拥有的url
     *
     * @param reqDTO
     * @return url集合
     * @deprecated 此方法已不推荐使用，请使用{@link UpmsHasPermissionDirectiveModel}权限标签
     */
    @Deprecated
    BaseResponseDTO<Set<String>> getPermissions(PermReqDTO reqDTO);

    /**
     * 添加菜单到收藏夹
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO addFavorite(FavoriteReqDTO reqDTO);

    /**
     * 查询用户收藏夹的菜单
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<List<FavoriteRespDTO>> queryFavorites(BaseRequestDTO reqDTO);

    /**
     * 删除用户收藏夹的菜单
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO deleteFavorite(FavoriteReqDTO reqDTO);

    /**
     * 未登录的情况下获取用户信息
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<UserRespDTO> getUserWithoutToken(SimpleUserReqDTO reqDTO);

    /**
     * 根据数据维度属性名获取维度值集合
     *
     * @param reqDTO
     * @return
     */
    BaseResponseDTO<List<DataDimensionValuesRespDTO>> queryDataDimensionValues(DataDimensionValuesReqDTO reqDTO);

}
