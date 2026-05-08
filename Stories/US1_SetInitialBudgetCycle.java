package Stories;

import DataBase.DataStorage;
import Model.BudgetCycle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class US1_SetInitialBudgetCycle {

    public US1_SetInitialBudgetCycle(Stage stage) {
        try {
            // لو عنده cycle خلاص روح الداشبورد
            BudgetCycle existingCycle = DataStorage.loadCycle();
            if (existingCycle != null) {
                existingCycle.getExpenses().addAll(DataStorage.loadExpenses());
                new US3_DynamicDailyLimitView(stage, existingCycle);
                return;
            }
        } catch (Exception e) {
            // مفيش cycle محفوظ
        }

        // Title
        Label title = new Label("Initialize Budget");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        // Allowance field
        Label allowanceLabel = new Label("Total Budget (EGP)");
        TextField allowanceField = new TextField();
        allowanceField.setPromptText("e.g. 2000");

        // Start Date
        Label startLabel = new Label("Start Date");
        DatePicker startPicker = new DatePicker(LocalDate.now());

        // End Date
        Label endLabel = new Label("End Date");
        DatePicker endPicker = new DatePicker(LocalDate.now().plusMonths(1));

        // Error label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Continue button
        Button continueBtn = new Button("Continue");
        continueBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");

        continueBtn.setOnAction(e -> {
            try {
                // التحقق من المبلغ
                double allowance = Double.parseDouble(allowanceField.getText());
                if (allowance <= 0) {
                    errorLabel.setText("Allowance must be a positive number.");
                    return;
                }

                // التحقق من التواريخ
                LocalDate startDate = startPicker.getValue();
                LocalDate endDate = endPicker.getValue();
                if (!endDate.isAfter(startDate)) {
                    errorLabel.setText("End date must be after start date.");
                    return;
                }

                // إنشاء الـ cycle وحفظه
                BudgetCycle cycle = new BudgetCycle(allowance, startDate, endDate);
                DataStorage.saveCycle(cycle);

                // روح الداشبورد
                new US3_DynamicDailyLimitView(stage, cycle);

            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid number.");
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                title,
                allowanceLabel, allowanceField,
                startLabel, startPicker,
                endLabel, endPicker,
                errorLabel,
                continueBtn
        );

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Setup");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }
}