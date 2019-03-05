package hr.fer.opp.project.controllers;

import hr.fer.opp.project.controllers.dataTransferObjects.IsVerifiedDTO;
import hr.fer.opp.project.controllers.dataTransferObjects.TimePeriodDTO;
import hr.fer.opp.project.controllers.dataTransferObjects.UserVerificationDTO;
import hr.fer.opp.project.entities.*;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.entities.complexEntities.SavingMember;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.enums.TimePeriod;
import hr.fer.opp.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private SavingService savingService;

    @Autowired
    private SavingTransactionService savingTransactionService;

    @Autowired
    private HomeGroupService homeGroupService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private RevenueCategoryService revenueCategoryService;

    @Autowired
    private MailService mailService;

    @GetMapping("")
    @Secured("ROLE_USER")
    public List<User> listUsers() {
        return userService.listAll();
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public User getUser(@PathVariable("id") long id) {
        return userService.fetchUser(id);
    }

    @GetMapping("/username/{username}")
    public User usernameExists(@PathVariable("username") String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        if(userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new RequestDeniedException("User with that username does not exist.");
    }

    @GetMapping("/email/{email}")
    public User emailExists(@PathVariable("email") String email) {
        Optional<User> userOpt = userService.findByEmail(email);
        if(userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new RequestDeniedException("User with that email does not exist.");
    }

    @PostMapping("/verified")
    public ResponseEntity isUserVerified(@RequestBody IsVerifiedDTO isVerifiedDTO) {
        Optional<User> userOpt = userService.findByUsername(isVerifiedDTO.getUsername());
        if(userOpt.isPresent()) {
            if(userOpt.get().isEmailVerified()) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/current")
    @Secured("ROLE_USER")
    public User getCurrentUsername() {
        return userService.fetchCurrentUser();
    }


    @PostMapping("/registration")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerificationCode(UUID.randomUUID().toString());
        user.setLastHomeGroupMembershipChangeTime(LocalDateTime.now());

        User saved = userService.createUser(user);

        try {
            if(!saved.isEmailVerified()) {
                mailService.sendNotification(saved);
            }
        } catch (MailException ex) {
            throw new RequestDeniedException(ex.getMessage());
        }

        return ResponseEntity.created(URI.create("/users/" + saved.getUsername())).body(saved);
    }

    @PostMapping("/registration/verify")
    public User verifyUserMail(@RequestBody UserVerificationDTO userVerificationDTO) {
        Assert.notNull(userVerificationDTO, "UserVerificationDTO must not be null");
        Assert.notNull(userVerificationDTO.getUsername(), "Username must be given");
        Assert.notNull(userVerificationDTO.getCode(), "Code must be given");

        Optional<User> userOpt = userService.findByUsername(userVerificationDTO.getUsername());
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if(user.getVerificationCode().equals(userVerificationDTO.getCode().trim())) {
                return userService.verifyMail(user);
            }
        }
        throw new RequestDeniedException("Entered code is not correct");
    }


    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        if (!user.getUserID().equals(id)) {
            throw new IllegalArgumentException("UserID must be preserved");
        }
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/password")
    public User updatePassword(@PathVariable("id") Long id, @RequestBody User user) {
        if (!user.getUserID().equals(id)) {
            throw new IllegalArgumentException("UserID must be preserved");
        }
        return userService.updatePassword(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable("id") long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}/savings")
    @Secured("ROLE_USER")
    public List<Saving> listUsersSavings(@PathVariable("id") long id) {
        User user = userService.fetchUser(id);
        return savingService.findSavingsByUser(user);
    }

    @GetMapping("/{id}/group")
    @Secured("ROLE_USER")
    public HomeGroup fetchHomeGroup(@PathVariable("id") long id) {
        User user = userService.fetchUser(id);
        return homeGroupService.fetchHomeGroupByUser(user);
    }

    @GetMapping("/{id}/notifications")
    @Secured("ROLE_USER")
    public List<Notification> fetchNotifications(@PathVariable("id") long id) {
        User user = userService.fetchUser(id);
        return notificationService.fetchByUser(user);
    }

    @GetMapping("{id}/expensecategories")
    @Secured("ROLE_USER")
    public List<ExpenseCategory> findExpenseCategoryByUser(@PathVariable("id") long id) {
        Optional<User> optUser = userService.findById(id);
        User user = null;
        if(optUser.isPresent()) {
            user = optUser.get();
        }
        return userService.fetchExpenseCategories(user);
    }

    @GetMapping("{id}/revenuecategories")
    @Secured("ROLE_USER")
    public List<RevenueCategory> findRevenueCategoryByUser(@PathVariable("id") long id) {
        Optional<User> optUser = userService.findById(id);
        User user = null;
        if(optUser.isPresent()) {
            user = optUser.get();
        }
        return userService.fetchRevenueCategories(user);
    }

    @PostMapping("/{id}/revenues")
    @Secured("ROLE_USER")
    public List<Revenue> findRevenuesByUser(@PathVariable("id") long id, @RequestBody TimePeriodDTO timePeriodDTO) {
        Optional<User> optUser = userService.findById(id);
        User user = optUser.orElseThrow(() -> new RequestDeniedException("User does not exist."));
        return userService.fetchRevenues(user, timePeriodDTO.getTimePeriod());
    }

    @PostMapping("/{id}/expenses")
    @Secured("ROLE_USER")
    public List<Expense> findExpensesByUser(@PathVariable("id") long id, @RequestBody TimePeriodDTO timePeriodDTO) {
        Optional<User> optUser = userService.findById(id);
        User user = optUser.orElseThrow(() -> new RequestDeniedException("User does not exist."));
        return userService.fetchExpenses(user, timePeriodDTO.getTimePeriod());

    }

    @GetMapping("/{id}/groupInvitations")
    @Secured("ROLE_USER")
    public List<HomeGroup> findGroupInvitations(@PathVariable("id") long id) {
        User user = userService.fetchUser(id);
        try {
            homeGroupService.fetchHomeGroupByUser(user);
            return new ArrayList<>();
        } catch(Exception ignore) {}

        return homeGroupService.findInvitationsByUser(user);
    }

    @GetMapping("/{id}/savingInvitations")
    @Secured("ROLE_USER")
    public List<Saving> findSavingInvitations(@PathVariable("id") long id) {
        User user = userService.fetchUser(id);

        return savingService.findInvitationsByUser(user);
    }
}
