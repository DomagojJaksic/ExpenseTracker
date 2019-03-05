package hr.fer.opp.project.controllers;

import hr.fer.opp.project.entities.SavingTransaction;
import hr.fer.opp.project.services.RequestDeniedException;
import hr.fer.opp.project.services.SavingTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class SavingTransactionController {

    @Autowired
    private SavingTransactionService savingTransactionService;

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public SavingTransaction getSavingTransaction(@PathVariable("id") long id) {
        return savingTransactionService.fetchSavingTransaction(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public SavingTransaction addSavingTransactions(@RequestBody SavingTransaction savingTransaction) {
        return savingTransactionService.addSavingTransaction(savingTransaction);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public SavingTransaction updateSavingTransactions(@PathVariable("id") long id,
                                                      @RequestBody SavingTransaction savingTransaction) {
        if(!savingTransaction.getSavingTransactionID().equals(id)) {
            throw new RequestDeniedException("Saving transaction id is not given correct");
        }
        return savingTransactionService.updateSavingTransaction(savingTransaction);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public SavingTransaction deleteSavingTransactions(@PathVariable("id") long id) {
        return savingTransactionService.deleteSavingTransaction(id);
    }

    @GetMapping("/{id}/editable")
    @Secured("ROLE_USER")
    public ResponseEntity isEditable(@PathVariable("id") long id) {
        if(savingTransactionService.isEditable(id)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
