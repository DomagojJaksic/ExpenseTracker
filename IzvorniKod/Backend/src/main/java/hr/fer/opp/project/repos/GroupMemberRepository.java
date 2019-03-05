package hr.fer.opp.project.repos;

import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<List<GroupMember>> findByHomeGroup(HomeGroup homeGroup);
    Optional<List<GroupMember>> findByUser(User user);
}
