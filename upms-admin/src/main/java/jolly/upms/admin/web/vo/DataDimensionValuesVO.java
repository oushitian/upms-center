package jolly.upms.admin.web.vo;

public class DataDimensionValuesVO  {
    private Integer recId;

    private Integer dimensionId;

    private String displayName;

    private String attributeValue;

    private String dimensionDisplayName;

    private int isDeleted;

    private String modifier;

    private Integer gmtModified;


    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public Integer getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(Integer dimensionId) {
        this.dimensionId = dimensionId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName == null ? null : displayName.trim();
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue == null ? null : attributeValue.trim();
    }

    public String getDimensionDisplayName() {
        return dimensionDisplayName;
    }

    public void setDimensionDisplayName(String dimensionDisplayName) {
        this.dimensionDisplayName = dimensionDisplayName;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Integer getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Integer gmtModified) {
        this.gmtModified = gmtModified;
    }
}