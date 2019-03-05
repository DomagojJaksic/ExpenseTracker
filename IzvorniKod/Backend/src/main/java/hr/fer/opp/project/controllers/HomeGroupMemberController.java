package hr.fer.opp.project.controllers;

import hr.fer.opp.project.controllers.dataTransferObjects.MemberDTO;
import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.services.HomeGroupService;
import hr.fer.opp.project.services.RequestDeniedException;
import hr.fer.opp.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/groupmembers")
public class HomeGroupMemberController {

    @Autowired
    private HomeGroupService homeGroupService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public GroupMember fetchHomeGroupMember(@PathVariable("id") long id) {
        return homeGroupService.fetchGroupMember(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public GroupMember addGroupMember(@RequestBody MemberDTO memberDTO) {
        Optional<User> userOpt = userService.findByUsername(memberDTO.getUsername());
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            HomeGroup homeGroup = homeGroupService.fetchHomeGroup(memberDTO.getId());
            GroupMember groupMember = new GroupMember(user, homeGroup, MemberRole.INVITED);

            List<GroupMember> groupMembersHG = homeGroupService.fetchGroupMembersByHomeGroup(homeGroup);
            if (groupMembersHG.isEmpty()) {
                return homeGroupService.addGroupFounder(groupMember);
            } else {
                boolean alreadyExists = false;
                for (GroupMember gm : groupMembersHG) {
                    if (gm.getUser().getUserID().equals(user.getUserID())) {
                        alreadyExists = true;
                    }
                }
                if (!alreadyExists) {
                    return homeGroupService.inviteNewGroupMember(groupMember);
                } else {
                    throw new RequestDeniedException("This user is already member of the group");
                }
            }
        }
        throw new RequestDeniedException("User with username: " + memberDTO.getUsername() + "does not exist");
    }

    @PutMapping("/{username}/{groupID}/accept")
    @Secured("ROLE_USER")
    public GroupMember acceptInvitationGroupMember(@PathVariable("groupID") long groupID,
                                                   @PathVariable("username") String username) {
        GroupMember groupMemberHelp = homeGroupService.fetchGroupMemberByUsernameAndGroupID(username, groupID);
        return homeGroupService.acceptInvitation(groupMemberHelp);
    }

    @DeleteMapping("/{username}/{groupID}/decline")
    @Secured("ROLE_USER")
    public GroupMember declineInvitationGroupMember(@PathVariable("groupID") long groupID,
                                                    @PathVariable("username") String username) {
        GroupMember groupMemberHelp = homeGroupService.fetchGroupMemberByUsernameAndGroupID(username, groupID);
        return homeGroupService.declineInvitation(groupMemberHelp);
    }

    @PutMapping("/{username}/{groupID}/adminstatus")
    @Secured("ROLE_USER")
    public GroupMember changeAdminStatusGroupMember(@PathVariable("groupID") long groupID,
                                                    @PathVariable("username") String username) {
        GroupMember groupMemberHelp = homeGroupService.fetchGroupMemberByUsernameAndGroupID(username, groupID);
        return homeGroupService.changeAdminStatus(groupMemberHelp);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public GroupMember deleteGroupMember(@PathVariable("id") long id) {
        return homeGroupService.deleteGroupMember(id);
    }

    @DeleteMapping("")
    @Secured("ROLE_USER")
    public GroupMember deleteGroupMemberByUsernameAndGroupID(@RequestBody MemberDTO memberDTO) {
        GroupMember groupMember = homeGroupService.fetchGroupMemberByUsernameAndGroupID(
                memberDTO.getUsername(), memberDTO.getId());
        return homeGroupService.deleteGroupMember(groupMember.getGroupMemberID());
    }
}
