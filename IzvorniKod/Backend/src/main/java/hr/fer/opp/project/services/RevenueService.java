package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.Revenue;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;

import java.util.List;
import java.util.Optional;

public interface RevenueService {

    List<Revenue> listAll();

    List<Revenue> findByUser(User user);

    Revenue createRevenue(Revenue revenue);

    Revenue fetchRevenue(long revenueID);

    Optional<Revenue> findById(long revenueID);

    Revenue updateRevenue(Revenue revenue);

    Revenue deleteRevenue(long revenueID);

    List<Revenue> findByRevenueCategory(RevenueCategory revenueCategory);

    void deleteRevenueCategoryForRevenues(long revenueCategoryID);

}