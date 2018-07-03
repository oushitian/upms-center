package jolly.upms.admin.web.controller.menu.vo;

import com.jolly.upms.manager.model.Menu;

/**
 * @author chenjc
 * @since 2017-06-06
 */
public class MenuVO extends Menu {
    private String typeName;
    private String parentName;
    private String appName;
    private String dimensionNames;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDimensionNames() {
        return dimensionNames;
    }

    public void setDimensionNames(String dimensionNames) {
        this.dimensionNames = dimensionNames;
    }
}
