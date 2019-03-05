package hr.fer.opp.project.entities.complexEntities;

import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.enums.MemberRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupMemberID;

    @ManyToOne
    @JoinColumn(name="user_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name="group_ID")
    private HomeGroup homeGroup;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private MemberRole memberRole;


    public GroupMember() {};

    public GroupMember(User user, HomeGroup homeGroup, @NotNull MemberRole memberRole) {
        this.user = user;
        this.homeGroup = homeGroup;
        this.memberRole = memberRole;
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
     * Sets new group.
     *
     * @param group New value of group.
     */
    public void setHomeGroup(HomeGroup group) {
        this.homeGroup = group;
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
     * Sets new memberRole.
     *
     * @param memberRole New value of memberRole.
     */
    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
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
     * Gets group.
     *
     * @return Value of group.
     */
    public HomeGroup getHomeGroup() {
        return homeGroup;
    }

    /**
     * Gets groupMemberID.
     *
     * @return Value of groupMemberID.
     */
    public Long getGroupMemberID() {
        return groupMemberID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupMember)) return false;

        GroupMember that = (GroupMember) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return homeGroup != null ? homeGroup.equals(that.homeGroup) : that.homeGroup == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (homeGroup != null ? homeGroup.hashCode() : 0);
        return result;
    }
}
