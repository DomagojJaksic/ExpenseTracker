package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {
    Optional<List<ExpenseCategory>> findByUser(User user);
    Optional<List<ExpenseCategory>> findByHomeGroup(HomeGroup homeGroup);
}
