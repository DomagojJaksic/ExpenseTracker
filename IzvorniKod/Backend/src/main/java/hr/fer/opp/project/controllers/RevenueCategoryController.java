package hr.fer.opp.project.controllers;

import hr.fer.opp.project.controllers.dataTransferObjects.RevenueCategoryDTO;
import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.Revenue;
import hr.fer.opp.project.entities.RevenueCategory;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.services.RevenueCategoryService;
import hr.fer.opp.project.services.RevenueService;
import hr.fer.opp.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/revenuecategories")
public class RevenueCategoryController {

    @Autowired
    private RevenueCategoryService revenueCategoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RevenueService revenueService;


    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public RevenueCategory getRevenueCategory(@PathVariable("id") long id) {
        return revenueCategoryService.fetchRevenueCategory(id);
    }

    @PostMapping("")
    @Secured("ROLE_USER")
    public ResponseEntity<RevenueCategory> createRevenueCategory(@RequestBody RevenueCategory revenueCategory) {
        List<RevenueCategory> revenueCategories = new ArrayList<>();
        if(revenueCategory.getUser() != null && revenueCategory.getHomeGroup() == null) {
            revenueCategories = revenueCategoryService.findByUser(revenueCategory.getUser());
        } else if(revenueCategory.getUser() == null && revenueCategory.getHomeGroup() != null) {
            revenueCategories = revenueCategoryService.findByHomeGroup(revenueCategory.getHomeGroup());
        }
        for(RevenueCategory rc : revenueCategories) {
            if(rc.getName().equals(revenueCategory.getName())) {
                throw new IllegalArgumentException("You already have revenue category named: " + revenueCategory.getName());
            }
        }
        RevenueCategory saved = revenueCategoryService.createRevenueCategory(revenueCategory);
        return ResponseEntity.created(URI.create("/revenuecategories/" + saved.getRevenueCategoryID())).body(saved);
    }



    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public RevenueCategory updateRevenueCategory(@PathVariable("id") long id,
                                                 @RequestBody RevenueCategory revenueCategory) {
        if (!revenueCategory.getRevenueCategoryID().equals(id)) {
            throw new IllegalArgumentException("RevenueCategoryID must be preserved");
        }
        return revenueCategoryService.updateRevenueCategory(revenueCategory);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    public RevenueCategory deleteRevenueCategory(@PathVariable("id") long id) {
        return revenueCategoryService.deleteRevenueCategory(id);
    }
}
