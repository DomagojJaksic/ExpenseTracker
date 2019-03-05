package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.SavingTransaction;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.enums.SavingTransactionType;
import hr.fer.opp.project.repos.SavingTransactionRepository;
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
public class SavingTransactionServiceJpa implements SavingTransactionService {

    @Autowired
    private SavingTransactionRepository savingTransactionRepo;

    @Autowired
    private SavingService savingService;

    @Autowired
    private UserService userService;

    @Autowired
    private HomeGroupService homeGroupService;

    @Override
    public List<SavingTransaction> findBySaving(Saving saving) {
        Optional<List<SavingTransaction>> savingTransactionsOpt = savingTransactionRepo.findBySaving(saving);
        return savingTransactionsOpt.orElseGet(ArrayList::new);
    }

    @Override
    public SavingTransaction fetchSavingTransaction(long id) {
        Optional<SavingTransaction> savingTransaction = findById(id);
        if(savingTransaction.isPresent()) {
            return savingTransaction.get();
        } else {
            throw new EntityMissingException(SavingTransaction.class, id);
        }
    }

    @Override
    public Optional<SavingTransaction> findById(long id) {
        return savingTransactionRepo.findById(id);
    }

    @Override
    public List<SavingTransaction> findByUser(User user) {
        Optional<List<SavingTransaction>> savingTransactionsOpt = savingTransactionRepo.findByUser(user);
        return savingTransactionsOpt.orElseGet(ArrayList::new);
    }

    @Override
    public List<SavingTransaction> findByUserAndSaving(User user, Saving saving) {
        List<SavingTransaction> savingTransactionsOfSaving = findBySaving(saving);
        List<SavingTransaction> retList = new ArrayList<>();
        for(SavingTransaction savingTransaction : savingTransactionsOfSaving) {
            if(savingTransaction.getUser().getUserID().equals(user.getUserID())) {
                retList.add(savingTransaction);
            }
        }
        return retList;
    }

    @Override
    public SavingTransaction addSavingTransaction(SavingTransaction savingTransaction) {
        validate(savingTransaction);
        savingTransaction.setTime(LocalDate.now());
        savingTransaction.setEntryTime(LocalDateTime.now());
        SavingTransaction retSavingTransaction = savingTransactionRepo.save(savingTransaction);
        User user = userService.fetchUser(retSavingTransaction.getUser().getUserID());
        updateSavingAmount(savingTransaction.getSaving().getSavingID());
        userService.updateCurrentBalance(user.getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) { }
        return retSavingTransaction;
    }

    @Override
    public SavingTransaction updateSavingTransaction(SavingTransaction savingTransaction) {
        validate(savingTransaction);
        User user = userService.fetchUser(savingTransaction.getUser().getUserID());
        Optional<SavingTransaction> sm = savingTransactionRepo.findById(savingTransaction.getSavingTransactionID());
        if(sm.isPresent()) {
            savingTransaction.setEntryTime(sm.get().getEntryTime());
            if(!savingTransaction.getEntryTime().isBefore(user.getLastHomeGroupMembershipChangeTime())) {
                SavingTransaction retSavingTransaction = savingTransactionRepo.save(savingTransaction);
                updateSavingAmount(retSavingTransaction.getSaving().getSavingID());
                userService.updateCurrentBalance(user.getUserID());
                try {
                    homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
                } catch (RequestDeniedException ignore) {
                }
                return retSavingTransaction;
            } else {
                throw new RequestDeniedException("It is not possible to edit this saving transaction.");
            }
        } else {
            throw new EntityMissingException(SavingTransaction.class, savingTransaction.getSavingTransactionID());
        }
    }

    @Override
    public SavingTransaction deleteSavingTransaction(long id) {
        SavingTransaction savingTransaction = fetchSavingTransaction(id);
        User user = userService.fetchUser(savingTransaction.getUser().getUserID());
        Saving saving = savingService.fetchSaving(savingTransaction.getSaving().getSavingID());
        if(!checkIfUserContributionIsOK(savingTransaction)) {
            throw new RequestDeniedException("You are deleting more money than there is left");
        }
        savingTransactionRepo.delete(savingTransaction);
        updateSavingAmount(saving.getSavingID());
        userService.updateCurrentBalance(user.getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) { }
        return savingTransaction;
    }

    @Override
    public SavingTransaction deleteSavingTransactionWhenDeletingSaving(long id) {
        SavingTransaction savingTransaction = fetchSavingTransaction(id);
        User user = userService.fetchUser(savingTransaction.getUser().getUserID());
        Saving saving = savingService.fetchSaving(savingTransaction.getSaving().getSavingID());
        savingTransactionRepo.delete(savingTransaction);
        return savingTransaction;
    }


    @Override
    public void updateSavingAmount(long savingID) {
        Saving saving = savingService.fetchSaving(savingID);
        List<SavingTransaction> savingTransactions = findBySaving(saving);
        double amount = 0;
        for(SavingTransaction savingTransaction : savingTransactions) {
            if(savingTransaction.getType().equals(SavingTransactionType.DEPOSIT)) {
                amount += savingTransaction.getAmount();
            } else if(savingTransaction.getType().equals(SavingTransactionType.WITHDRAW)) {
                amount -= savingTransaction.getAmount();
            }
        }
        saving.setCurrentBalance(amount);
        savingService.updateSaving(saving);
    }

    @Override
    public boolean isEditable(long savingTransactionID) {
        SavingTransaction savingTransaction = fetchSavingTransaction(savingTransactionID);
        User user = userService.fetchUser(savingTransaction.getUser().getUserID());

        return user.getLastHomeGroupMembershipChangeTime().isBefore(savingTransaction.getEntryTime());
    }

    private void validate(SavingTransaction savingTransaction) {
        long id = -1;
        if(savingTransaction.getSavingTransactionID() != null) {
            id = savingTransaction.getSavingTransactionID();
        }

        Assert.notNull(savingTransaction,"Saving transaction must not be null");
        Assert.notNull(savingTransaction.getSaving(), "Saving transaction must be defined for a saving");
        Assert.notNull(savingTransaction.getUser(), "Saving transaction user must not be null");
        Assert.notNull(savingTransaction.getType(), "Type must be determined");

        Optional<Saving> saving = savingService.findById(savingTransaction.getSaving().getSavingID());
        Optional<User> user = userService.findById(savingTransaction.getUser().getUserID());

        if(!saving.isPresent()) {
            throw new EntityMissingException(Saving.class, savingTransaction.getSaving().getSavingID());
        }
        if(!user.isPresent()) {
            throw new EntityMissingException(User.class, savingTransaction.getUser().getUserID());
        }
        if(savingTransaction.getAmount() <= 0) {
            throw new RequestDeniedException("Amount must be higher than 0");
        }

        List<SavingTransaction> savingTransactions =
                findByUserAndSaving(savingTransaction.getUser(), savingTransaction.getSaving());
        double userContribution = 0.;
        for(SavingTransaction st : savingTransactions) {
            if(st.getSavingTransactionID() != id) {
                if (st.getType().equals(SavingTransactionType.DEPOSIT)) {
                    userContribution += st.getAmount();
                } else {
                    userContribution -= st.getAmount();
                }
            }
        }
        if(savingTransaction.getAmount() > userContribution
                && savingTransaction.getType().equals(SavingTransactionType.WITHDRAW)) {
            throw new RequestDeniedException("User contributions must be bigger or equal than withdrawal.");
        }
        if(savingTransaction.getAmount()+userContribution < 0
                && savingTransaction.getType().equals(SavingTransactionType.DEPOSIT)) {
            throw new RequestDeniedException("User contributions must bigger or equal than zero");
        }
    }

    private boolean checkIfUserContributionIsOK(SavingTransaction savingTransaction) {
        List<SavingTransaction> savingTransactions =
                findByUserAndSaving(savingTransaction.getUser(), savingTransaction.getSaving());
        double userContribution = 0.;
        for(SavingTransaction st : savingTransactions) {
            if(!st.getSavingTransactionID().equals(savingTransaction.getSavingTransactionID())) {
                if (st.getType().equals(SavingTransactionType.DEPOSIT)) {
                    userContribution += st.getAmount();
                } else {
                    userContribution -= st.getAmount();
                }
            }
        }
        return !(userContribution < 0);
    }


}
