package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.controllers.dataTransferObjects.MemberDTO;
import hr.fer.opp.project.entities.Notification;
import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.SavingTransaction;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import hr.fer.opp.project.entities.complexEntities.SavingMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.enums.NotificationType;
import hr.fer.opp.project.enums.SavingTransactionType;
import hr.fer.opp.project.repos.SavingMemberRepository;
import hr.fer.opp.project.repos.SavingRepository;
import hr.fer.opp.project.repos.UserRepository;
import hr.fer.opp.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SavingServiceJpa implements SavingService {

    @Autowired
    private SavingRepository savingRepo;

    @Autowired
    private SavingMemberRepository savingMemberRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private HomeGroupService homeGroupService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SavingTransactionService savingTransactionService;

    @Override
    public List<Saving> listAll() {
        return savingRepo.findAll();
    }

    @Override
    public Saving createSaving(Saving saving, User user) {
        saving.setCurrentBalance(0.);
        saving.setCreationTime(LocalDateTime.now());
        saving.setStartDate(LocalDate.now());
        validate(saving);
        Saving savingRet = savingRepo.save(saving);
        addFounder(new SavingMember(user, savingRet, MemberRole.ADMIN));
        return savingRet;
    }

    @Override
    public Saving fetchSaving(long savingID) {
        Optional<Saving> saving = savingRepo.findById(savingID);
        if(!saving.isPresent()) {
            throw new EntityMissingException(Saving.class, savingID);
        }
        return saving.get();
    }

    @Override
    public Optional<Saving> findById(long savingID) {
        return savingRepo.findById(savingID);
    }

    @Override
    public List<Saving> findSavingsByUser(User user) {
        List<Saving> savings = new ArrayList<>();
        Optional<List<SavingMember>> sm = savingMemberRepo.findByUser(user);
        if(sm.isPresent()) {
            List<SavingMember> savingMembers = sm.get();
            for(SavingMember savingMember : savingMembers) {
                if(!savingMember.getMemberRole().equals(MemberRole.INVITED)) {
                    Saving saving = fetchSaving(savingMember.getSaving().getSavingID());
                    savings.add(saving);
                }
            }
        }
        return savings;
    }

    @Override
    public List<SavingMember> findSavingMembersBySaving(Saving saving) {
        Optional<List<SavingMember>> sm = savingMemberRepo.findBySaving(saving);
        return sm.orElseGet(ArrayList::new);
    }

    @Override
    public List<Saving> findInvitationsByUser(User user) {
        List<SavingMember> savingMembers = savingMemberRepo.findByUser(user).orElseGet(ArrayList::new);
        List<Saving> savings = new ArrayList<>();
        for(SavingMember savingMember : savingMembers) {
            if(savingMember.getMemberRole().equals(MemberRole.INVITED)) {
                savings.add(savingMember.getSaving());
            }
        }
        return savings;
    }

    @Override
    public List<User> findUsersBySaving(Saving saving) {
        List<SavingMember> savingMembers = findSavingMembersBySaving(saving);
        List<User> users = new ArrayList<>();
        for(SavingMember savingMember : savingMembers) {
            if(!savingMember.getMemberRole().equals(MemberRole.INVITED)) {
                users.add(savingMember.getUser());
            }
        }
        return users;
    }

    @Override
    public List<User> findInvitedUsersBySaving(Saving saving) {
        List<SavingMember> savingMembers = findSavingMembersBySaving(saving);
        List<User> users = new ArrayList<>();
        for(SavingMember savingMember : savingMembers) {
            if(savingMember.getMemberRole().equals(MemberRole.INVITED)) {
                users.add(savingMember.getUser());
            }
        }
        return users;
    }

    @Override
    public List<User> findAdminUsersBySaving(Saving saving) {
        List<SavingMember> savingMembers = findSavingMembersBySaving(saving);
        List<User> users = new ArrayList<>();
        for(SavingMember savingMember : savingMembers) {
            if(savingMember.getMemberRole().equals(MemberRole.ADMIN)) {
                users.add(savingMember.getUser());
            }
        }
        return users;
    }


    @Override
    public Saving updateSaving(Saving saving) {
        validate(saving);
        Long savingID = saving.getSavingID();
        if (!savingRepo.existsById(savingID)) {
            throw new EntityMissingException(Saving.class, savingID);
        }
        return savingRepo.save(saving);
    }

    @Override
    public Saving deleteSaving(long savingID) {
        Saving saving = fetchSaving(savingID);
        Optional<List<SavingMember>> savingMembersOpt = savingMemberRepo.findBySaving(saving);
        List<SavingMember> savingMembers;
        if(savingMembersOpt.isPresent()) {
            savingMembers = savingMembersOpt.get();
        } else {
            throw new EntityMissingException(SavingMember.class, savingMembersOpt);
        }
        List<SavingTransaction> savingTransactions = savingTransactionService.findBySaving(saving);
        for(SavingTransaction savingTransaction : savingTransactions) {
            if(savingTransaction.getType().equals(SavingTransactionType.WITHDRAW)) {
                savingTransactionService.deleteSavingTransactionWhenDeletingSaving(savingTransaction.getSavingTransactionID());
            }
        }
        for(SavingTransaction savingTransaction : savingTransactions) {
            if(savingTransaction.getType().equals(SavingTransactionType.DEPOSIT)) {
                savingTransactionService.deleteSavingTransactionWhenDeletingSaving(savingTransaction.getSavingTransactionID());
            }
        }
        for(SavingMember savingMember : savingMembers) {
            deleteMember(savingMember.getSavingMemberID());
            User user = userService.fetchUser(savingMember.getUser().getUserID());
            userService.updateCurrentBalance(user.getUserID());
            try {
                homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
            } catch(RequestDeniedException ignore) { }
        }
        savingRepo.delete(saving);
        return saving;
    }

    @Override
    public SavingMember fetchSavingMember(long id) {
        Optional<SavingMember> savingMember = savingMemberRepo.findById(id);
        if(savingMember.isPresent()) {
            return savingMember.get();
        } else {
            throw new EntityMissingException(SavingMember.class, id);
        }
    }

    @Override
    public SavingMember fetchSavingMemberByUsernameAndSavingID(long savingID, String username) {
        Saving saving = fetchSaving(savingID);
        Optional<List<SavingMember>> savingMembersOpt = savingMemberRepo.findBySaving(saving);
        if(savingMembersOpt.isPresent()) {
            List<SavingMember> savingMembers = savingMembersOpt.get();
            for(SavingMember savingMember : savingMembers) {
                if(savingMember.getUser().getUsername().equals(username)) {
                    return savingMember;
                }
            }
        }

        throw new RequestDeniedException("Saving member does not exist.");
    }

    @Override
    public SavingMember inviteNewMember(SavingMember savingMember) {
        validateAddingSavingMember(savingMember);
        savingMember.setMemberRole(MemberRole.INVITED);
        SavingMember savingMemberRet = savingMemberRepo.save(savingMember);
        Notification notification = new Notification(NotificationType.SAVING_INVITATION,
                "Pozvani ste u štednju " + savingMemberRet.getSaving().getName() + ".",
                LocalDateTime.now());
        notificationService.createNotification(notification);
        User user = userService.fetchUser(savingMemberRet.getUser().getUserID());
        notificationService.createNotifiedUser(new NotifiedUser(user, notification));
        return savingMemberRet;
    }

    @Override
    public SavingMember updateSavingMember(SavingMember savingMember) {
        if(savingMemberRepo.existsById(savingMember.getSavingMemberID())) {
            return savingMemberRepo.save(savingMember);
        } else {
            throw new EntityMissingException(SavingMember.class, savingMember.getSavingMemberID());
        }
    }


    @Override
    public SavingMember deleteMember(long savingMemberID) {
        SavingMember savingMember = fetchSavingMember(savingMemberID);
        Long savingID = savingMember.getSaving().getSavingID();
        Saving saving = fetchSaving(savingID);
//        savingMemberRepo.delete(savingMember);
        List<SavingTransaction> savingTransactions = savingTransactionService.findBySaving(saving);
        for(SavingTransaction savingTransaction : savingTransactions) {
            if(savingTransaction.getUser().getUserID().equals(savingMember.getUser().getUserID())) {
                savingTransactionService.deleteSavingTransaction(savingTransaction.getSavingTransactionID());
            }
        }
        try {
            savingTransactionService.updateSavingAmount(savingID);
        } catch (RuntimeException ignore) {}
        savingMemberRepo.delete(savingMember);

        return savingMember;
    }

    @Override
    public SavingMember addFounder(SavingMember savingMember) {
        validateAddingSavingMember(savingMember);
        savingMember.setMemberRole(MemberRole.ADMIN);
        return savingMemberRepo.save(savingMember);
    }

    @Override
    public SavingMember acceptInvitation(SavingMember savingMember) {
        if(savingMember.getMemberRole()!=MemberRole.INVITED) {
            throw new RequestDeniedException("User must be invited.");
        }
        savingMember.setMemberRole(MemberRole.MEMBER);
        User user = userService.fetchUser(savingMember.getUser().getUserID());
        userService.updateUser(user);
        return savingMemberRepo.save(savingMember);
    }

    @Override
    public SavingMember declineInvitation(SavingMember savingMember) {
        if(savingMember.getMemberRole()!=MemberRole.INVITED) {
            throw new RequestDeniedException("User must be invited.");
        }
        savingMemberRepo.delete(savingMember);
        return savingMember;
    }

    @Override
    public SavingMember changeAdminStatus(SavingMember savingMember) {
        if(savingMember.getMemberRole().equals(MemberRole.MEMBER)) {
            savingMember.setMemberRole(MemberRole.ADMIN);
        } else if(savingMember.getMemberRole().equals(MemberRole.ADMIN)) {
            savingMember.setMemberRole(MemberRole.MEMBER);
        } else {
            throw new RequestDeniedException("User is not member of a group");
        }
        if(savingMemberRepo.existsById(savingMember.getSavingMemberID())) {
            SavingMember savingMemberRet = savingMemberRepo.save(savingMember);
            Notification notification = new Notification(NotificationType.ADMIN_CHANGE,
                    "Vaš administratorski status u štednji "
                            + savingMemberRet.getSaving().getName() + " se promijenio.",
                    LocalDateTime.now());
            notificationService.createNotification(notification);
            User user = userService.fetchUser(savingMemberRet.getUser().getUserID());
            notificationService.createNotifiedUser(new NotifiedUser(user, notification));
            return savingMemberRet;
        } else {
            throw new EntityMissingException(SavingMember.class, savingMember);
        }
    }

    @Override
    public User isAdmin(Saving saving, User user) {
        List<User> admins = findAdminUsersBySaving(saving);
        for(User admin: admins) {
            if(admin.getUserID().equals(user.getUserID())) {
                return admin;
            }
        }

        throw new RequestDeniedException("User is not an admin");
    }

    private List<Saving> listSavings(List<SavingMember> savingMembers) {
        List<Saving> retList = new ArrayList<>();
        for(SavingMember savingMem : savingMembers) {
            retList.add(fetchSaving(savingMem.getSaving().getSavingID()));
        }
        return retList;
    }

    private void validateAddingSavingMember(SavingMember savingMember) {
        Assert.notNull(savingMember, "Saving member must be given");
        Assert.notNull(savingMember.getMemberRole(), "Role must be given");
        Assert.notNull(savingMember.getUser(), "User must be given");
        Assert.notNull(savingMember.getSaving(), "Saving must be given");
        Optional<Saving> s = savingRepo.findById(savingMember.getSaving().getSavingID());
        if(!s.isPresent()) {
            throw new IllegalArgumentException("Saving does not exist.");
        } else {
            Optional<List<SavingMember>> savingMembersOpt = savingMemberRepo.findBySaving(s.get());
            if(savingMembersOpt.isPresent()) {
                List<SavingMember> savingMembers = savingMembersOpt.get();
                for(SavingMember savingMemberExist : savingMembers) {
                    if(savingMemberExist.getUser().getUserID().equals(savingMember.getUser().getUserID())) {
                        throw new IllegalArgumentException("This user is already member of this saving");
                    }
                }
            }
        }
    }

    private void validate(Saving saving) {
        Assert.notNull(saving, "Saving object must be given");
        Assert.notNull(saving.getEndDate(), "Saving end date must be given");
        Assert.notNull(saving.getTargetedAmount(), "Saving target amount must be given");
        Assert.notNull(saving.getName(), "Saving name must be given");
        double targetedAmount = saving.getTargetedAmount();

        if(targetedAmount <= 0) {
            throw new IllegalArgumentException("Targeted amount must be bigger than 0!");
        }
        if(saving.getEndDate().isBefore(saving.getStartDate())) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
    }
}
