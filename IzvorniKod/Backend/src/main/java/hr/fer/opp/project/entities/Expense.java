package hr.fer.opp.project.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Expense {


	/**
	 * Expense ID
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long expenseID;

	/**
     * Category of expense
     */
	@ManyToOne
	@JoinColumn(name="expenseCategory_ID", nullable = true)
	private ExpenseCategory expenseCategory;

	/**
	 * The amount of transaction (revenue/expense)
	 */
	@Column
	@Min(0)
	private Double amount;

	//using Date class might be a problem because parsing dates in JSON brings some complications with it
	/**
	 * The date of transaction (revenue/expense)
	 */
	@Column
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@NotNull
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
	@NotNull
	private LocalDateTime entryTime;

	/**
	 * User that made the expense
	 */
	@ManyToOne
	@NotNull
	@JoinColumn(name="userID")
	private User user;


	/**
	 * ExpenseCategory getter
	 * @return value of ExpenseCategory
	 */
	public ExpenseCategory getExpenseCategory() {
		return expenseCategory;
	}

	/**
	 * ExpenseCategory setter
	 * @param expenseCategory - the expense category
	 */
	public void setExpenseCategory(ExpenseCategory expenseCategory) {
		this.expenseCategory = expenseCategory;
	}

    /**
     * Gets Expense ID.
     *
     * @return Value of Expense ID.
     */
    public Long getExpenseID() {
        return expenseID;
    }

    /**
     * Sets new Expense ID.
     *
     * @param expenseID New value of Expense ID.
     */
    public void setExpenseID(Long expenseID) {
        this.expenseID = expenseID;
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
	 * Gets The date of transaction revenueexpense.
	 *
	 * @return Value of The date of transaction revenueexpense.
	 */
	public LocalDate getDate() {
		return date;
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
	 * Gets The description of transaction revenueexpense.
	 *
	 * @return Value of The description of transaction revenueexpense.
	 */
	public String getDescription() {
		return description;
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
	 * Gets Time when expense was entered.
	 *
	 * @return Value of Time when expense was entered.
	 */
	public LocalDateTime getEntryTime() {
		return entryTime;
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
	 * Gets User that made the expense.
	 *
	 * @return Value of User that made the expense.
	 */
	public User getUser() {
		return user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Expense)) return false;
		Expense expense = (Expense) o;
		return expenseID.equals(expense.expenseID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(expenseID);
	}


	/**
	 * Gets The amount of transaction revenueexpense.
	 *
	 * @return Value of The amount of transaction revenueexpense.
	 */
	public Double getAmount() {
		return amount;
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
	 * Sets new User that made the expense.
	 *
	 * @param user New value of User that made the expense.
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
