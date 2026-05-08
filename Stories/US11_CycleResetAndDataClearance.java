package Stories;

import DataBase.DataStorage;
import Model.BudgetCycle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class US11_CycleResetAndDataClearance {

    public US11_CycleResetAndDataClearance(Stage stage, BudgetCycle cycle) {

        Label title = new Label("Reset Current Cycle");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label warningLabel = new Label("Warning: This will permanently delete all logs for this cycle!");
        warningLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");

        // عرض معلومات الـ cycle الحالي
        Label currentCycleLabel = new Label("Current Cycle Info:");
        currentCycleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label allowanceLabel = new Label(String.format("Total Allowance: %.2f EGP", cycle.getTotalAllowance()));
        allowanceLabel.setStyle("-fx-font-size: 14px;");

        Label startLabel = new Label("Start Date: " + cycle.getStartDate().toString());
        startLabel.setStyle("-fx-font-size: 14px;");

        Label endLabel = new Label("End Date: " + cycle.getEndDate().toString());
        endLabel.setStyle("-fx-font-size: 14px;");

        Label expensesLabel = new Label("Total Expenses: " + cycle.getExpenses().size());
        expensesLabel.setStyle("-fx-font-size: 14px;");

        // زر الـ Reset
        Button resetBtn = new Button("Reset Current Cycle");
        resetBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px;");
        resetBtn.setOnAction(e -> {
            // تأكيد الحذف
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Reset Cycle");
            confirm.setHeaderText(null);
            confirm.setContentText("This will permanently delete all logs for this cycle. Continue?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        DataStorage.deleteCycle();
                        DataStorage.deleteExpenses();
                        new US1_SetInitialBudgetCycle(stage);
                    } catch (Exception ex) {
                        warningLabel.setText("Error: " + ex.getMessage());
                    }
                }
            });
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                title,
                warningLabel,
                currentCycleLabel,
                allowanceLabel,
                startLabel,
                endLabel,
                expensesLabel,
                resetBtn,
                backBtn
        );

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Reset Cycle");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }
}