package jolly.upms.admin.web.vo;

/**
 * @author chenjc
 * @since 2017-06-05
 */
public class DataDimensionQueryVO extends PageVO {

    private String dimensionId;
    private String displayName;

    private String attributeName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }
}
