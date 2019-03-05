package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.Revenue;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    Optional<List<Revenue>> findByUser(User user);
    Optional<List<Revenue>> findByRevenueCategory(RevenueCategory revenueCategory);
}
