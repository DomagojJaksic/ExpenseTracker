package hr.fer.opp.project.entities.complexEntities;

import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.enums.MemberRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SavingMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingMemberID;

    @ManyToOne
    @JoinColumn(name="user_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name="saving_ID")
    private Saving saving;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private MemberRole memberRole;

    public SavingMember() {};

    public SavingMember(User user, Saving saving, @NotNull MemberRole memberRole) {
        this.user = user;
        this.saving = saving;
        this.memberRole = memberRole;
    }

    /**
     * Gets savingMemberID.
     *
     * @return Value of savingMemberID.
     */
    public Long getSavingMemberID() {
        return savingMemberID;
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
     * Sets new saving.
     *
     * @param saving New value of saving.
     */
    public void setSaving(Saving saving) {
        this.saving = saving;
    }

    /**
     * Sets new memberRole.
     *
     * @param memberRole New value of memberRole.
     */
    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }

    /**
     * Sets new savingMemberID.
     *
     * @param savingMemberID New value of savingMemberID.
     */
    public void setSavingMemberID(Long savingMemberID) {
        this.savingMemberID = savingMemberID;
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
     * Gets memberRole.
     *
     * @return Value of memberRole.
     */
    public MemberRole getMemberRole() {
        return memberRole;
    }

    /**
     * Gets user.
     *
     * @return Value of user.
     */
    public User getUser() {
        return user;
    }
}
