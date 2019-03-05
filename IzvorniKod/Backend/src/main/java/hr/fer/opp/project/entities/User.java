package hr.fer.opp.project.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_")
public class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userID;

	/**
	 * The user's username
	 */
	@Column(unique = true)
	@NotNull
	private String username;
	
	/**
	 * The user's password
	 */
	@Column
	@NotNull
	private String password;
	
	/**
	 * The user's email address
	 */
	@Column(unique = true)
	@NotNull
	private String email;
	
	/**
	 * Is the user's email address verified
	 */
	@Column
	@NotNull
	private boolean emailVerified = false;

	@Column
	private String verificationCode;

	/**
	 * The user's first name
	 */
	@Column
	@NotNull
	private String firstName;

	/**
	 * The user's last name
	 */
	@Column
	@NotNull
	private String lastName;


	/**
	 * The user's current balance
	 */
	@Column
	@NotNull
	private Double currentBalance = 0.0;


	@Column
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@NotNull
	private LocalDateTime lastHomeGroupMembershipChangeTime;


	/**
	 * Sets new The user's current balance.
	 *
	 * @param currentBalance New value of The user's current balance.
	 */
	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * Gets Is the user's email address verified.
	 *
	 * @return Value of Is the user's email address verified.
	 */
	public boolean isEmailVerified() {
		return emailVerified;
	}



	/**
	 * Sets new The user's email address.
	 *
	 * @param email New value of The user's email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets new The user's password.
	 *
	 * @param password New value of The user's password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets The user's username.
	 *
	 * @return Value of The user's username.
	 */
	public String getUsername() {
		return username;
	}



	/**
	 * Gets The user's first name.
	 *
	 * @return Value of The user's first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets Last home group membership change time.
	 *
	 * @return Value of Last home group membership change time.
	 */
	public LocalDateTime getLastHomeGroupMembershipChangeTime() {
		return lastHomeGroupMembershipChangeTime;
	}

	/**
	 * Gets The user's current balance.
	 *
	 * @return Value of The user's current balance.
	 */
	public Double getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * Gets The user's email address.
	 *
	 * @return Value of The user's email address.
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * Sets new The user's first name.
	 *
	 * @param firstName New value of The user's first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets The user's last name.
	 *
	 * @return Value of The user's last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets new Last home group membership change time.
	 *
	 * @param lastHomeGroupMembershipChangeTime New value of Last home group membership change time.
	 */
	public void setLastHomeGroupMembershipChangeTime(LocalDateTime lastHomeGroupMembershipChangeTime) {
		this.lastHomeGroupMembershipChangeTime = lastHomeGroupMembershipChangeTime;
	}

	/**
	 * Sets new The user's username.
	 *
	 * @param username New value of The user's username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets The user's password.
	 *
	 * @return Value of The user's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets new The user's last name.
	 *
	 * @param lastName New value of The user's last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets new Is the user's email address verified.
	 *
	 * @param emailVerified New value of Is the user's email address verified.
	 */
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	/**
	 * Gets userID.
	 *
	 * @return Value of userID.
	 */
	public Long getUserID() {
		return userID;
	}

	/**
	 * Gets verificationCode.
	 *
	 * @return Value of verificationCode.
	 */
	public String getVerificationCode() {
		return verificationCode;
	}

	/**
	 * Sets new verificationCode.
	 *
	 * @param verificationCode New value of verificationCode.
	 */
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
}
