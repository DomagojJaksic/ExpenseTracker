package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.SavingTransaction;
import hr.fer.opp.project.entities.User;

import java.util.List;
import java.util.Optional;

public interface SavingTransactionService {

    List<SavingTransaction> findBySaving(Saving saving);

    SavingTransaction fetchSavingTransaction(long id);

    Optional<SavingTransaction> findById(long id);

    List<SavingTransaction> findByUser(User user);

    List<SavingTransaction> findByUserAndSaving(User user, Saving saving);

    SavingTransaction addSavingTransaction(SavingTransaction savingTransaction);

    SavingTransaction updateSavingTransaction(SavingTransaction savingTransaction);

    SavingTransaction deleteSavingTransaction(long id);

    void updateSavingAmount(long savingID);

    boolean isEditable(long savingTransactionID);

    SavingTransaction deleteSavingTransactionWhenDeletingSaving(long id);
}