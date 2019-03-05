package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.Revenue;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.repos.RevenueRepository;
import hr.fer.opp.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RevenueServiceJpa implements RevenueService {

    @Autowired
    private RevenueRepository revenueRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private RevenueCategoryService revenueCategoryService;

    @Autowired
    private HomeGroupService homeGroupService;

    @Override
    public List<Revenue> listAll() {
        return revenueRepo.findAll();
    }

    @Override
    public List<Revenue> findByUser(User user) {
        Optional<List<Revenue>> revenuesOpt = revenueRepo.findByUser(user);
        return revenuesOpt.orElseGet(ArrayList::new);
    }

    @Override
    public Revenue createRevenue(Revenue revenue) {
        validate(revenue);
        revenue.setEntryTime(LocalDateTime.now());
        Revenue retRevenue = revenueRepo.save(revenue);
        User user = userService.fetchUser(revenue.getUser().getUserID());
        userService.updateCurrentBalance(revenue.getUser().getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) { }
        return retRevenue;
    }

    @Override
    public Revenue fetchRevenue(long revenueID) {
        Optional<Revenue> revenue = findById(revenueID);
        if(!revenue.isPresent()) {
            throw new EntityMissingException(Revenue.class, revenueID);
        }
        return revenue.get();
    }

    @Override
    public Optional<Revenue> findById(long revenueID) {
        return revenueRepo.findById(revenueID);

    }

    @Override
    public Revenue updateRevenue(Revenue revenue) {
        validate(revenue);
        Long revenueID = revenue.getRevenueID();
        if (!revenueRepo.existsById(revenueID)) {
            throw new EntityMissingException(Revenue.class, revenueID);
        }
        Revenue retRevenue = revenueRepo.save(revenue);
        User user = userService.fetchUser(revenue.getUser().getUserID());
        userService.updateCurrentBalance(revenue.getUser().getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) { }
        return retRevenue;
    }

    @Override
    public Revenue deleteRevenue(long revenueID) {
        Revenue revenue = fetchRevenue(revenueID);
        revenueRepo.delete(revenue);
        User user = userService.fetchUser(revenue.getUser().getUserID());
        userService.updateCurrentBalance(revenue.getUser().getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) { }
        return revenue;
    }

    @Override
    public List<Revenue> findByRevenueCategory(RevenueCategory revenueCategory) {
        return revenueRepo.findByRevenueCategory(revenueCategory).orElseGet(ArrayList::new);
    }

    @Override
    public void deleteRevenueCategoryForRevenues(long revenueCategoryID) {
        Optional<RevenueCategory> rc = revenueCategoryService.findById(revenueCategoryID);
        List<Revenue> revenues;
        if(rc.isPresent()) {
            revenues = findByRevenueCategory(rc.get());
            if(!revenues.isEmpty()) {
                for(Revenue r : revenues) {
                    r.setRevenueCategory(null);
                    updateRevenue(r);
                }
            }
        }
    }

    private void validate(Revenue revenue) {
        Assert.notNull(revenue, "Revenue object must be given");
        Assert.notNull(revenue.getDate(), "Revenue date must be given");
        double amount = revenue.getAmount();
        String desc = revenue.getDescription();
        Assert.hasText(desc, "Description must be given");
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount must be bigger than 0!");
        }
    }
}
