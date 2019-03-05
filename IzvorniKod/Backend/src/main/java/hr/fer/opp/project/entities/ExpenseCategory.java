package hr.fer.opp.project.entities;

import hr.fer.opp.project.enums.TimePeriod;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class ExpenseCategory {

    /**
     * Expense category ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseCategoryID;

    /**
     * Name of the expense category
     */
    @Column
    @NotNull
    private String name;

    /**
     * User that created this category - can be null(if group uses this category)
     */
    @ManyToOne
    @JoinColumn(name="user_ID", referencedColumnName="userID")
    private User user;

    /**
     * Time period for calculating average expenses for notifications
     */
    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private TimePeriod timePeriod;

    /**
     * Percentage that represents limit of expense amount before notification triggers
     */
    @Column
    @NotNull
    private Double limitPercentage;

    /**
     * Group that uses this category - can be null
     */
    @ManyToOne
    @JoinColumn(name = "group_ID")
    private HomeGroup homeGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseCategory)) return false;

        ExpenseCategory that = (ExpenseCategory) o;

        return expenseCategoryID != null ? expenseCategoryID.equals(that.expenseCategoryID) : that.expenseCategoryID == null;
    }

    @Override
    public int hashCode() {
        return expenseCategoryID != null ? expenseCategoryID.hashCode() : 0;
    }



    /**
     * Gets Time period for calculating average expenses for notifications.
     *
     * @return Value of Time period for calculating average expenses for notifications.
     */
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    /**
     * Gets Percentage that represents limit of expense amount before notification triggers.
     *
     * @return Value of Percentage that represents limit of expense amount before notification triggers.
     */
    public Double getLimitPercentage() {
        return limitPercentage;
    }

    /**
     * Sets new Time period for calculating average expenses for notifications.
     *
     * @param timePeriod New value of Time period for calculating average expenses for notifications.
     */
    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    /**
     * Sets new User that created this category - can be nullif group uses this category.
     *
     * @param user New value of User that created this category - can be nullif group uses this category.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets Name of the expense category.
     *
     * @return Value of Name of the expense category.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets new Name of the expense category.
     *
     * @param name New value of Name of the expense category.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets User that created this category - can be nullif group uses this category.
     *
     * @return Value of User that created this category - can be nullif group uses this category.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets new Percentage that represents limit of expense amount before notification triggers.
     *
     * @param limitPercentage New value of Percentage that represents limit of expense amount before notification triggers.
     */
    public void setLimitPercentage(Double limitPercentage) {
        this.limitPercentage = limitPercentage;
    }

    /**
     * Gets Expense category ID.
     *
     * @return Value of Expense category ID.
     */
    public Long getExpenseCategoryID() {
        return expenseCategoryID;
    }

    /**
     * Gets Group that uses this category - can be null.
     *
     * @return Value of Group that uses this category - can be null.
     */
    public HomeGroup getHomeGroup() {
        return homeGroup;
    }

    /**
     * Sets new Group that uses this category - can be null.
     *
     * @param group New value of Group that uses this category - can be null.
     */
    public void setHomeGroup(HomeGroup group) {
        this.homeGroup = group;
    }
}
