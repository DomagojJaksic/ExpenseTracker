package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.Expense;
import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<List<Expense>> findByExpenseCategory(ExpenseCategory expenseCategory);
    Optional<List<Expense>> findByUser(User user);
}
