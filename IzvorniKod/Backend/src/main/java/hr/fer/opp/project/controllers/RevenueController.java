package hr.fer.opp.project.controllers;

import hr.fer.opp.project.entities.Revenue;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/revenues")
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private RevenueCategoryService revenueCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private HomeGroupService homeGroupService;



    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public Revenue getRevenue(@PathVariable("id") long id) {
        return revenueService.fetchRevenue(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public ResponseEntity<Revenue> createRevenue(@RequestBody Revenue revenue) {
        Optional<RevenueCategory> rc = revenueCategoryService.findById(revenue.getRevenueCategory().getRevenueCategoryID());
        long id = -1;
        User user = revenue.getUser();
        Optional<List<GroupMember>> groupMembersOpt = homeGroupService.findByUser(user);
        List<GroupMember> groupMembers;
        GroupMember groupMember = null;
        if(groupMembersOpt.isPresent()) {               //check if user is member of a group
            groupMembers = groupMembersOpt.get();
            for(GroupMember gm : groupMembers) {
                if(gm.getMemberRole()==MemberRole.MEMBER || gm.getMemberRole()==MemberRole.ADMIN) {
                    groupMember = gm;
                }
            }
        }

        if(rc.isPresent() && groupMember != null && (groupMember.getMemberRole().equals(MemberRole.ADMIN) //if user is member of group
                || groupMember.getMemberRole().equals(MemberRole.MEMBER))) {                              //validate revenue
            if(rc.get().getHomeGroup() == null) {
                throw new IllegalArgumentException("Revenue category does not belong to the group");
            }
            if (!groupMember.getHomeGroup().getGroupID()
                    .equals(revenue.getRevenueCategory().getHomeGroup().getGroupID())) {
                throw new IllegalArgumentException("Revenue category must belong to the right group!");
            } else {
                Revenue saved = revenueService.createRevenue(revenue);
                return ResponseEntity.created(URI.create("/" + revenue.getUser().getUserID() + "/revenues/" +
                        +saved.getRevenueID())).body(saved);
            }
        } else {                                                            //if user is not member of a group validate revenue
            if (rc.isPresent()) {
                id = rc.get().getUser().getUserID();
            } else {
                throw new RequestDeniedException("Revenue category must be given");
            }
            if (!revenue.getUser().getUserID().equals(revenue.getRevenueCategory().getUser().getUserID())
                    || !revenue.getUser().getUserID().equals(id)) {
                throw new IllegalArgumentException("Revenue category must belong to user!");
            } else {
                Revenue saved = revenueService.createRevenue(revenue);
                return ResponseEntity.created(URI.create("/" + revenue.getUser().getUserID() + "/revenues/" +
                        +saved.getRevenueID())).body(saved);
            }
        }
    }

    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public Revenue updateRevenue(@PathVariable("id") long id,
                                 @RequestBody Revenue revenue) {
        if (!revenue.getRevenueID().equals(id)) {
            throw new IllegalArgumentException("RevenueID must be preserved");
        }
        return revenueService.updateRevenue(revenue);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public Revenue deleteRevenue(@PathVariable("id") long id) {
        return revenueService.deleteRevenue(id);
    }
}
