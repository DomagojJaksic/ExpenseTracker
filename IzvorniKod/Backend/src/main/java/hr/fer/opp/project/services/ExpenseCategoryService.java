package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.User;

import java.util.List;
import java.util.Optional;

public interface ExpenseCategoryService {

    List<ExpenseCategory> listAll();

    List<ExpenseCategory> findByUser(User user);

    List<ExpenseCategory> findByHomeGroup(HomeGroup homeGroup);

    ExpenseCategory createExpenseCategory(ExpenseCategory expenseCategory);

    ExpenseCategory fetchExpenseCategory(long expenseCategoryID);

    Optional<ExpenseCategory> findById(long expenseCategoryID);

    ExpenseCategory updateExpenseCategory(ExpenseCategory expenseCategory);

    ExpenseCategory deleteExpenseCategory(long expenseCategoryID);
}
