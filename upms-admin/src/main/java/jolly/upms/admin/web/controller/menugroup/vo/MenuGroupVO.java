package jolly.upms.admin.web.controller.menugroup.vo;

import com.jolly.upms.manager.model.MenuGroup;

/**
 * @author chenjc
 * @since 2017-06-12
 */
public class MenuGroupVO extends MenuGroup {

    private String dataDimensions;

    private String appName;

    public String getDataDimensions() {
        return dataDimensions;
    }

    public void setDataDimensions(String dataDimensions) {
        this.dataDimensions = dataDimensions;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
