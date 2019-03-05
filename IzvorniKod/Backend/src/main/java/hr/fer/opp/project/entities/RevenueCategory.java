package hr.fer.opp.project.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class RevenueCategory {

	/**
	 * Revenue category ID
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long revenueCategoryID;

    /**
     * Name of the revenue category
     */
    @Column
	@NotNull
    private String name;

	/**
	 * User that created revenue category
	 */
	@ManyToOne
	@JoinColumn(name="user_ID", referencedColumnName="userID")
	private User user;


	/**
	 * Group that uses this category - can be null
	 */
	@ManyToOne
	@JoinColumn(name = "group_ID")
	private HomeGroup homeGroup;

//	public RevenueCategory() {};
//
//	public RevenueCategory(@NotNull String name, User user, HomeGroup homeGroup) {
//		this.name = name;
//		this.user = user;
//		this.homeGroup = homeGroup;
//	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RevenueCategory)) return false;

		RevenueCategory that = (RevenueCategory) o;

		return revenueCategoryID != null ? revenueCategoryID.equals(that.revenueCategoryID) : that.revenueCategoryID == null;
	}

	@Override
	public int hashCode() {
		return revenueCategoryID != null ? revenueCategoryID.hashCode() : 0;
	}


	/**
	 * Gets User that created revenue category.
	 *
	 * @return Value of User that created revenue category.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets new User that created revenue category.
	 *
	 * @param user New value of User that created revenue category.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Sets new Name of the revenue category.
	 *
	 * @param name New value of Name of the revenue category.
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Gets Name of the revenue category.
	 *
	 * @return Value of Name of the revenue category.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets Revenue category ID.
	 *
	 * @return Value of Revenue category ID.
	 */
	public Long getRevenueCategoryID() {
		return revenueCategoryID;
	}

	/**
	 * Sets new Group that uses this category - can be null.
	 *
	 * @param group New value of Group that uses this category - can be null.
	 */
	public void setHomeGroup(HomeGroup group) {
		this.homeGroup = group;
	}

	/**
	 * Gets Group that uses this category - can be null.
	 *
	 * @return Value of Group that uses this category - can be null.
	 */
	public HomeGroup getHomeGroup() {
		return homeGroup;
	}
}
