package jolly.upms.admin.web.vo;

import java.util.List;

public class MenuNode {

    private String name;
    private String url;
    private String permissionString;
    private Integer applicationId;
    private Integer parentId;
    private Integer menuId;


    private List<MenuNode> childrenMenus;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermissionString() {
        return permissionString;
    }

    public void setPermissionString(String permissionString) {
        this.permissionString = permissionString;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuNode> getChildrenMenus() {
        return childrenMenus;
    }

    public void setChildrenMenus(List<MenuNode> childrenMenus) {
        this.childrenMenus = childrenMenus;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}