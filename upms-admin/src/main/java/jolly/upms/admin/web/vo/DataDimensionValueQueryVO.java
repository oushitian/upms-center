package jolly.upms.admin.web.vo;

/**
 * @author chenjc
 * @since 2017-06-05
 */
public class DataDimensionValueQueryVO extends PageVO {

    private String dimensionId;
    private String displayName;



    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
    }
}
