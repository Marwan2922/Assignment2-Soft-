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

public class US10_OfflineLocalDataPersistence {

    public US10_OfflineLocalDataPersistence(Stage stage, BudgetCycle cycle) {

        Label title = new Label("Data Storage Info");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label infoLabel = new Label("All your data is saved locally on your device.");
        infoLabel.setStyle("-fx-font-size: 14px;");

        Label offlineLabel = new Label("✓ Works 100% Offline");
        offlineLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

        Label storageLabel = new Label("✓ No Internet Required");
        storageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

        Label dataLabel = new Label("✓ Data Persists After App Restart");
        dataLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

        // عرض مكان الملفات
        Label filesLabel = new Label("Storage Files:");
        filesLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label cycleFile = new Label("• budget_cycle.txt");
        cycleFile.setStyle("-fx-font-size: 12px; -fx-text-fill: #2196F3;");

        Label expensesFile = new Label("• expenses.txt");
        expensesFile.setStyle("-fx-font-size: 12px; -fx-text-fill: #2196F3;");

        // زر حفظ يدوي
        Button saveBtn = new Button("Save Data Now");
        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        saveBtn.setOnAction(e -> {
            try {
                DataStorage.saveCycle(cycle);
                DataStorage.saveExpenses(cycle.getExpenses());
                saveBtn.setText("Saved Locally ✓");
                saveBtn.setStyle("-fx-background-color: #388E3C; -fx-text-fill: white; -fx-font-size: 14px;");
            } catch (Exception ex) {
                saveBtn.setText("Error Saving!");
                saveBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px;");
            }
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                title,
                infoLabel,
                offlineLabel,
                storageLabel,
                dataLabel,
                filesLabel,
                cycleFile,
                expensesFile,
                saveBtn,
                backBtn
        );

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Data Storage");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }
}