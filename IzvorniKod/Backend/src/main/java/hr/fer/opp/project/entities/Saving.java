package hr.fer.opp.project.entities;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import hr.fer.opp.project.entities.complexEntities.SavingMember;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Entity
public class Saving {


	/**
	 * Saving id
	 */
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingID;

	/**
	 * The savings name
	 */
	@Column
	@NotNull
	private String name;

	/**
	 * The savings start date
	 */
	@Column
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@NotNull
	private LocalDate startDate;
	
	/**
	 * The savings end date
	 */
	@Column
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@NotNull
	private LocalDate endDate;
	
	/**
	 * 	The savings current balance
	 */
	@Column
	@Min(0)
	private Double currentBalance;
	
	/**
	 * 	The savings targeted amount
	 */
	@Column
	@NotNull
	@Min(0)
	private Double targetedAmount;


	/**
	 * Creation time of the saving
	 */
	@Column
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@NotNull
	private LocalDateTime creationTime;


	/**
	 * Sets new The savings name.
	 *
	 * @param name New value of The savings name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets The savings start date.
	 *
	 * @return Value of The savings start date.
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Sets new The savings targeted amount.
	 *
	 * @param targetedAmount New value of The savings targeted amount.
	 */
	public void setTargetedAmount(Double targetedAmount) {
		this.targetedAmount = targetedAmount;
	}

	/**
	 * Sets new Creation time of the saving.
	 *
	 * @param creationTime New value of Creation time of the saving.
	 */
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Sets new The savings end date.
	 *
	 * @param endDate New value of The savings end date.
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * Sets new The savings current balance.
	 *
	 * @param currentBalance New value of The savings current balance.
	 */
	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * Gets The savings targeted amount.
	 *
	 * @return Value of The savings targeted amount.
	 */
	public Double getTargetedAmount() {
		return targetedAmount;
	}

	/**
	 * Gets The savings end date.
	 *
	 * @return Value of The savings end date.
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Gets The savings current balance.
	 *
	 * @return Value of The savings current balance.
	 */
	public Double getCurrentBalance() {
		return currentBalance;
	}


	/**
	 * Sets new The savings start date.
	 *
	 * @param startDate New value of The savings start date.
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}


	/**
	 * Gets Creation time of the saving.
	 *
	 * @return Value of Creation time of the saving.
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * Gets The savings name.
	 *
	 * @return Value of The savings name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets Saving id.
	 *
	 * @return Value of Saving id.
	 */
	public Long getSavingID() {
		return savingID;
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Saving)) return false;

		Saving saving = (Saving) o;

		return savingID != null ? savingID.equals(saving.savingID) : saving.savingID == null;
	}

	@Override
	public int hashCode() {
		return savingID != null ? savingID.hashCode() : 0;
	}
}
