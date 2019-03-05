package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;

import java.util.List;
import java.util.Optional;

public interface RevenueCategoryService {

    List<RevenueCategory> listAll();

    List<RevenueCategory> findByUser(User user);

    List<RevenueCategory> findByHomeGroup(HomeGroup homeGroup);

    RevenueCategory createRevenueCategory(RevenueCategory revenueCategory);

    RevenueCategory fetchRevenueCategory(long revenueCategoryID);

    Optional<RevenueCategory> findById(long revenueCategoryID);

    RevenueCategory updateRevenueCategory(RevenueCategory revenueCategory);

    RevenueCategory deleteRevenueCategory(long revenueCategoryID);
}
