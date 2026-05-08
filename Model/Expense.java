package Model;

import java.time.LocalDate;

public class Expense {
    private int id;
    private double amount;
    private Category category;
    private LocalDate date;

    public Expense(int id, double amount, Category category, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public Category getCategory() { return category; }
    public LocalDate getDate() { return date; }

    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(Category category) { this.category = category; }
}