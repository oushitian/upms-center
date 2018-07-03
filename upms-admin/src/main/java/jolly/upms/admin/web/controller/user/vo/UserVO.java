package jolly.upms.admin.web.controller.user.vo;

import com.jolly.upms.manager.model.User;

/**
 * @author chenjc
 * @since 2017-06-22
 */
public class UserVO extends User {

    private String ownRoles;

    public String getOwnRoles() {
        return ownRoles;
    }

    public void setOwnRoles(String ownRoles) {
        this.ownRoles = ownRoles;
    }
}
