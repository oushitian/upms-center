package jolly.upms.admin.web.controller.user.vo;


import jolly.upms.admin.web.vo.PageVO;

/**
 * @author chenjc
 * @since 2017-06-02
 */
public class UserParamVO extends PageVO {
    private String userName;
    private String roleName;
    private String email;
    private Integer applicationId;

    private Integer privilege;

    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
