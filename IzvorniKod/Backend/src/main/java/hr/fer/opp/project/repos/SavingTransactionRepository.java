package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.SavingTransaction;
import hr.fer.opp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingTransactionRepository extends JpaRepository<SavingTransaction, Long> {
    Optional<List<SavingTransaction>> findBySaving(Saving saving);
    Optional<List<SavingTransaction>> findByUser(User user);
}
