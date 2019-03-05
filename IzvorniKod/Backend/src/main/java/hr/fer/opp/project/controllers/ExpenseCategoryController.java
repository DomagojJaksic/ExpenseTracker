package hr.fer.opp.project.controllers;


import hr.fer.opp.project.entities.Expense;
import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.services.ExpenseCategoryService;
import hr.fer.opp.project.services.ExpenseService;
import hr.fer.opp.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expensecategories")
public class ExpenseCategoryController {

    @Autowired
    ExpenseCategoryService expenseCategoryService;

    @Autowired
    UserService userService;

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public ExpenseCategory getExpenseCategory(@PathVariable("id") long id) {
        return expenseCategoryService.fetchExpenseCategory(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public ResponseEntity<ExpenseCategory> createUsersExpenseCategory(@RequestBody ExpenseCategory expenseCategory) {
        List<ExpenseCategory> expenseCategories = new ArrayList<>();
        if(expenseCategory.getUser() != null && expenseCategory.getHomeGroup() == null) {
            expenseCategories = expenseCategoryService.findByUser(expenseCategory.getUser());
        } else if (expenseCategory.getUser() == null && expenseCategory.getHomeGroup() != null) {
            expenseCategories = expenseCategoryService.findByHomeGroup(expenseCategory.getHomeGroup());
        }
        for(ExpenseCategory ec : expenseCategories) {
            if(ec.getName().equals(expenseCategory.getName())) {
                throw new IllegalArgumentException("You already have expense category named: " + expenseCategory.getName());
            }
        }
        ExpenseCategory saved = expenseCategoryService.createExpenseCategory(expenseCategory);
        return ResponseEntity.created(URI.create("/expensecategory/" + saved.getExpenseCategoryID())).body(saved);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public ExpenseCategory updateExpenseCategory(@PathVariable("id") long id,
                                                 @RequestBody ExpenseCategory expenseCategory) {
        if (!expenseCategory.getExpenseCategoryID().equals(id)) {
            throw new IllegalArgumentException("ExpenseCategoryID must be preserved");
        }
        return expenseCategoryService.updateExpenseCategory(expenseCategory);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public ExpenseCategory deleteExpenseCategory(@PathVariable("id") long id) {
        return expenseCategoryService.deleteExpenseCategory(id);
    }
}
