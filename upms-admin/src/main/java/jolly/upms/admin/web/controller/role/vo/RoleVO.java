package jolly.upms.admin.web.controller.role.vo;

import com.jolly.upms.manager.model.Role;

/**
 * @author chenjc
 * @since 2017-06-14
 */
public class RoleVO extends Role {

    private String appName;
    private String ownMenuGroups;
    private String ownMenuGroupIds;

    private String appKey;

    private String permitApplicationIds;

    public String getPermitApplicationIds() {
        return permitApplicationIds;
    }

    public void setPermitApplicationIds(String permitApplicationIds) {
        this.permitApplicationIds = permitApplicationIds;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getOwnMenuGroupIds() {
        return ownMenuGroupIds;
    }

    public void setOwnMenuGroupIds(String ownMenuGroupIds) {
        this.ownMenuGroupIds = ownMenuGroupIds;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOwnMenuGroups() {
        return ownMenuGroups;
    }

    public void setOwnMenuGroups(String ownMenuGroups) {
        this.ownMenuGroups = ownMenuGroups;
    }
}
