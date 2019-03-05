package hr.fer.opp.project.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class HomeGroup {

    /**
     * Home group ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupID;

    /**
     * Name of the group
     */
    @Column(unique = true)
    private String groupName;

    /**
     * Group's current balance
     */
    @Column
    @NotNull
    private Double balance;

    /**
     * Date of founding
     */
    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @NotNull
    private LocalDateTime foundingDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomeGroup)) return false;

        HomeGroup group = (HomeGroup) o;

        return groupID != null ? groupID.equals(group.groupID) : group.groupID == null;
    }

    @Override
    public int hashCode() {
        return groupID != null ? groupID.hashCode() : 0;
    }

    /**
     * Gets Group's current balance.
     *
     * @return Value of Group's current balance.
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Sets new Name of the group.
     *
     * @param groupName New value of Name of the group.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Gets Date of founding.
     *
     * @return Value of Date of founding.
     */
    public LocalDateTime getFoundingDate() {
        return foundingDate;
    }

    /**
     * Gets Name of the group.
     *
     * @return Value of Name of the group.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets new Group's current balance.
     *
     * @param balance New value of Group's current balance.
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * Sets new Date of founding.
     *
     * @param foundingDate New value of Date of founding.
     */
    public void setFoundingDate(LocalDateTime foundingDate) {
        this.foundingDate = foundingDate;
    }

    /**
     * Gets Home group ID.
     *
     * @return Value of Home group ID.
     */
    public Long getGroupID() {
        return groupID;
    }
}
