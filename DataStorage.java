package DataBase;

import Model.BudgetCycle;
import Model.Category;
import Model.Expense;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class DataStorage {
    private static final String CYCLE_FILE = "budget_cycle.txt";
    private static final String EXPENSES_FILE = "expenses.txt";

    // حفظ الـ BudgetCycle
    public static void saveCycle(BudgetCycle cycle) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CYCLE_FILE))) {
            writer.write(cycle.getTotalAllowance() + "\n");
            writer.write(cycle.getStartDate().toString() + "\n");
            writer.write(cycle.getEndDate().toString() + "\n");
        }
    }

    // قراءة الـ BudgetCycle
    public static BudgetCycle loadCycle() throws IOException {
        File file = new File(CYCLE_FILE);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(CYCLE_FILE))) {
            double allowance = Double.parseDouble(reader.readLine());
            LocalDate startDate = LocalDate.parse(reader.readLine());
            LocalDate endDate = LocalDate.parse(reader.readLine());
            return new BudgetCycle(allowance, startDate, endDate);
        }
    }

    // حفظ المصاريف
    public static void saveExpenses(List<Expense> expenses) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXPENSES_FILE))) {
            for (Expense e : expenses) {
                writer.write(e.getId() + "," +
                        e.getAmount() + "," +
                        e.getCategory().name() + "," +
                        e.getDate().toString() + "\n");
            }
        }
    }

    // قراءة المصاريف
    public static List<Expense> loadExpenses() throws IOException {
        List<Expense> expenses = new ArrayList<>();
        File file = new File(EXPENSES_FILE);
        if (!file.exists()) return expenses;

        try (BufferedReader reader = new BufferedReader(new FileReader(EXPENSES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                double amount = Double.parseDouble(parts[1]);
                Category category = Category.valueOf(parts[2]);
                LocalDate date = LocalDate.parse(parts[3]);
                expenses.add(new Expense(id, amount, category, date));
            }
        }
        return expenses;
    }

    public static void deleteCycle() throws IOException {
        new File(CYCLE_FILE).delete();
    }

    public static void deleteExpenses() throws IOException {
        new File(EXPENSES_FILE).delete();
    }
}