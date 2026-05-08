package Stories;

import DataBase.DataStorage;
import Model.BudgetCycle;
import Model.Category;
import Model.Expense;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class US2_RapidExpenseLogging {

    public US2_RapidExpenseLogging(Stage stage, BudgetCycle cycle) {

        // Title
        Label title = new Label("Log Expense");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Amount field
        Label amountLabel = new Label("Amount (EGP)");
        TextField amountField = new TextField();
        amountField.setPromptText("e.g. 30");

        // Category buttons
        Label categoryLabel = new Label("Select Category");
        categoryLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        ToggleGroup categoryGroup = new ToggleGroup();

        ToggleButton foodBtn = new ToggleButton("🍔 Food");
        ToggleButton transportBtn = new ToggleButton("🚗 Transport");
        ToggleButton entertainmentBtn = new ToggleButton("🎮 Entertainment");
        ToggleButton otherBtn = new ToggleButton("📦 Other");

        String base = "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 13px; -fx-padding: 8 15;";
        foodBtn.setStyle("-fx-background-color: #4CAF50;" + base);
        transportBtn.setStyle("-fx-background-color: #2196F3;" + base);
        entertainmentBtn.setStyle("-fx-background-color: #E91E63;" + base);
        otherBtn.setStyle("-fx-background-color: #FF9800;" + base);

        foodBtn.setToggleGroup(categoryGroup);
        transportBtn.setToggleGroup(categoryGroup);
        entertainmentBtn.setToggleGroup(categoryGroup);
        otherBtn.setToggleGroup(categoryGroup);

        HBox categoryBox = new HBox(10);
        categoryBox.setAlignment(Pos.CENTER);
        categoryBox.getChildren().addAll(foodBtn, transportBtn, entertainmentBtn, otherBtn);
        // Error label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Save button
        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        saveBtn.setOnAction(e -> {
            try {
                // التحقق من المبلغ
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    errorLabel.setText("Please enter a valid number.");
                    return;
                }

                // التحقق من الـ category
                if (categoryGroup.getSelectedToggle() == null) {
                    errorLabel.setText("Please select a category.");
                    return;
                }

                // تحديد الـ category
                Category category;
                ToggleButton selected = (ToggleButton) categoryGroup.getSelectedToggle();
                category = switch (selected.getText()) {
                    case "🍔 Food" -> Category.FOOD;
                    case "🚗 Transport" -> Category.TRANSPORT;
                    case "🎮 Entertainmentt" -> Category.ENTERTAINMENT;
                    default -> Category.OTHER;
                };

                // إضافة المصروف
                int id = cycle.getExpenses().size() + 1;
                Expense expense = new Expense(id, amount, category, LocalDate.now());
                cycle.addExpense(expense);

                // حفظ المصاريف
                DataStorage.saveExpenses(cycle.getExpenses());

                US6_BudgetNotification.checkAndNotify(cycle);

                // رجوع للداشبورد
                new US3_DynamicDailyLimitView(stage, cycle);

            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid number.");
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        // زر الرجوع
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                title,
                amountLabel, amountField,
                categoryLabel, categoryBox,
                errorLabel,
                saveBtn,
                backBtn
        );

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Log Expense");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }
}