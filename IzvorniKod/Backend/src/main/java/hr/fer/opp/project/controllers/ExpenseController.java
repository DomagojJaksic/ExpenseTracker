package hr.fer.opp.project.controllers;

import hr.fer.opp.project.entities.Expense;
import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private HomeGroupService homeGroupService;

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public Expense getExpense(@PathVariable("id") long id) {
        return expenseService.fetchExpense(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Optional<ExpenseCategory> ec = expenseCategoryService.findById(expense.getExpenseCategory().getExpenseCategoryID());
        long id = -1;
        User user = userService.fetchUser(expense.getUser().getUserID());
        Optional<List<GroupMember>> groupMembersOpt = homeGroupService.findByUser(user);
        List<GroupMember> groupMembers;
        GroupMember groupMember = null;
        if(groupMembersOpt.isPresent()) {
            groupMembers = groupMembersOpt.get();
            for(GroupMember gm : groupMembers) {
                if(gm.getMemberRole().equals(MemberRole.MEMBER) || gm.getMemberRole().equals(MemberRole.ADMIN)) {
                    groupMember = gm;
                    System.out.println(gm.getHomeGroup().getGroupID() + " ! " + gm.getUser().getUsername() );
                    break;
                }
            }
        }
        if(ec.isPresent()) {
            System.out.println("ec present");
        }
        if(ec.isPresent() && groupMember != null && (groupMember.getMemberRole().equals(MemberRole.ADMIN)
                || groupMember.getMemberRole().equals(MemberRole.MEMBER))) {
            if(ec.get().getHomeGroup() == null) {
                throw new IllegalArgumentException("Expense category does not belong to the group");
            }
            if (!groupMember.getHomeGroup().getGroupID()
                    .equals(expense.getExpenseCategory().getHomeGroup().getGroupID())) {
                throw new IllegalArgumentException("Expense category must belong to the right group!");
            } else {
                Expense saved = expenseService.createExpense(expense);
                return ResponseEntity.created(URI.create("/" + expense.getUser().getUserID() + "/expenses/" +
                        +saved.getExpenseID())).body(saved);
            }
        } else {
            if (ec.isPresent()) {
                id = ec.get().getUser().getUserID();
            } else {
                throw new RequestDeniedException("Expense category must be given");
            }
            if (!expense.getUser().getUserID().equals(expense.getExpenseCategory().getUser().getUserID())
                    || !expense.getUser().getUserID().equals(id)) {
                throw new IllegalArgumentException("Revenue category must belong to user!");
            } else {
                Expense saved = expenseService.createExpense(expense);
                return ResponseEntity.created(URI.create("/" + expense.getUser().getUserID() + "/expenses/" +
                        +saved.getExpenseID())).body(saved);
            }
        }
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public Expense updateExpense(@PathVariable("id") long id,
                                 @RequestBody Expense expense) {
        if (!expense.getExpenseID().equals(id)) {
            throw new IllegalArgumentException("ExpenseID must be preserved");
        }
        return expenseService.updateExpense(expense);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public Expense deleteExpense(@PathVariable("id") long id) {
        return expenseService.deleteExpense(id);
    }
}
