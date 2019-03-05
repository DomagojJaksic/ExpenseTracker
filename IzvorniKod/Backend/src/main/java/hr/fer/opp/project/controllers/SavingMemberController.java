package hr.fer.opp.project.controllers;

import hr.fer.opp.project.controllers.dataTransferObjects.MemberDTO;
import hr.fer.opp.project.entities.Saving;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.SavingMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.services.RequestDeniedException;
import hr.fer.opp.project.services.SavingService;
import hr.fer.opp.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/savingmembers")
public class SavingMemberController {

    @Autowired
    private SavingService savingService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public SavingMember fetchSavingMember(@PathVariable("id") long id) {
        return savingService.fetchSavingMember(id);
    }


    @PostMapping("/add")
    @Secured("ROLE_USER")
    public SavingMember addSavingMember(@RequestBody MemberDTO memberDTO) {
        Long savingID = memberDTO.getId();
        Saving saving = savingService.fetchSaving(savingID);
        List<SavingMember> savingMembers = savingService.findSavingMembersBySaving(saving);
        Optional<User> userOpt = userService.findByUsername(memberDTO.getUsername());
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            SavingMember savingMember = new SavingMember(user,saving,MemberRole.INVITED);
            if (savingMembers.isEmpty()) {
                return savingService.addFounder(savingMember);
            } else {
                boolean alreadyExists = false;

                for (SavingMember sm : savingMembers) {
                    if (sm.getUser().getUserID().equals(user.getUserID())) {
                        alreadyExists = true;
                    }
                }
                if (!alreadyExists) {
                    return savingService.inviteNewMember(savingMember);
                } else {
                    throw new RequestDeniedException("This user is already member of the group");
                }
            }
        }
        throw new RequestDeniedException("User with username: "
                + memberDTO.getUsername() + "does not exist.");
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public SavingMember updateSavingMember(@RequestBody SavingMember savingMember, @PathVariable("id") long id) {
        if(!savingMember.getSavingMemberID().equals(id)) {
            throw new RequestDeniedException("Wrong id given");
        } else {
            return savingService.updateSavingMember(savingMember);
        }
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public SavingMember deleteSavingMember(@PathVariable("id") long id) {
        return savingService.deleteMember(id);
    }

    @DeleteMapping("")
    @Secured("ROLE_USER")
    public SavingMember deleteSavingMemberByUsernameAndSavingID(@RequestBody MemberDTO memberDTO) {
        SavingMember savingMember = savingService.fetchSavingMemberByUsernameAndSavingID(
                memberDTO.getId(), memberDTO.getUsername());
        return savingService.deleteMember(savingMember.getSavingMemberID());
    }

    @PutMapping("/{username}/{id}/accept")
    @Secured("ROLE_USER")
    public SavingMember acceptInvitationSavingMember(@PathVariable("id") long id,
                                                     @PathVariable("username") String username) {
        SavingMember savingMember = savingService.fetchSavingMemberByUsernameAndSavingID(id, username);
        return savingService.acceptInvitation(savingMember);
    }

    @DeleteMapping("/{username}/{id}/decline")
    @Secured("ROLE_USER")
    public SavingMember declineInvitationSavingMember(@PathVariable("id") long id,
                                                      @PathVariable("username") String username) {
        SavingMember savingMember = savingService.fetchSavingMemberByUsernameAndSavingID(id, username);
        return savingService.declineInvitation(savingMember);
    }

    @PutMapping("/{username}/{id}/adminstatus")
    @Secured("ROLE_USER")
    public SavingMember changeAdminStatusSavingMember(@PathVariable("id") long id,
                                                      @PathVariable("username") String username) {
        SavingMember savingMember = savingService.fetchSavingMemberByUsernameAndSavingID(id, username);
        return savingService.changeAdminStatus(savingMember);
    }

}
