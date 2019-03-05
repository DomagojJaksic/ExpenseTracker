package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.*;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import hr.fer.opp.project.enums.NotificationType;
import hr.fer.opp.project.enums.SavingTransactionType;
import hr.fer.opp.project.enums.TimePeriod;
import hr.fer.opp.project.repos.UserRepository;
import hr.fer.opp.project.services.*;
import org.apache.commons.validator.routines.EmailValidator;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceJpa implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private HomeGroupService homeGroupService;

    @Autowired
    private RevenueCategoryService revenueCategoryService;

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SavingTransactionService savingTransactionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> listAll() {
        return userRepo.findAll();
    }

    @Override
    public User createUser(User user) {
        validate(user);
        Assert.isNull(user.getUserID(), "UserID needs to be null!");
        if (userRepo.countByUsername(user.getUsername()) > 0) {
            throw new RequestDeniedException("User with username " + user.getUsername() + " already exists!");
        }
        if(userRepo.countByEmail(user.getEmail()) > 0) {
            throw new RequestDeniedException("User with email " + user.getEmail() + " already exists!");

        }
        return userRepo.save(user);
    }

    @Override
    public User fetchUser(long userID) {
        return findById(userID).orElseThrow(
                () -> new EntityMissingException(User.class, userID)
        );

    }

    @Override
    public User fetchCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return findByUsername(username).orElseThrow(() -> new RequestDeniedException("User with username "
                + username + "does not exist!"));
    }

    @Override
    public Optional<User> findById(long userID) {
        return userRepo.findById(userID);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Assert.notNull(username, "Username must be given!");
        return userRepo.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Assert.notNull(email, "E-mail must be given!");
        return userRepo.findByEmail(email);
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        Long userID = user.getUserID();
        if (!userRepo.existsById(userID)) {
            throw new EntityMissingException(User.class, userID);
        }
        if (userRepo.existsByUsernameAndUserIDNot(user.getUsername(), userID)) {
            throw new RequestDeniedException(
                    "User with username " + user.getUsername() + " already exists!"
            );
        }
        return userRepo.save(user);
    }

    @Override
    public User updatePassword(User user) {
        validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Long userID = user.getUserID();
        if (!userRepo.existsById(userID)) {
            throw new EntityMissingException(User.class, userID);
        }
        if (userRepo.existsByUsernameAndUserIDNot(user.getUsername(), userID)) {
            throw new RequestDeniedException(
                    "User with username " + user.getUsername() + " already exists!"
            );
        }
        return userRepo.save(user);
    }

    @Override
    public User verifyMail(User user) {
        Long userID = user.getUserID();
        if (!userRepo.existsById(userID)) {
            throw new EntityMissingException(User.class, userID);
        }
        user.setEmailVerified(true);
        return userRepo.save(user);
    }

    @Override
    public User deleteUser(long userID) {
        User user = fetchUser(userID);
        userRepo.delete(user);
        return user;

    }

    @Override
    public void updateCurrentBalance(long userID) {
        User user = fetchUser(userID);
        Double startBalance = user.getCurrentBalance();
        List<Revenue> revenues = revenueService.findByUser(user);
        List<Expense> expenses = expenseService.findByUser(user);
        List<SavingTransaction> savingTransactions = savingTransactionService.findByUser(user);
        double amount = 0.;
        for(Revenue revenue : revenues) {
            if(revenue.getEntryTime().isAfter(user.getLastHomeGroupMembershipChangeTime())) {
                amount += revenue.getAmount();
            }
        }
        for(Expense expense : expenses) {
            if(expense.getEntryTime().isAfter(user.getLastHomeGroupMembershipChangeTime())) {
                amount -= expense.getAmount();
            }
        }
        for(SavingTransaction savingTransaction : savingTransactions) {
            if(savingTransaction.getEntryTime().isAfter(user.getLastHomeGroupMembershipChangeTime())) {
                if(savingTransaction.getType().equals(SavingTransactionType.DEPOSIT)) {
                    amount -= savingTransaction.getAmount();
                } else if (savingTransaction.getType().equals(SavingTransactionType.WITHDRAW)) {
                    amount += savingTransaction.getAmount();
                }
            }
        }
        user.setCurrentBalance(amount);
        user = updateUser(user);
        HomeGroup homeGroup = null;
        try {
            homeGroup = homeGroupService.fetchHomeGroupByUser(user);
        } catch(Exception ignore) {}
        if(homeGroup == null) {
            if(user.getCurrentBalance() < 0 && user.getCurrentBalance() < startBalance) {
                Notification notification = notificationService.createNotification(
                        new Notification(NotificationType.BUDGET_MINUS,
                        "Stanje Vaše kućne blagajne je trenutačno manje od 0 kuna.",
                        LocalDateTime.now()));
                notificationService.createNotifiedUser(new NotifiedUser(user, notification));
            }
        }
    }

    @Override
    public List<Expense> fetchExpenses(User user, String timePeriod) {
        TimePeriod period = checkTimePeriod(timePeriod);
        LocalDate targetDate = calculateTargetDate(period);
        List<Expense> expenses = new ArrayList<>();
        HomeGroup homeGroup = null;
        try {
            homeGroup = homeGroupService.fetchHomeGroupByUser(user);
        } catch (Exception ignore) { }
        List<Expense> expenseList;
        if(homeGroup != null) {
            expenseList = homeGroupService.fetchExpensesByHomeGroup(homeGroup);
            for(Expense expense : expenseList) {
                if(expense.getDate().isAfter(targetDate)) {
                    expenses.add(expense);
                }
            }
            return expenses;
        } else {
            expenseList = expenseService.findByUser(user);
            for(Expense expense : expenseList) {
                if(expense.getEntryTime().isAfter(user.getLastHomeGroupMembershipChangeTime())
                        && expense.getDate().isAfter(targetDate)) {
                    expenses.add(expense);
                }
            }

        }

        return expenses;
    }



    @Override
    public List<Revenue> fetchRevenues(User user, String timePeriod) {
        TimePeriod period = checkTimePeriod(timePeriod);
        LocalDate targetDate = calculateTargetDate(period);
        List<Revenue> revenues = new ArrayList<>();
        HomeGroup homeGroup = null;
        try {
            homeGroup = homeGroupService.fetchHomeGroupByUser(user);
        } catch (Exception ignore) { }

        List<Revenue> revenueList;
        if(homeGroup != null) {
            revenueList = homeGroupService.fetchRevenuesByHomeGroup(homeGroup);
            for(Revenue revenue : revenueList) {
                if(revenue.getDate().isAfter(targetDate)){
                    revenues.add(revenue);
                }
            }
        } else {
            revenueList = revenueService.findByUser(user);
            for(Revenue revenue : revenueList) {
                if(revenue.getEntryTime().isAfter(user.getLastHomeGroupMembershipChangeTime())
                        && revenue.getDate().isAfter(targetDate)){
                    revenues.add(revenue);
                }
            }
        }

        return revenues;
    }

    @Override
    public List<ExpenseCategory> fetchExpenseCategories(User user) {
        List<ExpenseCategory> expenseCategories;
        HomeGroup homeGroup = null;
        try {
            homeGroup = homeGroupService.fetchHomeGroupByUser(user);
        } catch (Exception ignore) { }

        if(homeGroup != null) {
            expenseCategories = expenseCategoryService.findByHomeGroup(homeGroup);
        } else {
            expenseCategories = expenseCategoryService.findByUser(user);
        }
        return expenseCategories;
    }

    @Override
    public List<RevenueCategory> fetchRevenueCategories(User user) {
        List<RevenueCategory> revenueCategories;
        HomeGroup homeGroup = null;
        try {
            homeGroup = homeGroupService.fetchHomeGroupByUser(user);
        } catch (Exception ignore) { }

        if(homeGroup != null) {
            revenueCategories = revenueCategoryService.findByHomeGroup(homeGroup);
        } else {
            revenueCategories = revenueCategoryService.findByUser(user);
        }
        return revenueCategories;
    }

    private void validate(User user) {
        Assert.notNull(user, "User object must be given");
        String username = user.getUsername();
        Assert.hasText(username, "Username must be given");
        Assert.hasText(user.getEmail(), "Email address must be given");
        Assert.isTrue(EmailValidator.getInstance()
                                    .isValid(user.getEmail()), "Email address format is not valid.");
    }

    private TimePeriod checkTimePeriod(String timePeriod) {
        TimePeriod resultTimePeriod = TimePeriod.ALL_TIME;

        if(timePeriod.equals(TimePeriod.ONE_WEEK.toString())) {
            resultTimePeriod = TimePeriod.ONE_WEEK;
        } else if(timePeriod.equals(TimePeriod.ONE_MONTH.toString())) {
            resultTimePeriod = TimePeriod.ONE_MONTH;
        } else if(timePeriod.equals(TimePeriod.THREE_MONTHS.toString())) {
            resultTimePeriod = TimePeriod.THREE_MONTHS;
        } else if(timePeriod.equals(TimePeriod.SIX_MONTHS.toString())) {
            resultTimePeriod = TimePeriod.SIX_MONTHS;
        } else if(timePeriod.equals(TimePeriod.ONE_YEAR.toString())) {
            resultTimePeriod = TimePeriod.ONE_YEAR;
        }

        return resultTimePeriod;
    }

    private LocalDate calculateTargetDate(TimePeriod period) {
        LocalDate targetDate;

        if(period.equals(TimePeriod.ONE_WEEK)) {
            targetDate = LocalDate.now().minusDays(7);
        } else if(period.equals(TimePeriod.ONE_MONTH)) {
            targetDate = LocalDate.now().minusMonths(1);
        } else if(period.equals(TimePeriod.THREE_MONTHS)) {
            targetDate = LocalDate.now().minusMonths(3);
        } else if(period.equals(TimePeriod.SIX_MONTHS)) {
            targetDate = LocalDate.now().minusMonths(6);
        } else if(period.equals(TimePeriod.ONE_YEAR)) {
            targetDate = LocalDate.now().minusYears(1);
        } else {
            targetDate = LocalDate.of(0,1,1);
        }

        return targetDate;
    }
}
