package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.SavingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingMemberRepository extends JpaRepository<SavingMember, Long> {
    Optional<List<SavingMember>> findByUser(User user);
    Optional<List<SavingMember>> findBySaving(Saving saving);
}
