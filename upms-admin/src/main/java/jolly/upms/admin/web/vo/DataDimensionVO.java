package jolly.upms.admin.web.vo;

import com.jolly.upms.manager.model.DataDimension;
import org.hibernate.validator.constraints.NotBlank;

/**
 * berkeley
 */
public class DataDimensionVO {

    private Integer dimensionId;

    @NotBlank
    private String displayName;

    @NotBlank
    private String attributeName;

    private String description;

    private Integer gmtCreated;

    private Integer gmtModified;

    private String modifier;
    private int isDeleted;

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

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName == null ? null : attributeName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Integer gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Integer getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Integer gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataDimension that = (DataDimension) o;

        return dimensionId != null ? dimensionId.equals(that.getDimensionId()) : that.getDimensionId() == null;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public int hashCode() {
        return dimensionId != null ? dimensionId.hashCode() : 0;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
