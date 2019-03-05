package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.Expense;
import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ExpenseService {

    List<Expense> listAll();

    List<Expense> findByUser(User user);

    Expense createExpense(Expense expense);

    Expense fetchExpense(long expenseID);

    Optional<Expense> findById(long expenseID);

    Expense updateExpense(Expense expense);

    Expense deleteExpense(long expenseID);

    List<Expense> findByExpenseCategory(ExpenseCategory expenseCategory);

    void deleteExpenseCategoryForExpenses(long expenseCategoryID);
}
