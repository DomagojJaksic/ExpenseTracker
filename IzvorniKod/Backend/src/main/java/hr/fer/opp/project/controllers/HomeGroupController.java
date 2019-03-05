package hr.fer.opp.project.controllers;

import hr.fer.opp.project.controllers.dataTransferObjects.GroupExistsDTO;
import hr.fer.opp.project.entities.*;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class HomeGroupController {

    @Autowired
    private HomeGroupService homeGroupService;

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private RevenueCategoryService revenueCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private RevenueService revenueService;

    @GetMapping("")
    public List<HomeGroup> listHomeGroups() {
        return homeGroupService.listAll();
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public HomeGroup getHomeGroup(@PathVariable("id") long id) {
        return homeGroupService.fetchHomeGroup(id);
    }

    @PostMapping("/name")
    @Secured("ROLE_USER")
    public HomeGroup getHomeGroup(@RequestBody GroupExistsDTO groupExistsDTO) {
        Optional<HomeGroup> homeGroupOptional = homeGroupService.findByName(groupExistsDTO.getName());
        return homeGroupOptional.orElseThrow(() -> new RequestDeniedException("There is no group with that name"));
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public HomeGroup createHomeGroup(@RequestBody HomeGroup homeGroup) {
        User user = userService.fetchCurrentUser();
        return homeGroupService.createHomeGroup(homeGroup, user);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public HomeGroup updateHomeGroup(@PathVariable("id") Long id, @RequestBody HomeGroup homeGroup) {
        if (!homeGroup.getGroupID().equals(id)) {
            throw new IllegalArgumentException("Group ID must be preserved");
        }
        return homeGroupService.updateHomeGroup(homeGroup);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public HomeGroup deleteHomeGroup(@PathVariable("id") long id) {
        return homeGroupService.deleteHomeGroup(id);
    }

    @GetMapping("/{id}/groupmembers")
    @Secured("ROLE_USER")
    public List<GroupMember> getHomeGroupMembers(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.fetchGroupMembersByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/users")
    @Secured("ROLE_USER")
    public List<User> fetchUsers(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.fetchUsersByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/invited")
    @Secured("ROLE_USER")
    public List<User> fetchInvitedUsers(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.fetchInvitedUsersByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/admins")
    @Secured("ROLE_USER")
    public List<User> fetchAdminUsers(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.fetchAdminUsersByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/expensecategories")
    @Secured("ROLE_USER")
    public List<ExpenseCategory> findExpenseCategoriesByGroup(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return expenseCategoryService.findByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/revenuecategories")
    @Secured("ROLE_USER")
    public List<RevenueCategory> findRevenueCategoriesByGroup(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return revenueCategoryService.findByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/expenses")
    @Secured("ROLE_USER")
    public List<Expense> findExpensesByGroup(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.fetchExpensesByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/revenues")
    @Secured("ROLE_USER")
    public List<Revenue> findRevenuesByGroup(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.fetchRevenuesByHomeGroup(homeGroup);
    }

    @GetMapping("/{id}/isAdmin/{username}")
    @Secured("ROLE_USER")
    public User isAdmin(@PathVariable("id") long id, @PathVariable("username") String username) {
        User user = userService.findByUsername(username).orElseThrow(
                () -> new RequestDeniedException("User with this username does not exist"));
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.isAdmin(homeGroup, user);
    }

    @GetMapping("/{id}/balance")
    @Secured("ROLE_USER")
    public Double fetchGroupBalance(@PathVariable("id") long id) {
        HomeGroup homeGroup = homeGroupService.fetchHomeGroup(id);
        return homeGroupService.fetchGroupBalance(homeGroup);
    }
}
