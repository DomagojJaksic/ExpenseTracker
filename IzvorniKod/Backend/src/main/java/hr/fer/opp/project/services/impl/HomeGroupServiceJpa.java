package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.*;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.enums.NotificationType;
import hr.fer.opp.project.repos.GroupMemberRepository;
import hr.fer.opp.project.repos.HomeGroupRepository;
import hr.fer.opp.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeGroupServiceJpa implements HomeGroupService {

    @Autowired
    private HomeGroupRepository homeGroupRepo;

    @Autowired
    private GroupMemberRepository groupMemberRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private RevenueCategoryService revenueCategoryService;

    @Override
    public List<HomeGroup> listAll() {
        return homeGroupRepo.findAll();
    }

    @Override
    public Optional<HomeGroup> findById(long id) {
        return homeGroupRepo.findById(id);
    }

    @Override
    public HomeGroup fetchHomeGroup(long id) {
        Optional<HomeGroup> homeGroup = findById(id);
        if(homeGroup.isPresent()) {
            return homeGroup.get();
        } else {
            throw new EntityMissingException(HomeGroup.class, id);
        }
    }

    @Override
    public HomeGroup createHomeGroup(HomeGroup homeGroup, User user) {
        homeGroup.setBalance(0.);
        homeGroup.setFoundingDate(LocalDateTime.now());
        validate(homeGroup);
        if(homeGroupRepo.countByGroupName(homeGroup.getGroupName()) > 0) {
            throw new RequestDeniedException("Group with this name already exists");
        }
        HomeGroup homeGroupRet = homeGroupRepo.save(homeGroup);
        addGroupFounder(new GroupMember(user, homeGroupRet, MemberRole.ADMIN));
        return homeGroupRet;
    }

    @Override
    public HomeGroup updateHomeGroup(HomeGroup homeGroup) {
        validate(homeGroup);
        if(!homeGroupRepo.existsById(homeGroup.getGroupID())) {
            throw new EntityMissingException(HomeGroup.class, homeGroup.getGroupID());
        }
        return homeGroupRepo.save(homeGroup);
    }

    @Override
    public HomeGroup deleteHomeGroup(long id) {
        HomeGroup homeGroup = fetchHomeGroup(id);
        List<GroupMember> groupMembers = fetchGroupMembersByHomeGroup(homeGroup);
        for(GroupMember gm : groupMembers) {
            deleteGroupMember(gm.getGroupMemberID());
            List<RevenueCategory> revenueCategories = revenueCategoryService.findByHomeGroup(homeGroup);
            List<ExpenseCategory> expenseCategories = expenseCategoryService.findByHomeGroup(homeGroup);
            for(RevenueCategory revenueCategory : revenueCategories) {
                revenueCategoryService.deleteRevenueCategory(revenueCategory.getRevenueCategoryID());
            }
            for(ExpenseCategory expenseCategory : expenseCategories) {
                expenseCategoryService.deleteExpenseCategory(expenseCategory.getExpenseCategoryID());
            }
        }
        homeGroupRepo.delete(homeGroup);
        return homeGroup;
    }

    @Override
    public Optional<HomeGroup> findByName(String name) {
        return homeGroupRepo.findByGroupName(name);
    }

    @Override
    public Optional<List<GroupMember>> findByUser(User user) {
        return groupMemberRepo.findByUser(user);
    }

    @Override
    public void updateGroupBalance(long id) {
        HomeGroup homeGroup = fetchHomeGroup(id);
        List<GroupMember> groupMembers = fetchGroupMembersByHomeGroup(homeGroup);
        double amount = 0.;
        Double startBalance = homeGroup.getBalance();
        for(GroupMember groupMember : groupMembers) {
            if(!groupMember.getMemberRole().equals(MemberRole.INVITED)) {
                userService.updateCurrentBalance(groupMember.getUser().getUserID());
                User user = userService.fetchUser(groupMember.getUser().getUserID());
                amount += user.getCurrentBalance();
            }
        }
        homeGroup.setBalance(amount);
        updateHomeGroup(homeGroup);
        if(homeGroup.getBalance() < 0 && homeGroup.getBalance() < startBalance) {

            Notification notification = notificationService.createNotification(
                    new Notification(NotificationType.BUDGET_MINUS,
                            "Stanje Vaše kućne blagajne je trenutačno manje od 0 kuna.",
                            LocalDateTime.now()));
            List<User> users = fetchUsersByHomeGroup(homeGroup);
            for(User user : users) {
                notificationService.createNotifiedUser(new NotifiedUser(user, notification));
            }
        }
    }

    @Override
    public HomeGroup fetchHomeGroupByUser(User user) {
        Optional<List<GroupMember>> groupMembersOpt = findByUser(user);
        if(groupMembersOpt.isPresent()) {
            long id = -1;
            List<GroupMember> groupMembers = groupMembersOpt.get();
            for(GroupMember gm : groupMembers) {
                if(gm.getMemberRole() == MemberRole.MEMBER || gm.getMemberRole() == MemberRole.ADMIN) {
                    id = gm.getHomeGroup().getGroupID();
                    break;
                }
            }
            if(id < 0) {
                throw new RequestDeniedException("User doesn't belong to a group");
            }
            return fetchHomeGroup(id);
        } else {
            throw new RequestDeniedException("User doesn't belong to a group");
        }
    }

    @Override
    public List<GroupMember> fetchGroupMembersByHomeGroup(HomeGroup homeGroup) {
        Optional<List<GroupMember>> groupMembers = groupMemberRepo.findByHomeGroup(homeGroup);
        return groupMembers.orElseGet(ArrayList::new);
    }

    @Override
    public List<User> fetchUsersByHomeGroup(HomeGroup homeGroup) {
        List<GroupMember> groupMembers = fetchGroupMembersByHomeGroup(homeGroup);
        List<User> users = new ArrayList<>();
        for(GroupMember groupMember : groupMembers) {
            if(!groupMember.getMemberRole().equals(MemberRole.INVITED)) {
                users.add(groupMember.getUser());
            }
        }
        return users;
    }

    @Override
    public List<User> fetchInvitedUsersByHomeGroup(HomeGroup homeGroup) {
        List<GroupMember> groupMembers = fetchGroupMembersByHomeGroup(homeGroup);
        List<User> users = new ArrayList<>();
        for(GroupMember groupMember : groupMembers) {
            if(groupMember.getMemberRole().equals(MemberRole.INVITED)) {
                users.add(groupMember.getUser());
            }
        }
        return users;
    }

    @Override
    public List<User> fetchAdminUsersByHomeGroup(HomeGroup homeGroup) {
        List<GroupMember> groupMembers = fetchGroupMembersByHomeGroup(homeGroup);
        List<User> users = new ArrayList<>();
        for(GroupMember groupMember : groupMembers) {
            if(groupMember.getMemberRole().equals(MemberRole.ADMIN)) {
                users.add(groupMember.getUser());
            }
        }
        return users;
    }

    @Override
    public GroupMember fetchGroupMember(long id) {
        Optional<GroupMember> groupMember = groupMemberRepo.findById(id);
        if(groupMember.isPresent()) {
            return groupMember.get();
        } else {
            throw new EntityMissingException(GroupMember.class, id);
        }
    }

    @Override
    public GroupMember fetchGroupMemberByUsernameAndGroupID(String username, long id) {
        HomeGroup homeGroup = fetchHomeGroup(id);
        Optional<List<GroupMember>> groupMembersOpt = groupMemberRepo.findByHomeGroup(homeGroup);
        if(groupMembersOpt.isPresent()) {
            List<GroupMember> groupMembers = groupMembersOpt.get();
            for(GroupMember groupMember : groupMembers) {
                if(groupMember.getUser().getUsername().equals(username)) {
                    return groupMember;
                }
            }
        }

        throw new RequestDeniedException("Group member does not exist.");
    }

    @Override
    public GroupMember inviteNewGroupMember(GroupMember groupMember) {
        groupMember.setMemberRole(MemberRole.INVITED);
        validateAddingGroupMember(groupMember);
        GroupMember groupMemberRet = groupMemberRepo.save(groupMember);
        Notification notification = new Notification(NotificationType.GROUP_INVITATION,
                "Pozvani ste u grupu " + groupMemberRet.getHomeGroup().getGroupName() + ".",
                LocalDateTime.now());
        notificationService.createNotification(notification);
        User user = userService.fetchUser(groupMemberRet.getUser().getUserID());
        notificationService.createNotifiedUser(new NotifiedUser(user, notification));
        return groupMemberRet;
    }

    @Override
    public GroupMember acceptInvitation(GroupMember groupMember) {
        validateAddingGroupMember(groupMember);
        if(groupMember.getMemberRole()!=MemberRole.INVITED) {
            throw new RequestDeniedException("User must be invited.");
        }
        groupMember.setMemberRole(MemberRole.MEMBER);
        User user = userService.fetchUser(groupMember.getUser().getUserID());
        user.setLastHomeGroupMembershipChangeTime(LocalDateTime.now());
        userService.updateUser(user);
        userService.updateCurrentBalance(user.getUserID());
        return groupMemberRepo.save(groupMember);
    }

    @Override
    public GroupMember declineInvitation(GroupMember groupMember) {
        if(groupMember.getMemberRole()!=MemberRole.INVITED) {
            throw new RequestDeniedException("User must be invited.");
        }
        groupMemberRepo.delete(groupMember);
        return groupMember;
    }

    @Override
    public GroupMember addGroupFounder(GroupMember groupMember) {
        groupMember.setMemberRole(MemberRole.ADMIN);
        validateAddingGroupMember(groupMember);
        User user = userService.fetchUser(groupMember.getUser().getUserID());
        user.setLastHomeGroupMembershipChangeTime(LocalDateTime.now());
        userService.updateUser(user);
        userService.updateCurrentBalance(user.getUserID());
        return groupMemberRepo.save(groupMember);
    }

    @Override
    public GroupMember changeAdminStatus(GroupMember groupMember) {
        if(groupMember.getMemberRole()==MemberRole.MEMBER) {
            groupMember.setMemberRole(MemberRole.ADMIN);
        } else if(groupMember.getMemberRole()==MemberRole.ADMIN) {
            groupMember.setMemberRole(MemberRole.MEMBER);
        } else {
            throw new RequestDeniedException("User is not member of a group");
        }
        if(groupMemberRepo.existsById(groupMember.getGroupMemberID())) {
            GroupMember groupMemberRet = groupMemberRepo.save(groupMember);
            Notification notification = new Notification(NotificationType.ADMIN_CHANGE,
                    "Vaš administratorski status u grupi "
                            + groupMemberRet.getHomeGroup().getGroupName() + " se promijenio.",
                    LocalDateTime.now());
            notificationService.createNotification(notification);
            User user = userService.fetchUser(groupMemberRet.getUser().getUserID());
            notificationService.createNotifiedUser(new NotifiedUser(user, notification));
            return groupMemberRet;
        } else {
            throw new EntityMissingException(GroupMember.class, groupMember);
        }
    }

    @Override
    public GroupMember deleteGroupMember(long id) {
        GroupMember groupMember = fetchGroupMember(id);
        Long groupID = groupMember.getHomeGroup().getGroupID();
        User user = userService.fetchUser(groupMember.getUser().getUserID());
        groupMemberRepo.delete(groupMember);
        user.setLastHomeGroupMembershipChangeTime(LocalDateTime.now());
        userService.updateUser(user);
        userService.updateCurrentBalance(user.getUserID());
        updateGroupBalance(groupID);
        return groupMember;
    }

    @Override
    public List<HomeGroup> findInvitationsByUser(User user) {
        List<GroupMember> groupMembers = groupMemberRepo.findByUser(user).orElseGet(ArrayList::new);
        List<HomeGroup> groups = new ArrayList<>();
        for(GroupMember groupMember : groupMembers) {
            if(groupMember.getMemberRole().equals(MemberRole.INVITED)) {
                groups.add(groupMember.getHomeGroup());
            }
        }
        return groups;
    }

    @Override
    public Double fetchGroupBalance(HomeGroup homeGroup) {
        updateGroupBalance(homeGroup.getGroupID());
        return fetchHomeGroup(homeGroup.getGroupID()).getBalance();
    }

    private void validate(HomeGroup homeGroup) {
        Assert.notNull(homeGroup, "Home group must not be null");
        Assert.notNull(homeGroup.getGroupName(), "Group must be named");
        Assert.hasLength(homeGroup.getGroupName(), "Group must be named");
    }

    private void validateAddingGroupMember(GroupMember groupMember) {
        Assert.notNull(groupMember, "Group member must be given");
        Assert.notNull(groupMember.getMemberRole(), "Role must be given");
        Assert.notNull(groupMember.getUser(), "User must be given");
        Assert.notNull(groupMember.getHomeGroup(), "Group must be given");
        Optional<HomeGroup> hg = homeGroupRepo.findById(groupMember.getHomeGroup().getGroupID());

        if(!hg.isPresent()) {
            throw new IllegalArgumentException("Home Group does not exist.");
        } else {
            Optional<List<GroupMember>> groupMembersOpt = groupMemberRepo.findByUser(groupMember.getUser());
            if(groupMembersOpt.isPresent()) {
                List<GroupMember> groupMembers = groupMembersOpt.get();
                for(GroupMember groupMemberExists : groupMembers) {
                    if(groupMember.getMemberRole() != MemberRole.INVITED && (
                            groupMemberExists.getMemberRole() == MemberRole.MEMBER
                            || groupMemberExists.getMemberRole() == MemberRole.ADMIN)) {
                        throw new IllegalArgumentException("This user is already member of a group");
                    }
                }
            }
        }
    }

    public Long findGroupIDByUserMembership(User user) {
        Optional<List<GroupMember>> groupMemberList = findByUser(user);
        if(groupMemberList.isPresent()) {
            List<GroupMember> groupMembershipList = groupMemberList.get();
            for(GroupMember groupMember : groupMembershipList) {
                if(groupMember.getMemberRole().equals(MemberRole.MEMBER)
                        || groupMember.getMemberRole().equals(MemberRole.ADMIN)) {
                    return groupMember.getHomeGroup().getGroupID();
                }
            }
        }
        throw new RequestDeniedException("User is not a member of a group");
    }

    @Override
    public List<Expense> fetchExpensesByHomeGroup(HomeGroup homeGroup) {
        List<GroupMember> groupMembers = fetchGroupMembersByHomeGroup(homeGroup);
        List<Expense> expenses = new ArrayList<>();
        for(GroupMember groupMember : groupMembers) {
            if(!groupMember.getMemberRole().equals(MemberRole.INVITED)) {
                User user = groupMember.getUser();
                List<Expense> expenseList = expenseService.findByUser(user);
                for(Expense expense : expenseList) {
                    if(expense.getEntryTime().isAfter(user.getLastHomeGroupMembershipChangeTime())) {
                        expenses.add(expense);
                    }
                }
            }
        }
        return expenses;
    }

    @Override
    public List<Revenue> fetchRevenuesByHomeGroup(HomeGroup homeGroup) {
        List<GroupMember> groupMembers = fetchGroupMembersByHomeGroup(homeGroup);
        List<Revenue> revenues = new ArrayList<>();
        for(GroupMember groupMember : groupMembers) {
            if(!groupMember.getMemberRole().equals(MemberRole.INVITED)) {
                User user = groupMember.getUser();
                List<Revenue> revenueList = revenueService.findByUser(user);
                for(Revenue revenue : revenueList) {
                    if(revenue.getEntryTime().isAfter(user.getLastHomeGroupMembershipChangeTime())) {
                        revenues.add(revenue);
                    }
                }
            }
        }
        return revenues;
    }

    @Override
    public User isAdmin(HomeGroup homeGroup, User user) {
        List<User> admins = fetchAdminUsersByHomeGroup(homeGroup);
        for(User admin: admins) {
            if(admin.getUserID().equals(user.getUserID())) {
                return admin;
            }
        }

        throw new RequestDeniedException("User is not an admin");
    }

//    @Override
//    public List<ExpenseCategory> fetchExpeneCategoriesByHomeGroup(HomeGroup homeGroup) {
//        return null;
//    }
//
//    @Override
//    public List<RevenueCategory> fetchRevenueCategoriesByHomeGroup(HomeGroup homeGroup) {
//        return null;
//    }
}
