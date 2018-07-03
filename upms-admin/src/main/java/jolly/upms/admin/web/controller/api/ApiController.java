package jolly.upms.admin.web.controller.api;

import com.jolly.upms.facade.dto.*;
import com.jolly.upms.facade.tag.UpmsHasPermissionDirectiveModel;
import com.jolly.upms.manager.model.*;
import com.jolly.upms.manager.service.*;
import com.jolly.upms.manager.util.Constant;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.vo.AccessTokenVO;
import com.jolly.upms.manager.vo.BaseResponseVO;
import com.jolly.upms.manager.vo.UserRoleVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 * 提供对外服务接口
 *
 * @author chenjc
 * @since 2017-06-23
 */
@Controller
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Resource
    private AuthService authService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserService userService;

    @Resource
    private FavoriteService favoriteService;

    @Resource
    private DataDimensionValuesService dataDimensionValuesService;

    /**
     * 获取用户一级和二级菜单
     *
     * @param requestDTO
     * @param result
     * @return
     */
    @RequestMapping("/getUserMenus")
    @ResponseBody
    public BaseResponseDTO<List<MenuRespDTO>> getUserMenus(@Valid BaseRequestDTO requestDTO, BindingResult result) {
        List<MenuRespDTO> list = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), list);
            }
            AuthUser authUser = authService.getAuthUser(requestDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", list);
            Application application = authService.getApplication(requestDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", list);
            List<Menu> menuList = menuService.queryFirstAndSecondLevel(authUser.getUserId(), application.getApplicationId());
            List<Menu> firstList = new ArrayList<>();
            List<Menu> secondList = new ArrayList<>();
            //先分类
            for (Menu menu : menuList) {
                if (menu.getType() == 1) firstList.add(menu);
                if (menu.getType() == 2) secondList.add(menu);
            }
            for (Menu first : firstList) {
                MenuRespDTO firstNode = new MenuRespDTO();
                BeanUtils.copyProperties(first, firstNode);
                List<MenuRespDTO> children4First = new ArrayList<>();
                for (Menu second : secondList) {
                    if (second.getParentId().equals(first.getMenuId())) {
                        MenuRespDTO secondNode = new MenuRespDTO();
                        BeanUtils.copyProperties(second, secondNode);
                        secondNode.setChildren(new ArrayList<MenuRespDTO>());//不能省，否则FastJson泛型转换的时候会报错
                        children4First.add(secondNode);
                    }
                }
                firstNode.setChildren(children4First);
                list.add(firstNode);
            }
            return new BaseResponseDTO<>(list);
        } catch (Exception e) {
            logger.error("getUserMenus接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), list);
        }
    }

    /**
     * 登出
     *
     * @param requestDTO
     * @param result
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public BaseResponseDTO logout(@Valid BaseRequestDTO requestDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage());
            }
            BaseResponseVO baseResponseVO = authService.logout(requestDTO.getToken(), requestDTO.getAppKey());
            if (baseResponseVO.getResult() == Constant.STATUS_FAIL)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), baseResponseVO.getMessage());
            return new BaseResponseDTO();
        } catch (Exception e) {
            logger.error("logout接口异常", e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 验证访问token有效性
     *
     * @param requestDTO
     * @param result
     * @return
     */
    @RequestMapping("/accessToken")
    @ResponseBody
    public BaseResponseDTO accessToken(@Valid BaseRequestDTO requestDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage());
            }
            AccessTokenVO tokenVO = new AccessTokenVO();
            tokenVO.setAppKey(requestDTO.getAppKey());
            tokenVO.setToken(requestDTO.getToken());
            BaseResponseVO baseResponseVO = authService.accessToken(tokenVO);
            if (baseResponseVO.getResult() == Constant.STATUS_FAIL)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), baseResponseVO.getMessage());
            return new BaseResponseDTO();
        } catch (Exception e) {
            logger.error("accessToken接口异常", e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }


    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("/loginUI")
    public ModelAndView loginUI() {
        ModelAndView view = new ModelAndView("login");
        view.addObject("appKey", Constant.UPMS_APPKEY);
        return view;
    }

    @RequestMapping("/getDataPermissions")
    @ResponseBody
    public BaseResponseDTO<Map<String, Set<String>>> getDataPermissions(@Valid PermissionReqDTO reqDTO, BindingResult result) {
        Map<String, Set<String>> map = new HashMap<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), map);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", map);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", map);
            map = authService.getPermission(authUser, application, reqDTO.getUrl(), reqDTO.getToken());
            return new BaseResponseDTO<>(map);
        } catch (Exception e) {
            logger.error("getDataPermissions接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), map);
        }
    }

    @RequestMapping("/batchGetDataPermissions")
    @ResponseBody
    public BaseResponseDTO<List<BatchDataPermissionRespDTO>> batchGetDataPermissions(@Valid BatchDataPermissionReqDTO reqDTO, BindingResult result) {
        List<BatchDataPermissionRespDTO> list = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), list);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", list);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", list);
            Map<String, Map<String, Set<String>>> map = authService.batchGetPermissions(authUser, application, reqDTO.getUrls(), reqDTO.getToken());
            for (Map.Entry<String, Map<String, Set<String>>> entry : map.entrySet()) {
                BatchDataPermissionRespDTO respDTO = new BatchDataPermissionRespDTO();
                respDTO.setUrl(entry.getKey());
                respDTO.setDataDimensionMap(entry.getValue());
                list.add(respDTO);
            }
            return new BaseResponseDTO<>(list);
        } catch (Exception e) {
            logger.error("batchGetDataPermissions接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), list);
        }
    }

    /**
     * 验证权限
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/validatePermission")
    @ResponseBody
    public BaseResponseDTO validatePermission(@Valid PermissionReqDTO reqDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage());
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token");
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey");
            boolean b = authService.validatePermission(authUser, application, reqDTO.getUrl(), null);
            if (b) return new BaseResponseDTO(BaseResponseDTO.RespCode.SUCCESS.getCode(), "校验通过");
            return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "校验不通过");
        } catch (Exception e) {
            logger.error("validatePermission接口异常", e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 根据权限串验证权限
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/hasPermission")
    @ResponseBody
    public BaseResponseDTO hasPermission(@Valid HasPermissionReqDTO reqDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage());
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token");
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey");
            boolean b = authService.validatePermission(authUser, application, null, reqDTO.getPermissionString());
            if (b) return new BaseResponseDTO(BaseResponseDTO.RespCode.SUCCESS.getCode(), "校验通过");
            return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "校验不通过");
        } catch (Exception e) {
            logger.error("hasPermission接口异常", e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 查询用户拥有的角色ID，老项目需要用到
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/findRoleIds")
    @ResponseBody
    public BaseResponseDTO<Set<String>> findRoleIds(@Valid BaseRequestDTO reqDTO, BindingResult result) {
        Set<String> set = new HashSet<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), set);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", set);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", set);
            List<Role> roleList = roleService.queryRoles(authUser.getUserName(), application.getApplicationId());
            if (CollectionUtils.isNotEmpty(roleList)) {
                for (Role role : roleList) {
                    set.add(role.getRoleId().toString());
                }
            }
            return new BaseResponseDTO<>(set);
        } catch (Exception e) {
            logger.error("findRoleIds接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), set);
        }
    }

    /**
     * 获取用户信息
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/getUser")
    @ResponseBody
    public BaseResponseDTO<UserRespDTO> getUser(@Valid GetUserReqDTO reqDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), null);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", null);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", null);
            UserRespDTO userRespDTO = null;
            User user;
            if (reqDTO.getUserId() != null) {
                user = userService.getUser(reqDTO.getUserId(), reqDTO.getIncludeDeleted());
            } else if (StringUtils.isNotBlank(reqDTO.getUserName())) {
                user = userService.queryByUserName(reqDTO.getUserName());
            } else {
                user = userService.get(authUser.getUserId());
            }
            if (user != null) {
                userRespDTO = new UserRespDTO();
                userRespDTO.setUserId(user.getUserId());
                userRespDTO.setUserName(user.getUserName());
                userRespDTO.setIsSuppUser(user.getIsSuppUser());
                userRespDTO.setSuppCode(user.getSuppCode());
                List<String> roleNames = new ArrayList<>();
                List<Role> roleList = roleService.queryRoles(user.getUserName(), application.getApplicationId());
                for (Role role : roleList) {
                    roleNames.add(role.getName());
                }
                userRespDTO.setRoleNameList(roleNames);
            }
            return new BaseResponseDTO<>(userRespDTO);
        } catch (Exception e) {
            logger.error("getUser接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), null);
        }
    }

    /**
     * 根据角色ID查询用户，老项目需要用到
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/queryUserByRoleIds")
    @ResponseBody
    public BaseResponseDTO<List<UserRespDTO>> queryUserByRoleIds(@Valid UserReqDTO reqDTO, BindingResult result) {
        List<UserRespDTO> list = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), list);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", list);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", list);
            List<User> userList = userService.queryUserByRoleIds(reqDTO.getRoleIds());
            if (CollectionUtils.isNotEmpty(userList)) {
                for (User user : userList) {
                    UserRespDTO userRespDTO = new UserRespDTO();
                    userRespDTO.setUserId(user.getUserId());
                    userRespDTO.setUserName(user.getUserName());
                    userRespDTO.setSuppCode(user.getSuppCode());
                    list.add(userRespDTO);
                }
            }
            return new BaseResponseDTO<>(list);
        } catch (Exception e) {
            logger.error("queryUserByRoleIds接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), list);
        }
    }

    /**
     * 根据角色ID查询用户角色关系，老项目需要用到
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/queryUserRoleByRoleIds")
    @ResponseBody
    public BaseResponseDTO<List<UserRoleRespDTO>> queryUserRoleByRoleIds(@Valid UserReqDTO reqDTO, BindingResult result) {
        List<UserRoleRespDTO> list = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), list);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", list);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", list);
            List<UserRoleVO> userRoleVOS = roleService.queryUserRoles(reqDTO.getRoleIds(), reqDTO.getUserId());
            if (CollectionUtils.isNotEmpty(userRoleVOS)) {
                for (UserRoleVO userRoleVO : userRoleVOS) {
                    UserRoleRespDTO userRoleRespDTO = new UserRoleRespDTO();
                    userRoleRespDTO.setRoleId(userRoleVO.getRoleId());
                    userRoleRespDTO.setUserId(userRoleVO.getUserId());
                    userRoleRespDTO.setUserName(userRoleVO.getUserName());
                    list.add(userRoleRespDTO);
                }
            }
            return new BaseResponseDTO<>(list);
        } catch (Exception e) {
            logger.error("queryUserRoleByRoleIds接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), list);
        }
    }

    /**
     * 条件查询用户，老项目需要用到
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/queryUserByParams")
    @ResponseBody
    public BaseResponseDTO<List<UserRespDTO>> queryUserByParams(@Valid QueryUserReqDTO reqDTO, BindingResult result) {
        List<UserRespDTO> list = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), list);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", list);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", list);
            Map params = new HashMap();
            if (StringUtils.isNotBlank(reqDTO.getUserName())) {
                params.put("userName", reqDTO.getUserName());
            }
            List<User> userList = userService.queryList(params);
            if (CollectionUtils.isNotEmpty(userList)) {
                for (User user : userList) {
                    UserRespDTO userRespDTO = new UserRespDTO();
                    userRespDTO.setUserId(user.getUserId());
                    userRespDTO.setUserName(user.getUserName());
                    list.add(userRespDTO);
                }
            }
            return new BaseResponseDTO<>(list);
        } catch (Exception e) {
            logger.error("queryUserByParams接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), list);
        }
    }

    /**
     * 删除或恢复供应商用户，老项目需要用到
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/resumeOrDelSuppUser")
    @ResponseBody
    public BaseResponseDTO resumeOrDelSuppUser(@Valid SuppUserReqDTO reqDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage());
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token");
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey");
            int isDeleted = 0;
            if (reqDTO.getDeleteUser()) isDeleted = 1;
            userService.resumeOrDelSuppUser(reqDTO.getSuppCode(), isDeleted);
            return new BaseResponseDTO();
        } catch (Exception e) {
            logger.error("resumeOrDelSuppUser接口异常", e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 获取用户拥有的url
     *
     * @param reqDTO
     * @return url集合
     * @deprecated 此方法已不推荐使用，请使用{@link UpmsHasPermissionDirectiveModel}权限标签
     */
    @RequestMapping("/getPermissions")
    @ResponseBody
    @Deprecated
    public BaseResponseDTO<Set<String>> getPermissions(@Valid PermReqDTO reqDTO, BindingResult result) {
        Set<String> set = new HashSet<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), set);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", set);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", set);
            set = menuService.queryUrls(authUser.getUserId(), application.getApplicationId(), reqDTO.getMenuType(), reqDTO.getParentMenuUrl());
            return new BaseResponseDTO<>(set);
        } catch (Exception e) {
            logger.error("getPermissions接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), set);
        }
    }

    /**
     * 添加菜单到收藏夹
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/addFavorite")
    @ResponseBody
    public BaseResponseDTO addFavorite(@Valid FavoriteReqDTO reqDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage());
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token");
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey");
            Set<Integer> menuIds = reqDTO.getMenuIds();
            for (Integer menuId : menuIds) {
                Favorite favorite = new Favorite();
                favorite.setUserId(authUser.getUserId());
                favorite.setMenuId(menuId);
                favorite.setApplicationId(application.getApplicationId());
                favorite.setIsDeleted((byte) 0);
                favorite.setGmtCreated(DateUtils.getCurrentSecondIntValue());
                favorite.setGmtModified(DateUtils.getCurrentSecondIntValue());
                favoriteService.save(favorite);
            }
            return new BaseResponseDTO();
        } catch (Exception e) {
            logger.error("addFavorite接口异常", e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }


    /**
     * 查询用户收藏夹的菜单
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/queryFavorites")
    @ResponseBody
    public BaseResponseDTO<List<FavoriteRespDTO>> queryFavorites(@Valid BaseRequestDTO reqDTO, BindingResult result) {
        List<FavoriteRespDTO> list = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), list);
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token", list);
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", list);
            Map params = new HashMap();
            params.put("userId", authUser.getUserId());
            params.put("applicationId", application.getApplicationId());
            List<Favorite> favorites = favoriteService.queryList(params);
            if (!favorites.isEmpty()) {
                //查询用户拥有权限的一级和二级菜单
                List<Menu> menuList = menuService.queryFirstAndSecondLevel(authUser.getUserId(), application.getApplicationId());
                Set<Integer> menuIdSet = new HashSet<>();
                for (Menu menu : menuList) {
                    menuIdSet.add(menu.getMenuId());
                }
                for (Favorite favorite : favorites) {
                    Integer menuId = favorite.getMenuId();
                    Menu menu = menuService.get(menuId);
                    if (menu == null) {
                        continue;
                    }
                    if (!menu.getApplicationId().equals(application.getApplicationId())) {
                        continue;
                    }
                    if (!menuIdSet.contains(menuId)) {
                        continue;
                    }
                    FavoriteRespDTO favoriteRespDTO = new FavoriteRespDTO();
                    favoriteRespDTO.setMenuId(menuId);
                    favoriteRespDTO.setName(menu.getName());
                    favoriteRespDTO.setUrl(menu.getUrl());
                    favoriteRespDTO.setPermissionString(menu.getPermissionString());
                    favoriteRespDTO.setGmtCreated(favorite.getGmtCreated());
                    list.add(favoriteRespDTO);
                }
            }
            //按创建时间降序排列
            Collections.sort(list, new Comparator<FavoriteRespDTO>() {
                @Override
                public int compare(FavoriteRespDTO o1, FavoriteRespDTO o2) {
                    return o2.getGmtCreated() - o1.getGmtCreated();
                }
            });
            return new BaseResponseDTO<>(list);
        } catch (Exception e) {
            logger.error("queryFavorites接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), list);
        }
    }

    /**
     * 删除用户收藏夹的菜单
     *
     * @param reqDTO
     * @param result
     * @return
     */
    @RequestMapping("/deleteFavorite")
    @ResponseBody
    public BaseResponseDTO deleteFavorite(@Valid FavoriteReqDTO reqDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage());
            }
            AuthUser authUser = authService.getAuthUser(reqDTO.getToken());
            if (authUser == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.INVALID_TOKEN.getCode(), "invalid token");
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey");
            Set<Integer> menuIds = reqDTO.getMenuIds();
            for (Integer menuId : menuIds) {
                favoriteService.deleteFavorite(menuId, authUser.getUserId(), application.getApplicationId());
            }
            return new BaseResponseDTO();
        } catch (Exception e) {
            logger.error("deleteFavorite接口异常", e);
            return new BaseResponseDTO(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage());
        }
    }

    /**
     * 未登录的情况下获取用户信息
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/getUserWithoutToken")
    @ResponseBody
    public BaseResponseDTO<UserRespDTO> getUserWithoutToken(@Valid SimpleUserReqDTO reqDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), null);
            }
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", null);
            UserRespDTO userRespDTO = null;
            User user = userService.getUser(reqDTO.getUserId(), true);
            if (user != null) {
                userRespDTO = new UserRespDTO();
                userRespDTO.setUserId(user.getUserId());
                userRespDTO.setUserName(user.getUserName());
                userRespDTO.setIsSuppUser(user.getIsSuppUser());
                userRespDTO.setSuppCode(user.getSuppCode());
            }
            return new BaseResponseDTO<>(userRespDTO);
        } catch (Exception e) {
            logger.error("getUserWithoutToken接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), null);
        }
    }

    /**
     * 根据数据维度属性名获取维度值集合
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/queryDataDimensionValues")
    @ResponseBody
    public BaseResponseDTO<List<DataDimensionValuesRespDTO>> queryDataDimensionValues(@Valid DataDimensionValuesReqDTO reqDTO, BindingResult result) {
        List<DataDimensionValuesRespDTO> list = new ArrayList<>();
        try {
            if (result.hasErrors()) {
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), result.getAllErrors().get(0).getDefaultMessage(), list);
            }
            Application application = authService.getApplication(reqDTO.getAppKey());
            if (application == null)
                return new BaseResponseDTO<>(BaseResponseDTO.RespCode.FAILURE.getCode(), "invalid appKey", list);
            List<DataDimensionValues> dataDimensionValuesList = dataDimensionValuesService.queryByDimensionAttributeName(reqDTO.getDimensionAttributeName());
            for (DataDimensionValues dataDimensionValues : dataDimensionValuesList) {
                DataDimensionValuesRespDTO respDTO = new DataDimensionValuesRespDTO();
                respDTO.setDisplayName(dataDimensionValues.getDisplayName());
                respDTO.setAttributeValue(dataDimensionValues.getAttributeValue());
                list.add(respDTO);
            }
            return new BaseResponseDTO<>(list);
        } catch (Exception e) {
            logger.error("queryDataDimensionValues接口异常", e);
            return new BaseResponseDTO<>(BaseResponseDTO.RespCode.EXCEPTION.getCode(), e.getMessage(), list);
        }
    }

}
