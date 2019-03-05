package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.repos.RevenueCategoryRepository;
import hr.fer.opp.project.services.EntityMissingException;
import hr.fer.opp.project.services.RequestDeniedException;
import hr.fer.opp.project.services.RevenueCategoryService;
import hr.fer.opp.project.services.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RevenueCategoryServiceJpa implements RevenueCategoryService {

    @Autowired
    private RevenueCategoryRepository revenueCategoryRepo;

    @Autowired
    private RevenueService revenueService;

    @Override
    public List<RevenueCategory> listAll() {
        return revenueCategoryRepo.findAll();
    }

    @Override
    public List<RevenueCategory> findByUser(User user) {
        Optional<List<RevenueCategory>> revenueCategories = revenueCategoryRepo.findByUser(user);
        return revenueCategories.orElseGet(ArrayList::new);
    }

    @Override
    public List<RevenueCategory> findByHomeGroup(HomeGroup homeGroup) {
        Optional<List<RevenueCategory>> revenueCategories = revenueCategoryRepo.findByHomeGroup(homeGroup);
        return revenueCategories.orElseGet(ArrayList::new);
    }

    @Override
    public RevenueCategory createRevenueCategory(RevenueCategory revenueCategory) {
        validate(revenueCategory);
        Assert.isNull(revenueCategory.getRevenueCategoryID(), "RevenueCategoryID needs to be null!");
        return revenueCategoryRepo.save(revenueCategory);
    }

    @Override
    public RevenueCategory fetchRevenueCategory(long revenueCategoryID) {
        return findById(revenueCategoryID).orElseThrow(
            () -> new EntityMissingException(ExpenseCategory.class, revenueCategoryID)
        );
    }

    @Override
    public Optional<RevenueCategory> findById(long revenueCategoryID) {
        return revenueCategoryRepo.findById(revenueCategoryID);
    }


    @Override
    public RevenueCategory updateRevenueCategory(RevenueCategory revenueCategory) {
        validate(revenueCategory);
        Long revenueCategoryID = revenueCategory.getRevenueCategoryID();
        if (!revenueCategoryRepo.existsById(revenueCategoryID)) {
            throw new EntityMissingException(RevenueCategory.class, revenueCategoryID);
        }
        return revenueCategoryRepo.save(revenueCategory);
    }

    @Override
    public RevenueCategory deleteRevenueCategory(long revenueCategoryID) {
        RevenueCategory revenueCategory = fetchRevenueCategory(revenueCategoryID);
        revenueService.deleteRevenueCategoryForRevenues(revenueCategoryID);
        revenueCategoryRepo.delete(revenueCategory);
        return revenueCategory;
    }

    private void validate(RevenueCategory revenueCategory) {
        Assert.notNull(revenueCategory, "Revenue category object must be given");
        String revenueCategoryName = revenueCategory.getName();
        Assert.hasText(revenueCategoryName, "Name must be given");
        if(revenueCategory.getHomeGroup() == null && revenueCategory.getUser() == null) {
            throw new IllegalArgumentException("User or group must be given");
        }
    }
}
