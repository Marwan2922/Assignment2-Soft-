package Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BudgetCycle {
    private double totalAllowance;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Expense> expenses;

    public BudgetCycle(double totalAllowance, LocalDate startDate, LocalDate endDate) {
        this.totalAllowance = totalAllowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.expenses = new ArrayList<>();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void removeExpense(int id) {
        expenses.removeIf(e -> e.getId() == id);
    }

    public double getTotalSpent() {
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double getRemainingBalance() {
        return totalAllowance - getTotalSpent();
    }

    public long getRemainingDays() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate) + 1;
        return Math.max(days, 0);
    }

    public double getSafeDailyLimit() {
        long remainingDays = getRemainingDays();
        if (remainingDays <= 0) return getRemainingBalance();
        return getRemainingBalance() / remainingDays;
    }

    public double getSpentPercentage() {
        return (getTotalSpent() / totalAllowance) * 100;
    }

    public double getTotalAllowance() { return totalAllowance; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<Expense> getExpenses() { return expenses; }

    public void setTotalAllowance(double totalAllowance) { this.totalAllowance = totalAllowance; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}