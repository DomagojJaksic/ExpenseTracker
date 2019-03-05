package hr.fer.opp.project.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import hr.fer.opp.project.enums.SavingTransactionType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class SavingTransaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingTransactionID;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "saving_ID")
    private Saving saving;

    @Column
    @Min(0)
    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private SavingTransactionType type;

    @Column
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate time;

    @Column
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime entryTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavingTransaction)) return false;

        SavingTransaction that = (SavingTransaction) o;

        return savingTransactionID != null ? savingTransactionID.equals(that.savingTransactionID) : that.savingTransactionID == null;
    }

    @Override
    public int hashCode() {
        return savingTransactionID != null ? savingTransactionID.hashCode() : 0;
    }

    /**
     * Sets new saving.
     *
     * @param saving New value of saving.
     */
    public void setSaving(Saving saving) {
        this.saving = saving;
    }

    /**
     * Sets new user.
     *
     * @param user New value of user.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Sets new amount.
     *
     * @param amount New value of amount.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Gets time.
     *
     * @return Value of time.
     */
    public LocalDate getTime() {
        return time;
    }

    /**
     * Gets type.
     *
     * @return Value of type.
     */
    public SavingTransactionType getType() {
        return type;
    }

    /**
     * Gets user.
     *
     * @return Value of user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets new type.
     *
     * @param type New value of type.
     */
    public void setType(SavingTransactionType type) {
        this.type = type;
    }

    /**
     * Gets amount.
     *
     * @return Value of amount.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets new time.
     *
     * @param time New value of time.
     */
    public void setTime(LocalDate time) {
        this.time = time;
    }

    /**
     * Gets saving.
     *
     * @return Value of saving.
     */
    public Saving getSaving() {
        return saving;
    }

    /**
     * Gets savingTransactionID.
     *
     * @return Value of savingTransactionID.
     */
    public Long getSavingTransactionID() {
        return savingTransactionID;
    }


    /**
     * Sets new entryTime.
     *
     * @param entryTime New value of entryTime.
     */
    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    /**
     * Gets entryTime.
     *
     * @return Value of entryTime.
     */
    public LocalDateTime getEntryTime() {
        return entryTime;
    }
}
