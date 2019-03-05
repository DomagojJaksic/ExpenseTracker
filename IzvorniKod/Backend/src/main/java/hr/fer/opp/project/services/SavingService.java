package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.SavingMember;

import java.util.List;
import java.util.Optional;

public interface SavingService {

    List<Saving> listAll();

    Saving createSaving(Saving saving, User user);

    Saving fetchSaving(long savingID);

    Optional<Saving> findById(long savingID);

    List<Saving> findSavingsByUser(User user);

    List<User> findUsersBySaving(Saving saving);

    List<User> findInvitedUsersBySaving(Saving saving);

    List<User> findAdminUsersBySaving(Saving saving);

    List<SavingMember> findSavingMembersBySaving(Saving saving);

    List<Saving> findInvitationsByUser(User user);

    Saving updateSaving(Saving saving);

    Saving deleteSaving(long savingID);

    SavingMember fetchSavingMember(long id);

    SavingMember fetchSavingMemberByUsernameAndSavingID(long savingID, String username);

    SavingMember inviteNewMember(SavingMember savingMember);

    SavingMember updateSavingMember(SavingMember savingMember);

    SavingMember deleteMember(long savingMemberID);

    SavingMember addFounder(SavingMember savingMember);

    SavingMember acceptInvitation(SavingMember savingMember);

    SavingMember declineInvitation(SavingMember savingMember);

    SavingMember changeAdminStatus(SavingMember savingMember);

    User isAdmin(Saving saving, User user);


}
