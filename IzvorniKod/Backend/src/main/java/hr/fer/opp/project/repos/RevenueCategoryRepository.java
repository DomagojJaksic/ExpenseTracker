package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueCategoryRepository extends JpaRepository<RevenueCategory, Long> {
    Optional<List<RevenueCategory>> findByUser(User user);
    Optional<List<RevenueCategory>> findByHomeGroup(HomeGroup homeGroup);
}
