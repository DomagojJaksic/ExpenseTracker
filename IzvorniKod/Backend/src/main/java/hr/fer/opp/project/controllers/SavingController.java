package hr.fer.opp.project.controllers;


import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.SavingTransaction;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.SavingMember;
import hr.fer.opp.project.services.RequestDeniedException;
import hr.fer.opp.project.services.SavingService;
import hr.fer.opp.project.services.SavingTransactionService;
import hr.fer.opp.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/savings")
public class SavingController {

    @Autowired
    private SavingService savingService;

    @Autowired
    private UserService userService;

    @Autowired
    private SavingTransactionService savingTransactionService;

    @GetMapping("")
    public List<Saving> listSavings() {
        return savingService.listAll();
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public Saving fetchSaving(@PathVariable("id") long id) {
        return savingService.fetchSaving(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public ResponseEntity<Saving> createSaving(@RequestBody Saving saving) {
        User user = userService.fetchCurrentUser();
        Saving saved = savingService.createSaving(saving, user);
        return ResponseEntity.created(URI.create("/savings/" + saved.getSavingID())).body(saved);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public Saving updateSaving(@PathVariable("id") Long id, @RequestBody Saving saving) {
        if (!saving.getSavingID().equals(id)) {
            throw new IllegalArgumentException("SavingID must be preserved");
        }
        return savingService.updateSaving(saving);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public Saving deleteSaving(@PathVariable("id") long id) {
        return savingService.deleteSaving(id);
    }

    @GetMapping("/{id}/savingmembers")
    @Secured("ROLE_USER")
    public List<SavingMember> fetchSavingMembers(@PathVariable("id") long id) {
        Saving saving = savingService.fetchSaving(id);
        return savingService.findSavingMembersBySaving(saving);
    }

    @GetMapping("/{id}/users")
    @Secured("ROLE_USER")
    public List<User> fetchUsers(@PathVariable("id") long id) {
        Saving saving = savingService.fetchSaving(id);
        return savingService.findUsersBySaving(saving);
    }

    @GetMapping("/{id}/invited")
    @Secured("ROLE_USER")
    public List<User> fetchInvitedUsers(@PathVariable("id") long id) {
        Saving saving = savingService.fetchSaving(id);
        return savingService.findInvitedUsersBySaving(saving);
    }

    @GetMapping("/{id}/admins")
    @Secured("ROLE_USER")
    public List<User> fetchAdminUsers(@PathVariable("id") long id) {
        Saving saving = savingService.fetchSaving(id);
        return savingService.findAdminUsersBySaving(saving);
    }

    @GetMapping("/{id}/transactions")
    @Secured("ROLE_USER")
    public List<SavingTransaction> getSavingTransactions(@PathVariable("id") long id) {
        Saving saving = fetchSaving(id);
        return savingTransactionService.findBySaving(saving);
    }

    @GetMapping("/{savingID}/{username}")
    @Secured("ROLE_USER")
    public SavingMember fetchSavingMember(@PathVariable("savingID") long savingID,
                                          @PathVariable("username") String username) {
        return savingService.fetchSavingMemberByUsernameAndSavingID(savingID, username);
    }
    @GetMapping("/{id}/isAdmin/{username}")
    @Secured("ROLE_USER")
    public User isAdmin(@PathVariable("id") long id, @PathVariable("username") String username) {
        User user = userService.findByUsername(username).orElseThrow(
                () -> new RequestDeniedException("User with this username does not exist"));
        Saving saving = savingService.fetchSaving(id);
        return savingService.isAdmin(saving, user);
    }

}
