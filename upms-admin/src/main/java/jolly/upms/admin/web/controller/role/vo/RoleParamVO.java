package jolly.upms.admin.web.controller.role.vo;


import jolly.upms.admin.web.vo.PageVO;

/**
 * @author chenjc
 * @since 2017-06-14
 */
public class RoleParamVO extends PageVO {
    private String name;
    private Integer menuGroupId;
    private Integer applicationId;

    private String sort;

    private String order;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Integer menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
}
