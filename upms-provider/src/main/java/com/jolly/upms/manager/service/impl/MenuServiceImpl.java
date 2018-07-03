package com.jolly.upms.manager.service.impl;

import com.jolly.upms.manager.dao.*;
import com.jolly.upms.manager.dao.base.BaseDao;
import com.jolly.upms.manager.model.*;
import com.jolly.upms.manager.service.AuthService;
import com.jolly.upms.manager.service.MenuService;
import com.jolly.upms.manager.service.base.impl.BaseServiceImpl;
import com.jolly.upms.manager.util.Assert;
import com.jolly.upms.manager.util.DateUtils;
import com.jolly.upms.manager.util.ShiroUtils;
import com.jolly.upms.manager.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author chenjc
 * @since 2017-05-26
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

    @Resource
    private MenuDao menuDao;

    @Resource
    private RoleDao roleDao;

    @Resource
    private ApplicationDao applicationDao;

    @Resource
    private DataDimensionValuesDao dataDimensionValuesDao;

    @Resource
    private UserPermissionDao userPermissionDao;

    @Resource
    private MenuGroupDao menuGroupDao;

    @Resource
    private UserDao userDao;

    @Resource
    private MenuGroupDetailDao menuGroupDetailDao;

    @Resource
    private AuthService authService;

    @Override
    public BaseDao<Menu> getDao() {
        return menuDao;
    }

    @Override
    public List<Menu> queryFirstAndSecondLevel(Integer userId, Integer applicationId) {
        Set<Menu> set = new HashSet<>();//去重使用
        List<Menu> userMenus = menuDao.queryMenusByUserId(userId, applicationId, new Byte[]{1, 2});
        set.addAll(userMenus);
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("applicationId", applicationId);
        List<Role> roles = roleDao.queryListByUserId(map);
        for (Role role : roles) {
            List<Menu> roleMenus = menuDao.queryMenusByRoleId(role.getRoleId(), applicationId, new Byte[]{1, 2});
            set.addAll(roleMenus);
        }
        List<Menu> menuList = new ArrayList<>(set);
        //排序，按displayOrder升序排列
        Collections.sort(menuList, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                return o1.getDisplayOrder() - o2.getDisplayOrder();
            }
        });
        return menuList;
    }

    @Override
    public List<Menu> getParentMenuByType(Byte type, Integer applicationId) {
        return menuDao.getParentMenuByType(type, applicationId);
    }

    @Override
    public void saveOrUpdate(Menu menu, String dimensionIds) {
        //是否跨域访问
        boolean isCrossDomain = false;
        if (StringUtils.isBlank(menu.getUrl())) {
            menu.setUrl("/");
        } else {
            String url = menu.getUrl();
            if (url.toLowerCase().startsWith("http")) {
                isCrossDomain = true;
            } else if (!url.startsWith("/")) {
                menu.setUrl("/" + url);
            }
        }
        if (!isCrossDomain) {
            menu.setPermissionString(ShiroUtils.convertPathToPermissionString(menu.getUrl()));
        } else {
            menu.setPermissionString("");
        }
        if (menu.getType() == 1) {
            menu.setParentId(0);
        }
        Integer menuId = menu.getMenuId();
        if (menuId == null) {
            List<Menu> menuList = menuDao.queryByName(menu.getName(), menu.getApplicationId());
            for (Menu m : menuList) {
                if (m.getType().equals(menu.getType()) && m.getParentId().equals(menu.getParentId())) {
                    throw new RuntimeException("资源名已存在");
                }
            }
            if (menu.getType() != 1) {
                menuList = menuDao.queryByUrl(menu.getUrl(), menu.getApplicationId());
                for (Menu m : menuList) {
                    if (m.getType().equals(menu.getType()) && m.getParentId().equals(menu.getParentId())) {
                        throw new RuntimeException("链接已存在");
                    }
                }
            }
            menu.setGmtCreated(DateUtils.getCurrentSecondIntValue());
            menu.setGmtModified(DateUtils.getCurrentSecondIntValue());
            menuDao.saveSelective(menu);
        } else {
            List<Menu> menuList = menuDao.queryByName(menu.getName(), menu.getApplicationId());
            for (Menu m : menuList) {
                if ((!m.getMenuId().equals(menuId)) && m.getType().equals(menu.getType()) && m.getParentId().equals(menu.getParentId())) {
                    throw new RuntimeException("资源名已存在");
                }
            }
            if (menu.getType() != 1) {
                menuList = menuDao.queryByUrl(menu.getUrl(), menu.getApplicationId());
                for (Menu m : menuList) {
                    if ((!m.getMenuId().equals(menuId)) && m.getType().equals(menu.getType()) && m.getParentId().equals(menu.getParentId())) {
                        throw new RuntimeException("链接已存在");
                    }
                }
            }
            menu.setGmtModified(DateUtils.getCurrentSecondIntValue());
            menuDao.updateSelective(menu);
            //删除菜单数据维度关系
            menuDao.deleteMenuDimension(menuId);
        }
        //保存菜单数据维度关系
        if (menu.getType() == 3 && StringUtils.isNotBlank(dimensionIds)) {
            String[] arr = StringUtils.split(dimensionIds, ",");
            for (String str : arr) {
                MenuDataDimension menuDataDimension = new MenuDataDimension();
                menuDataDimension.setMenuId(menu.getMenuId());
                menuDataDimension.setDimensionId(Integer.valueOf(str));
                menuDao.saveMenuDataDimension(menuDataDimension);
            }
        }
    }

    @Override
    public List<TreeNode> getMenus(Integer applicationId, Integer menuGroupId) {
        List<Integer> menuIds = new ArrayList<>();
        //查询权限组所拥有的菜单
        if (menuGroupId != null) {
            List<Menu> list = menuDao.queryMenusByMenuGroupId(menuGroupId);
            for (Menu menu : list) {
                menuIds.add(menu.getMenuId());
            }
        }
        List<TreeNode> list = new ArrayList<>();
        List<Menu> menuList = menuDao.queryMenusByApplicationId(applicationId);
        List<Menu> firstList = new ArrayList<>();
        List<Menu> secondList = new ArrayList<>();
        List<Menu> thirdList = new ArrayList<>();
        //先分类
        for (Menu menu : menuList) {
            if (menu.getType() == 1) firstList.add(menu);
            if (menu.getType() == 2) secondList.add(menu);
            if (menu.getType() == 3) thirdList.add(menu);
        }
        for (Menu first : firstList) {
            TreeNode firstNode = new TreeNode();
            firstNode.setId(first.getMenuId());
            firstNode.setText(first.getName());
            List<TreeNode> children4First = new ArrayList<>();
            for (Menu second : secondList) {
                if (second.getParentId().equals(first.getMenuId())) {
                    TreeNode secondNode = new TreeNode();
                    secondNode.setId(second.getMenuId());
                    secondNode.setText(second.getName());
                    List<TreeNode> children4Second = new ArrayList<>();
                    for (Menu third : thirdList) {
                        if (third.getParentId().equals(second.getMenuId())) {
                            TreeNode thirdNode = new TreeNode();
                            thirdNode.setId(third.getMenuId());
                            thirdNode.setText(third.getName());
                            thirdNode.setChecked(menuIds.contains(third.getMenuId()));//权限组编辑时的回显
                            thirdNode.setChildren(new ArrayList<TreeNode>());
                            children4Second.add(thirdNode);
                        }
                    }
                    //没有孩子节点，说明是叶子节点
                    if (children4Second.isEmpty())
                        secondNode.setChecked(menuIds.contains(second.getMenuId()));//权限组编辑时的回显
                    else
                        secondNode.setState("closed");//非叶子节点不展开
                    secondNode.setChildren(children4Second);
                    children4First.add(secondNode);
                }
            }
            //没有孩子节点，说明是叶子节点
            if (children4First.isEmpty())
                firstNode.setChecked(menuIds.contains(first.getMenuId()));//权限组编辑时的回显
            else
                firstNode.setState("closed");//非叶子节点不展开
            firstNode.setChildren(children4First);
            list.add(firstNode);
        }
        return list;
    }

    @Override
    public List<TreeNode> getMenusGroupByApplication(Integer userId, HttpServletRequest request) {
        List<Menu> userMenus = menuDao.queryMenusByUserId(userId, null, null);//用户拥有的菜单
        List<Integer> userMenuIds = new ArrayList<>();
        for (Menu userMenu : userMenus) {
            userMenuIds.add(userMenu.getMenuId());
        }
        Map paramMap = new HashMap();
        paramMap.put("userId", userId);
        List<UserPermission> userPermissionList = userPermissionDao.queryList(paramMap);//查询用户权限表
        //存放有分配数据维度值的权限
        List<PermissionVO4User> permissionVO4UserList = new ArrayList<>();
        for (UserPermission userPermission : userPermissionList) {
            String attributeName = userPermission.getAttributeName();
            if (StringUtils.isNotBlank(attributeName)) {
                PermissionVO4User permissionVO4User = new PermissionVO4User(userPermission.getApplicationId(), userPermission.getPermissionString());
                if (permissionVO4UserList.contains(permissionVO4User)) {//如果permissionVO4UserList已经包含该权限串
                    String[] values = StringUtils.split(userPermission.getAttributeValues(), ",");
                    Set<String> set = new HashSet<>(Arrays.asList(values));
                    PermissionVO4User existVO = permissionVO4UserList.get(permissionVO4UserList.indexOf(permissionVO4User));//拿到已经存在的permissionVO4User
                    Map<String, Set<String>> dataDimensionMap = existVO.getDataDimensionMap();
                    Set<String> existValues = dataDimensionMap.get(attributeName);
                    if (existValues == null) {//没有找到相同的数据维度，就新增一个数据维度
                        dataDimensionMap.put(attributeName, set);
                    } else {//找到了相同的数据维度，就合并数据维度值
                        existValues.addAll(set);
                    }
                } else {
                    String[] values = StringUtils.split(userPermission.getAttributeValues(), ",");
                    Set<String> set = new HashSet<>(Arrays.asList(values));
                    Map<String, Set<String>> dataDimensionMap = new HashMap<>();
                    dataDimensionMap.put(attributeName, set);
                    permissionVO4User.setDataDimensionMap(dataDimensionMap);
                    permissionVO4UserList.add(permissionVO4User);
                }
            }
        }
        //////////////////////////////////////////////////
        List<TreeNode> nodeList = new ArrayList<>();
        Map<Integer, List<TreeNode>> map = new HashMap<>();//按应用系统将菜单分组，key=应用系统ID，value=一级菜单集合
        List<Menu> menuList = menuDao.queryList(null);
        List<DataDimensionVO> dataDimensionVOList = menuDao.queryMenuDataDimensions(null);//查询菜单数据维度
        Map<Integer, List<DataDimensionVO>> dataMap = new HashMap<>();
        for (DataDimensionVO dataDimensionVO : dataDimensionVOList) {
            Integer menuId = dataDimensionVO.getMenuId();
            List<DataDimensionVO> ddList = dataMap.get(menuId);
            if (ddList == null) {
                ddList = new ArrayList<>();
                ddList.add(dataDimensionVO);
                dataMap.put(menuId, ddList);
            } else {
                ddList.add(dataDimensionVO);
            }
        }
        List<Menu> firstList = new ArrayList<>();
        List<Menu> secondList = new ArrayList<>();
        List<Menu> thirdList = new ArrayList<>();
        //先分类
        for (Menu menu : menuList) {
            if (menu.getType() == 1) firstList.add(menu);
            if (menu.getType() == 2) secondList.add(menu);
            if (menu.getType() == 3) thirdList.add(menu);
        }
        for (Menu first : firstList) {
            TreeNode firstNode = new TreeNode();
            firstNode.setId(first.getMenuId());
            firstNode.setText(first.getName());
            List<TreeNode> children4First = new ArrayList<>();
            for (Menu second : secondList) {
                if (second.getParentId().equals(first.getMenuId())) {
                    TreeNode secondNode = new TreeNode();
                    secondNode.setId(second.getMenuId());
                    secondNode.setText(second.getName());
                    List<TreeNode> children4Second = new ArrayList<>();
                    for (Menu third : thirdList) {
                        if (third.getParentId().equals(second.getMenuId())) {
                            TreeNode thirdNode = new TreeNode();
                            thirdNode.setId(third.getMenuId());
                            thirdNode.setText(third.getName());
                            ArrayList<TreeNode> children4Third = new ArrayList<>();
                            //将菜单下的数据维度组装成菜单的节点
                            if (dataMap.get(third.getMenuId()) != null) {
                                List<DataDimensionVO> dataDimensionVOS = dataMap.get(third.getMenuId());
                                for (DataDimensionVO dataDimensionVO : dataDimensionVOS) {
                                    TreeNode fourthNode = new TreeNode();
                                    fourthNode.setId(dataDimensionVO.getDimensionId());
                                    fourthNode.setText(dataDimensionVO.getDisplayName());
                                    fourthNode.setNodeType(NodeType.DIMENSION.getValue());//标记为数据维度
                                    List<TreeNode> children4Fourth = new ArrayList<>();
                                    //查询数据维度值，拼装成数据维度的节点
                                    Map params = new HashMap();
                                    params.put("dimensionId", dataDimensionVO.getDimensionId());
                                    List<DataDimensionValues> dataDimensionValuesList = dataDimensionValuesDao.queryList(params);
                                    for (DataDimensionValues dataDimensionValues : dataDimensionValuesList) {
                                        TreeNode fifthNode = new TreeNode();
                                        fifthNode.setId(dataDimensionValues.getRecId());
                                        fifthNode.setText(dataDimensionValues.getDisplayName());
                                        for (PermissionVO4User permissionVO4User : permissionVO4UserList) {
                                            //确定唯一菜单
                                            if (third.getApplicationId().equals(permissionVO4User.getApplicationId()) && third.getPermissionString().equals(permissionVO4User.getPermissionString())) {
                                                Map<String, Set<String>> dataDimensionMap = permissionVO4User.getDataDimensionMap();
                                                for (Map.Entry<String, Set<String>> mapEntry : dataDimensionMap.entrySet()) {
                                                    if (dataDimensionVO.getAttributeName().equals(mapEntry.getKey())) {
                                                        fifthNode.setChecked(mapEntry.getValue().contains(dataDimensionValues.getAttributeValue()));//回显已选择的数据维度值
                                                        break;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        fifthNode.setNodeType(NodeType.DIMENSION_VALUE.getValue());//标记为数据维度值
                                        //填充数据维度值详细
                                        fifthNode.setDimensionValueDetail(third.getMenuId() + "," + dataDimensionVO.getAttributeName() + "," + dataDimensionValues.getAttributeValue());
                                        fifthNode.setChildren(new ArrayList<TreeNode>());
                                        children4Fourth.add(fifthNode);
                                    }
                                    fourthNode.setChildren(children4Fourth);
                                    children4Third.add(fourthNode);
                                }
                            }
                            //没有孩子节点，说明是叶子节点
                            if (children4Third.isEmpty())
                                thirdNode.setChecked(userMenuIds.contains(third.getMenuId()));
                            else
                                thirdNode.setState("closed");//非叶子节点不展开
                            thirdNode.setChildren(children4Third);
                            children4Second.add(thirdNode);
                        }
                    }
                    //没有孩子节点，说明是叶子节点
                    if (children4Second.isEmpty())
                        secondNode.setChecked(userMenuIds.contains(second.getMenuId()));
                    else
                        secondNode.setState("closed");//非叶子节点不展开
                    secondNode.setChildren(children4Second);
                    children4First.add(secondNode);
                }
            }
            //没有孩子节点，说明是叶子节点
            if (children4First.isEmpty())
                firstNode.setChecked(userMenuIds.contains(first.getMenuId()));
            else
                firstNode.setState("closed");//非叶子节点不展开
            firstNode.setChildren(children4First);
            //将firstNode按应用分组
            Integer applicationId = first.getApplicationId();
            List<TreeNode> list = map.get(applicationId);
            if (list == null) {
                list = new ArrayList<>();
                list.add(firstNode);
                map.put(applicationId, list);
            } else {
                list.add(firstNode);
            }
        }

        //当前用户可访问的应用
        Set<Integer> permitApplicationIds = authService.getRolePermitApplicationIds(request);
        for (Map.Entry<Integer, List<TreeNode>> entry : map.entrySet()) {
            Integer appId = entry.getKey();
            if (!permitApplicationIds.contains(appId)) {
                continue;
            }
            TreeNode node1 = new TreeNode();
            node1.setId(appId);
            node1.setText(applicationDao.get(appId).getAppName());
            node1.setNodeType(NodeType.APPLICATION.getValue());//标记为应用系统
            node1.setChildren(entry.getValue());
            node1.setState("closed");//不展开顶级节点
            nodeList.add(node1);
        }
        return nodeList;
    }

    @Override
    public List<TreeNode> getDimensionMenus(String menuGroupIds, Integer roleId) {
        //存放有分配数据维度值的权限
        List<PermissionVO> permissionVOList = new ArrayList<>();
        if (roleId != null) {
            List<PermissionDO> rolePermissionList = userDao.queryRolePermissions(roleId, null);//查询角色权限表
            for (PermissionDO permissionDO : rolePermissionList) {
                String attributeName = permissionDO.getAttributeName();
                if (StringUtils.isNotBlank(attributeName)) {
                    PermissionVO permissionVO = new PermissionVO(permissionDO.getPermissionString());
                    if (permissionVOList.contains(permissionVO)) {//如果permissionVOList已经包含该权限串
                        String[] values = StringUtils.split(permissionDO.getAttributeValues(), ",");
                        Set<String> set = new HashSet<>(Arrays.asList(values));
                        PermissionVO existVO = permissionVOList.get(permissionVOList.indexOf(permissionVO));//拿到已经存在的PermissionVO
                        Map<String, Set<String>> dataDimensionMap = existVO.getDataDimensionMap();
                        Set<String> existValues = dataDimensionMap.get(attributeName);
                        if (existValues == null) {//没有找到相同的数据维度，就新增一个数据维度
                            dataDimensionMap.put(attributeName, set);
                        } else {//找到了相同的数据维度，就合并数据维度值
                            existValues.addAll(set);
                        }
                    } else {
                        String[] values = StringUtils.split(permissionDO.getAttributeValues(), ",");
                        Set<String> set = new HashSet<>(Arrays.asList(values));
                        Map<String, Set<String>> dataDimensionMap = new HashMap<>();
                        dataDimensionMap.put(attributeName, set);
                        permissionVO.setDataDimensionMap(dataDimensionMap);
                        permissionVOList.add(permissionVO);
                    }
                }
            }
        }

        //////////////////////////////////////////////////
        Map<Integer, List<Menu>> map = new HashMap<>();//按权限组将菜单分组，key=权限组ID，value=菜单集合
        Set<Integer> menuIds = new HashSet<>();//存储菜单ID
        String[] menuGroupIdArr = StringUtils.split(menuGroupIds, ",");
        for (String menuGroupId : menuGroupIdArr) {
            List<Menu> list = menuDao.queryMenusByMenuGroupId(Integer.valueOf(menuGroupId));
            for (Menu menu : list) {
                menuIds.add(menu.getMenuId());
            }
            map.put(Integer.valueOf(menuGroupId), list);
        }
        if (menuIds.isEmpty()) return new ArrayList<>();
        List<DataDimensionVO> dataDimensionVOList = menuDao.queryMenuDataDimensions(menuIds);//查询菜单数据维度
        Map<Integer, List<DataDimensionVO>> dataMap = new HashMap<>();
        for (DataDimensionVO dataDimensionVO : dataDimensionVOList) {
            Integer menuId = dataDimensionVO.getMenuId();
            List<DataDimensionVO> ddList = dataMap.get(menuId);
            if (ddList == null) {
                ddList = new ArrayList<>();
                ddList.add(dataDimensionVO);
                dataMap.put(menuId, ddList);
            } else {
                ddList.add(dataDimensionVO);
            }
        }

        List<TreeNode> nodeList = new ArrayList<>();
        for (Map.Entry<Integer, List<Menu>> entry : map.entrySet()) {
            Integer menuGroupId = entry.getKey();
            List<Menu> menuList = entry.getValue();
            TreeNode topNode = new TreeNode();
            topNode.setId(menuGroupId);
            topNode.setText(menuGroupDao.get(menuGroupId).getName());
            topNode.setNodeType(NodeType.MENU_GROUP.getValue());//设置节点类型为菜单组
            List<TreeNode> children4Top = new ArrayList<>();
            List<Menu> firstList = new ArrayList<>();
            List<Menu> secondList = new ArrayList<>();
            List<Menu> thirdList = new ArrayList<>();
            //先分类
            for (Menu menu : menuList) {
                if (menu.getType() == 1) firstList.add(menu);
                if (menu.getType() == 2) secondList.add(menu);
                if (menu.getType() == 3) thirdList.add(menu);
            }
            for (Menu first : firstList) {
                TreeNode firstNode = new TreeNode();
                firstNode.setId(first.getMenuId());
                firstNode.setText(first.getName());
                List<TreeNode> children4First = new ArrayList<>();
                for (Menu second : secondList) {
                    if (second.getParentId().equals(first.getMenuId())) {
                        TreeNode secondNode = new TreeNode();
                        secondNode.setId(second.getMenuId());
                        secondNode.setText(second.getName());
                        List<TreeNode> children4Second = new ArrayList<>();
                        for (Menu third : thirdList) {
                            if (third.getParentId().equals(second.getMenuId())) {
                                TreeNode thirdNode = new TreeNode();
                                thirdNode.setId(third.getMenuId());
                                thirdNode.setText(third.getName());
                                ArrayList<TreeNode> children4Third = new ArrayList<>();
                                //将菜单下的数据维度组装成菜单的节点
                                if (dataMap.get(third.getMenuId()) != null) {
                                    List<DataDimensionVO> dataDimensionVOS = dataMap.get(third.getMenuId());
                                    for (DataDimensionVO dataDimensionVO : dataDimensionVOS) {
                                        TreeNode fourthNode = new TreeNode();
                                        fourthNode.setId(dataDimensionVO.getDimensionId());
                                        fourthNode.setText(dataDimensionVO.getDisplayName());
                                        fourthNode.setNodeType(NodeType.DIMENSION.getValue());//标记为数据维度
                                        List<TreeNode> children4Fourth = new ArrayList<>();
                                        //查询数据维度值，拼装成数据维度的节点
                                        Map params = new HashMap();
                                        params.put("dimensionId", dataDimensionVO.getDimensionId());
                                        List<DataDimensionValues> dataDimensionValuesList = dataDimensionValuesDao.queryList(params);
                                        for (DataDimensionValues dataDimensionValues : dataDimensionValuesList) {
                                            TreeNode fifthNode = new TreeNode();
                                            fifthNode.setId(dataDimensionValues.getRecId());
                                            fifthNode.setText(dataDimensionValues.getDisplayName());
                                            for (PermissionVO permissionVO : permissionVOList) {
                                                //数据维度值回显
                                                if (third.getPermissionString().equals(permissionVO.getPermissionString())) {
                                                    Map<String, Set<String>> dataDimensionMap = permissionVO.getDataDimensionMap();
                                                    for (Map.Entry<String, Set<String>> mapEntry : dataDimensionMap.entrySet()) {
                                                        if (dataDimensionVO.getAttributeName().equals(mapEntry.getKey())) {
                                                            fifthNode.setChecked(mapEntry.getValue().contains(dataDimensionValues.getAttributeValue()));//回显已选择的数据维度值
                                                            break;
                                                        }
                                                    }
                                                    break;
                                                }
                                            }
                                            fifthNode.setNodeType(NodeType.DIMENSION_VALUE.getValue());//标记为数据维度值
                                            //填充数据维度值详细
                                            fifthNode.setDimensionValueDetail(third.getMenuId() + "," + dataDimensionVO.getAttributeName() + "," + dataDimensionValues.getAttributeValue());
                                            fifthNode.setChildren(new ArrayList<TreeNode>());
                                            children4Fourth.add(fifthNode);
                                        }
                                        fourthNode.setChildren(children4Fourth);
                                        //不展开数据维度节点
                                        fourthNode.setState("closed");
                                        children4Third.add(fourthNode);
                                    }
                                }
                                //只有含有数据维度的三级菜单，才会被添加
                                if (!children4Third.isEmpty()) {
                                    thirdNode.setChildren(children4Third);
                                    children4Second.add(thirdNode);
                                }
                            }
                        }
                        //二级菜单下有三级菜单，可以推算出三级菜单必有数据维度节点
                        if (!children4Second.isEmpty()) {
                            secondNode.setChildren(children4Second);
                            children4First.add(secondNode);
                        }
                    }
                }
                //同理
                if (!children4First.isEmpty()) {
                    firstNode.setChildren(children4First);
                    children4Top.add(firstNode);
                }
            }
            //同理
            if (!children4Top.isEmpty()) {
                topNode.setChildren(children4Top);
                nodeList.add(topNode);
            }
        }
        return nodeList;
    }

    @Override
    public Long countByApplicationId(Integer applicationId) {
        Assert.notNull(applicationId, "countByApplicationId@MenuServiceImpl:applicationId不能为NULL");
        HashMap map = new HashMap();
        map.put("applicationId", applicationId);
        return menuDao.queryCount(map);
    }

    @Override
    public Set<String> queryUrls(Integer userId, Integer applicationId, Byte menuType, String parentMenuUrl) {
        Set<String> urls = new HashSet<>();
        Set<Integer> parentMenuIds = new HashSet<>();
        if (StringUtils.isNotBlank(parentMenuUrl)) {
            List<Menu> menuList = menuDao.queryByUrl(parentMenuUrl, applicationId);
            if (menuList.isEmpty()) {
                return urls;
            }
            for (Menu menu : menuList) {
                parentMenuIds.add(menu.getMenuId());
            }
        }
        Set<Menu> set = new HashSet<>();
        Byte[] type;
        if (menuType == null) {
            type = new Byte[]{1, 2, 3};
        } else {
            type = new Byte[]{menuType};
        }
        List<Menu> userMenus = menuDao.queryMenusByUserId(userId, applicationId, type);
        set.addAll(userMenus);
        Map map = new HashMap();
        map.put("userId", userId);
        map.put("applicationId", applicationId);
        List<Role> roles = roleDao.queryListByUserId(map);
        for (Role role : roles) {
            List<Menu> roleMenus = menuDao.queryMenusByRoleId(role.getRoleId(), applicationId, type);
            set.addAll(roleMenus);
        }

        for (Menu menu : set) {
            String url = menu.getUrl();
            if (StringUtils.isNotBlank(url)) {
                if (StringUtils.isNotBlank(parentMenuUrl)) {
                    if (parentMenuIds.contains(menu.getParentId())) {
                        urls.add(url);
                    }
                } else {
                    urls.add(url);
                }
            }
        }
        return urls;
    }

    @Override
    public void deleteMenu(Integer menuId) {
        Map params = new HashMap();
        params.put("menuId", menuId);
        //查询菜单组详情表
        List<MenuGroupDetail> menuGroupDetailList = menuGroupDetailDao.queryList(params);
        if (CollectionUtils.isNotEmpty(menuGroupDetailList)) {
            throw new RuntimeException("该菜单已被权限组绑定，请先解绑");
        }
        //查询用户菜单表
        List<UserMenu> userMenuList = userDao.queryUserMenus(menuId);
        if (CollectionUtils.isNotEmpty(userMenuList)) {
            throw new RuntimeException("该菜单已被用户绑定，请先解绑");
        }
        //查询子菜单
        List<Menu> children = menuDao.getChildren(menuId);
        if (CollectionUtils.isNotEmpty(children)) {
            throw new RuntimeException("该菜单下已有子菜单，请先删除子菜单");
        }
        //删除菜单
        menuDao.delete(menuId);
        //删除菜单数据维度关系
        menuDao.deleteMenuDimension(menuId);
    }
}
