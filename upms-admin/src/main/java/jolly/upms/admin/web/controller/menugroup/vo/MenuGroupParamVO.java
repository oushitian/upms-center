package jolly.upms.admin.web.controller.menugroup.vo;


import jolly.upms.admin.web.vo.PageVO;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public class MenuGroupParamVO extends PageVO {

    private String name;
    private Integer applicationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
}
