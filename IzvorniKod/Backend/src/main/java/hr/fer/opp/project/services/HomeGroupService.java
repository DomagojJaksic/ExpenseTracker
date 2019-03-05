package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.*;
import hr.fer.opp.project.entities.complexEntities.GroupMember;

import java.security.acl.Group;
import java.util.List;
import java.util.Optional;

public interface HomeGroupService {
    List<HomeGroup> listAll();

    Optional<HomeGroup> findById(long id);

    HomeGroup fetchHomeGroup(long id);

    HomeGroup createHomeGroup(HomeGroup homeGroup, User user);

    HomeGroup updateHomeGroup(HomeGroup homeGroup);

    HomeGroup deleteHomeGroup(long id);

    Optional<HomeGroup> findByName(String name);

    Optional<List<GroupMember>> findByUser(User user);

    void updateGroupBalance(long id);

    HomeGroup fetchHomeGroupByUser(User user);

    List<GroupMember> fetchGroupMembersByHomeGroup(HomeGroup homeGroup);

    List<User> fetchUsersByHomeGroup(HomeGroup homeGroup);

    List<HomeGroup> findInvitationsByUser(User user);

    List<User> fetchInvitedUsersByHomeGroup(HomeGroup homeGroup);

    List<User> fetchAdminUsersByHomeGroup(HomeGroup homeGroup);

    GroupMember fetchGroupMember(long id);

    GroupMember fetchGroupMemberByUsernameAndGroupID(String username, long id);

    GroupMember inviteNewGroupMember(GroupMember groupMember);

    GroupMember changeAdminStatus(GroupMember groupMember);

    GroupMember deleteGroupMember(long id);

    GroupMember addGroupFounder(GroupMember groupMember);

    GroupMember acceptInvitation(GroupMember groupMember);

    GroupMember declineInvitation(GroupMember groupMember);

    Long findGroupIDByUserMembership(User user);

    List<Expense> fetchExpensesByHomeGroup(HomeGroup homeGroup);

    List<Revenue> fetchRevenuesByHomeGroup(HomeGroup homeGroup);

    User isAdmin(HomeGroup homeGroup, User user);

    Double fetchGroupBalance(HomeGroup homeGroup);

//    List<ExpenseCategory> fetchExpeneCategoriesByHomeGroup(HomeGroup homeGroup);
//
//    List<RevenueCategory> fetchRevenueCategoriesByHomeGroup(HomeGroup homeGroup);


}
