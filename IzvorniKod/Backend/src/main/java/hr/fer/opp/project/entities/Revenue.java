package hr.fer.opp.project.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
public class Revenue {

	/**
	 * Revenue ID
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long revenueID;

	/**
     * Category of revenue
     */
	@ManyToOne
	@JoinColumn(name="revenueCategory_ID", nullable = true)
	private RevenueCategory revenueCategory;

	/**
	 * The amount of transaction (revenue/expense)
	 */
	@Column
	@Min(0)
	private Double amount;

	/**
	 * The date of transaction (revenue/expense)
	 */
	@Column
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate date;

	/**
	 *  The description of transaction (revenue/expense)
	 */
	@Column
	private String description;

	/**
	 * Time when expense was entered
	 */
	@Column
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime entryTime;

	/**
	 * User that made the revenue
	 */
	@ManyToOne
	@JoinColumn(name="userID")
	private User user;

	/**
	 * RevenueCategory getter
	 * @return value of RevenueCategory
	 */
	public RevenueCategory getRevenueCategory() {
		return revenueCategory;
	}

	/**
	 * RevenueCategory setter
	 * @param revenueCategory - the revenue's category
	 */
	public void setRevenueCategory(RevenueCategory revenueCategory) {
		this.revenueCategory = revenueCategory;
	}

	/**
	 * Gets revenueID.
	 * @return Value of revenueID.
	 */
	public Long getRevenueID() {
		return revenueID;
	}

	/**
	 * Sets new revenueID.
	 * @param revenueID New value of revenueID.
	 */
	public void setRevenueID(Long revenueID) {
		this.revenueID = revenueID;
	}


	/**
	 * Sets new The amount of transaction revenueexpense.
	 *
	 * @param amount New value of The amount of transaction revenueexpense.
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * Gets The date of transaction revenueexpense.
	 *
	 * @return Value of The date of transaction revenueexpense.
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Gets The description of transaction revenueexpense.
	 *
	 * @return Value of The description of transaction revenueexpense.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets new The date of transaction revenueexpense.
	 *
	 * @param date New value of The date of transaction revenueexpense.
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}



	/**
	 * Sets new The description of transaction revenueexpense.
	 *
	 * @param description New value of The description of transaction revenueexpense.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets new Time when expense was entered.
	 *
	 * @param entryTime New value of Time when expense was entered.
	 */
	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}

	/**
	 * Gets Time when expense was entered.
	 *
	 * @return Value of Time when expense was entered.
	 */
	public LocalDateTime getEntryTime() {
		return entryTime;
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Revenue)) return false;
		Revenue revenue = (Revenue) o;
		return revenueID == revenue.revenueID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(revenueID);
	}


	/**
	 * Gets User that made the revenue.
	 *
	 * @return Value of User that made the revenue.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets new User that made the revenue.
	 *
	 * @param user New value of User that made the revenue.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Sets new The amount of transaction revenueexpense.
	 *
	 * @param amount New value of The amount of transaction revenueexpense.
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * Gets The amount of revenue.
	 *
	 * @return Value of The amount of reveneu.
	 */
	public Double getAmount() {
		return amount;
	}
}
