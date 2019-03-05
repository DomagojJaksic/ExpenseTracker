package hr.fer.opp.project.controllers.dataTransferObjects;

public class RevenueCategoryDTO {
    private String categoryName;
    private Long id;
    private Boolean isGroup;


    /**
     * Sets new categoryName.
     *
     * @param categoryName New value of categoryName.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets id.
     *
     * @return Value of id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets new id.
     *
     * @param id New value of id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets categoryName.
     *
     * @return Value of categoryName.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Gets isGroup.
     *
     * @return Value of isGroup.
     */
    public Boolean getIsGroup() {
        return isGroup;
    }

    /**
     * Sets new isGroup.
     *
     * @param isGroup New value of isGroup.
     */
    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }
}
