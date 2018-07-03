package jolly.upms.admin.web.controller.menu.vo;


import jolly.upms.admin.web.vo.PageVO;

/**
 * @author chenjc
 * @since 2017-06-05
 */
public class MenuParamVO extends PageVO {

    private Integer applicationId;

    private Byte type;

    private String name;

    private String parentName;

    private String url;

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
