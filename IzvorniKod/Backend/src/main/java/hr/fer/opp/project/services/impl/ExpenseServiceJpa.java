package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.*;
import hr.fer.opp.project.entities.complexEntities.GroupMember;
import hr.fer.opp.project.entities.complexEntities.NotifiedUser;
import hr.fer.opp.project.enums.MemberRole;
import hr.fer.opp.project.enums.NotificationType;
import hr.fer.opp.project.enums.TimePeriod;
import hr.fer.opp.project.repos.ExpenseRepository;
import hr.fer.opp.project.services.*;
import org.apache.tomcat.jni.Local;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceJpa implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @Autowired
    private HomeGroupService homeGroupService;

    @Autowired
    private NotificationService notificationService;


    @Override
    public List<Expense> listAll() {
        return expenseRepo.findAll();
    }

    @Override
    public List<Expense> findByUser(User user) {
       return expenseRepo.findByUser(user).orElseGet(ArrayList::new);
    }

    @Override
    public Expense createExpense(Expense expense) {
        validate(expense);
        expense.setEntryTime(LocalDateTime.now());
        Expense retExpense = expenseRepo.save(expense);
        ExpenseCategory expenseCategory = expenseCategoryService.fetchExpenseCategory(
                expense.getExpenseCategory().getExpenseCategoryID());
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        if(!expenseCategory.getTimePeriod().equals(TimePeriod.NO_NOTIFICATION)
            && (expense.getDate().isEqual(date) || expense.getDate().isAfter(date))) {
            createNotificationIfNeeded(retExpense);
        }

        User user = userService.fetchUser(expense.getUser().getUserID());
        userService.updateCurrentBalance(user.getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) { }
        return retExpense;
    }

    private void createNotificationIfNeeded(Expense expense) {
        User user = userService.fetchUser(expense.getUser().getUserID());
        ExpenseCategory expenseCategory = expenseCategoryService.fetchExpenseCategory(
                expense.getExpenseCategory().getExpenseCategoryID());
        TimePeriod referentTimePeriod = expenseCategory.getTimePeriod();
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        LocalDate startDate = calculateStartDate(referentTimePeriod, endDate);
        List<Expense> expenses = findByExpenseCategory(expenseCategory);
        double thisMonthAmount = 0.;
        double lastMonthsAmount = 0.;
        for(Expense oneExpense : expenses) {
            if(oneExpense.getExpenseCategory().getExpenseCategoryID()
                    .equals(expenseCategory.getExpenseCategoryID())) {
                if(oneExpense.getDate().isAfter(endDate) || oneExpense.getDate().isEqual(endDate)) {
                    thisMonthAmount += oneExpense.getAmount();
                } else if (oneExpense.getDate().isAfter(startDate) && oneExpense.getDate().isBefore(endDate)) {
                    lastMonthsAmount += oneExpense.getAmount();
                }
            }
        }
        if(lastMonthsAmount > 0) {
            if (referentTimePeriod.equals(TimePeriod.THREE_MONTHS)) {
                lastMonthsAmount = lastMonthsAmount / 3;
            } else if (referentTimePeriod.equals(TimePeriod.SIX_MONTHS)) {
                lastMonthsAmount = lastMonthsAmount / 6;
            } else if (referentTimePeriod.equals(TimePeriod.ONE_YEAR)) {
                lastMonthsAmount = lastMonthsAmount / 12;
            }

            if (lastMonthsAmount * (expenseCategory.getLimitPercentage() / 100) < thisMonthAmount) {
                Notification notification = notificationService.createNotification(
                        new Notification(NotificationType.CATEGORY_EXPENSE,
                        "Vaši troškovi u kategoriji " + expenseCategory.getName() + " su premašili " +
                                "postavljeni limit u odnosu na prosječnu potrošnju u zadanom razdoblju!",
                        LocalDateTime.now()));

                HomeGroup homeGroup = null;
                try {
                    homeGroup = homeGroupService.fetchHomeGroupByUser(user);
                } catch (Exception ignore) {}
                if(homeGroup != null) {
                    List<User> users = homeGroupService.fetchUsersByHomeGroup(homeGroup);
                    for(User member : users) {
                        notificationService.createNotifiedUser(new NotifiedUser(member, notification));
                    }
                } else {
                    notificationService.createNotifiedUser(new NotifiedUser(user, notification));
                }
            }
        }
    }

    private LocalDate calculateStartDate(TimePeriod referentTimePeriod, LocalDate endDate) {
        if(referentTimePeriod.equals(TimePeriod.THREE_MONTHS)) {
            return endDate.minusMonths(3);
        } else if(referentTimePeriod.equals(TimePeriod.SIX_MONTHS)) {
            return endDate.minusMonths(6);
        }  else if(referentTimePeriod.equals(TimePeriod.ONE_YEAR)) {
            return endDate.minusYears(1);
        }   else {
            return LocalDate.of(1900,1,1);
        }
    }

    @Override
    public Expense fetchExpense(long expenseID) {
        Optional<Expense> expense = expenseRepo.findById(expenseID);
        if(!expense.isPresent()) {
            throw new EntityMissingException(Expense.class, expenseID);
        }
        return expense.get();
    }

    @Override
    public Optional<Expense> findById(long expenseID) {
        return expenseRepo.findById(expenseID);
    }

    @Override
    public Expense updateExpense(Expense expense) {
        validate(expense);
        Long expenseID = expense.getExpenseID();
        if (!expenseRepo.existsById(expenseID)) {
            throw new EntityMissingException(Expense.class, expense);
        }
        Expense retExpense = expenseRepo.save(expense);
        try {
            ExpenseCategory expenseCategory = expenseCategoryService.fetchExpenseCategory(
                    expense.getExpenseCategory().getExpenseCategoryID());
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
            if (!expenseCategory.getTimePeriod().equals(TimePeriod.NO_NOTIFICATION)
                    && (expense.getDate().isEqual(date) || expense.getDate().isAfter(date))) {
                createNotificationIfNeeded(retExpense);
            }
        } catch (RuntimeException ignore) {}
        User user = userService.fetchUser(expense.getUser().getUserID());
        userService.updateCurrentBalance(user.getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) { }
        return retExpense;
    }

    @Override
    public Expense deleteExpense(long expenseID) {
        Expense expense = fetchExpense(expenseID);
        expenseRepo.delete(expense);
        User user = userService.fetchUser(expense.getUser().getUserID());
        userService.updateCurrentBalance(user.getUserID());
        try {
            homeGroupService.updateGroupBalance(homeGroupService.findGroupIDByUserMembership(user));
        } catch(RequestDeniedException ignore) {}
        return expense;
    }

    @Override
    public List<Expense> findByExpenseCategory(ExpenseCategory expenseCategory) {
        return expenseRepo.findByExpenseCategory(expenseCategory).orElseGet(ArrayList::new);
    }

    @Override
    public void deleteExpenseCategoryForExpenses(long expenseCategoryID) {
        Optional<ExpenseCategory> ec = expenseCategoryService.findById(expenseCategoryID);
        List<Expense> expenses;
        if(ec.isPresent()) {
            expenses = findByExpenseCategory(ec.get());
            if(!expenses.isEmpty()) {
                for(Expense e : expenses) {
                    e.setExpenseCategory(null);
                    updateExpense(e);
                }
            }
        }
    }

    private void validate(Expense expense) {
        Assert.notNull(expense, "Expense object must be given");
        Assert.notNull(expense.getDate(), "Date must be given");
        double amount = expense.getAmount();
        String desc = expense.getDescription();
        Assert.hasText(desc, "Description must be given");
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount must be bigger than 0!");
        }
    }
}
