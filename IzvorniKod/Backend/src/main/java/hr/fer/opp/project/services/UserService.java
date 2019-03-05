package hr.fer.opp.project.services;

import hr.fer.opp.project.entities.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> listAll();

    User createUser(User user);

    User fetchUser(long userID);

    User fetchCurrentUser();

    Optional<User> findById(long userID);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User updateUser(User user);

    User verifyMail(User user);

    User deleteUser(long userID);

    void updateCurrentBalance(long userID);

    List<Expense> fetchExpenses(User user, String timePeriod);

    List<Revenue> fetchRevenues(User user, String timePeriod);

    List<ExpenseCategory> fetchExpenseCategories(User user);

    List<RevenueCategory> fetchRevenueCategories(User user);

    User updatePassword(User user);

}
