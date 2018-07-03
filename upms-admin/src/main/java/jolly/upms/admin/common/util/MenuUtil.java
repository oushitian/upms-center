package jolly.upms.admin.common.util;

import com.jolly.upms.manager.model.Menu;
import jolly.upms.admin.web.vo.MenuNode;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
public class MenuUtil {

    /**
     * 递归查找子菜单
     * @param id
     *            当前菜单id
     * @param rootMenu
     *            要查找的列表
     * @return
     */
    public static List<MenuNode> getChild(Integer id, List<Menu> rootMenu) {
        // 子菜单
        List<MenuNode> childList = new ArrayList<>();
        for (Menu menu : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            MenuNode addMenuDTO=new MenuNode();
            if (menu.getParentId()!=0) {
                if (menu.getParentId().equals(id)) {
                    try {
                        BeanUtils.copyProperties(addMenuDTO,menu);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    childList.add(addMenuDTO);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (MenuNode menu : childList) {// 没有url子菜单还有子菜单
            if (StringUtils.isBlank(menu.getUrl())) {
                // 递归
                menu.setChildrenMenus(getChild(menu.getMenuId(), rootMenu));
            }
        } // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
}
