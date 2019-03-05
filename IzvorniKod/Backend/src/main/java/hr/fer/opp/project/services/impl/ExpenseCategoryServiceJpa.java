package hr.fer.opp.project.services.impl;

import hr.fer.opp.project.entities.ExpenseCategory;
import hr.fer.opp.project.entities.HomeGroup;
import hr.fer.opp.project.entities.User;
import hr.fer.opp.project.enums.TimePeriod;
import hr.fer.opp.project.repos.ExpenseCategoryRepository;
import hr.fer.opp.project.services.EntityMissingException;
import hr.fer.opp.project.services.ExpenseCategoryService;
import hr.fer.opp.project.services.ExpenseService;
import hr.fer.opp.project.services.RequestDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCategoryServiceJpa implements ExpenseCategoryService {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepo;

    @Autowired
    private ExpenseService expenseService;

    @Override
    public List<ExpenseCategory> listAll() {
        return expenseCategoryRepo.findAll();
    }

    @Override
    public List<ExpenseCategory> findByUser(User user) {
        Optional<List<ExpenseCategory>> expenseCategories = expenseCategoryRepo.findByUser(user);
        return expenseCategories.orElseGet(ArrayList::new);
    }

    @Override
    public List<ExpenseCategory> findByHomeGroup(HomeGroup homeGroup) {
        Optional<List<ExpenseCategory>> expenseCategories = expenseCategoryRepo.findByHomeGroup(homeGroup);
        return expenseCategories.orElseGet(ArrayList::new);    }

    @Override
    public ExpenseCategory createExpenseCategory(ExpenseCategory expenseCategory) {
        validate(expenseCategory);
        Assert.isNull(expenseCategory.getExpenseCategoryID(), "ExpenseCategoryID needs to be null!");
        ExpenseCategory expenseCategoryResult = checkTimePeriod(expenseCategory);
        return expenseCategoryRepo.save(expenseCategoryResult);
    }

    private ExpenseCategory checkTimePeriod(ExpenseCategory expenseCategory) {
        TimePeriod timePeriod = expenseCategory.getTimePeriod();
        TimePeriod resultTimePeriod = TimePeriod.NO_NOTIFICATION;

        if(timePeriod.toString().equals(TimePeriod.THREE_MONTHS.toString())) {
            resultTimePeriod = TimePeriod.THREE_MONTHS;
        } else if(timePeriod.toString().equals(TimePeriod.SIX_MONTHS.toString())) {
            resultTimePeriod = TimePeriod.SIX_MONTHS;
        } else if(timePeriod.toString().equals(TimePeriod.ONE_YEAR.toString())) {
            resultTimePeriod = TimePeriod.ONE_YEAR;
        } else if(timePeriod.toString().equals(TimePeriod.ALL_TIME.toString())) {
            resultTimePeriod = TimePeriod.ALL_TIME;
        }
        expenseCategory.setTimePeriod(resultTimePeriod);
        return expenseCategory;
    }

    @Override
    public ExpenseCategory fetchExpenseCategory(long expenseCategoryID) {
        return findById(expenseCategoryID).orElseThrow(
            () -> new EntityMissingException(ExpenseCategory.class, expenseCategoryID)
        );
    }

    @Override
    public Optional<ExpenseCategory> findById(long expenseCategoryID) {
        return expenseCategoryRepo.findById(expenseCategoryID);
    }

    @Override
    public ExpenseCategory updateExpenseCategory(ExpenseCategory expenseCategory) {
        validate(expenseCategory);
        Long expenseCategoryID = expenseCategory.getExpenseCategoryID();
        if (!expenseCategoryRepo.existsById(expenseCategoryID)) {
            throw new EntityMissingException(ExpenseCategory.class, expenseCategoryID);
        }
        return expenseCategoryRepo.save(expenseCategory);    }

    @Override
    public ExpenseCategory deleteExpenseCategory(long expenseCategoryID) {
        ExpenseCategory expenseCategory = fetchExpenseCategory(expenseCategoryID);
        expenseService.deleteExpenseCategoryForExpenses(expenseCategoryID);
        expenseCategoryRepo.delete(expenseCategory);
        return expenseCategory;
    }

    private void validate(ExpenseCategory expenseCategory) {
        Assert.notNull(expenseCategory, "Expense category object must be given");
        String expenseCategoryName = expenseCategory.getName();
        Assert.hasText(expenseCategoryName, "Name must be given");
        if(expenseCategory.getHomeGroup() == null && expenseCategory.getUser() == null) {
            throw new RequestDeniedException("Expense category must belong to user or home group!");
        }
        if(expenseCategory.getLimitPercentage() < 0) {
            throw new RequestDeniedException("Limit percentage must be greater or equal than 0.");
        }
    }
}
