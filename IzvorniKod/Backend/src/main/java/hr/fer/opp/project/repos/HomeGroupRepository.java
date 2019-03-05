package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.HomeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeGroupRepository extends JpaRepository<HomeGroup, Long> {
    Optional<HomeGroup> findByGroupName(String groupName);
    int countByGroupName(String groupName);
}
