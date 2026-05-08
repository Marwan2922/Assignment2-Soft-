package Stories;

import DataBase.DataStorage;
import Model.BudgetCycle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class US5_DailyRolloverManagement {

    public US5_DailyRolloverManagement(Stage stage, BudgetCycle cycle) {

        Label title = new Label("Daily Rollover");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // حساب الـ rollover
        double previousDailyLimit = cycle.getTotalAllowance() /
                (cycle.getEndDate().toEpochDay() - cycle.getStartDate().toEpochDay() + 1);
        double todaySpent = cycle.getExpenses().stream()
                .filter(e -> e.getDate().equals(LocalDate.now().minusDays(1)))
                .mapToDouble(e -> e.getAmount())
                .sum();
        double rollover = previousDailyLimit - todaySpent;

        // عرض المعلومات
        Label previousLimitLabel = new Label(String.format("Previous Daily Limit: %.2f EGP", previousDailyLimit));
        previousLimitLabel.setStyle("-fx-font-size: 14px;");

        Label spentLabel = new Label(String.format("Yesterday's Spending: %.2f EGP", todaySpent));
        spentLabel.setStyle("-fx-font-size: 14px;");

        Label rolloverLabel;
        if (rollover > 0) {
            rolloverLabel = new Label(String.format("Rollover Added: +%.2f EGP", rollover));
            rolloverLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #4CAF50;");
        } else if (rollover < 0) {
            rolloverLabel = new Label(String.format("Deficit: %.2f EGP", rollover));
            rolloverLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: orange;");
        } else {
            rolloverLabel = new Label("No Rollover");
            rolloverLabel.setStyle("-fx-font-size: 16px;");
        }

        Label newLimitLabel = new Label(String.format("New Daily Limit: %.2f EGP", cycle.getSafeDailyLimit()));
        newLimitLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                title,
                previousLimitLabel,
                spentLabel,
                rolloverLabel,
                newLimitLabel,
                backBtn
        );

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Daily Rollover");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }
}