package Stories;

import Model.BudgetCycle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class US3_DynamicDailyLimitView {

    public US3_DynamicDailyLimitView(Stage stage, BudgetCycle cycle) {

        // HEADER
        Label appName = new Label("💰 Masroofy");
        appName.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label subtitle = new Label("Personal Budget Tracker");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #B2DFDB;");
        VBox headerText = new VBox(2, appName, subtitle);
        headerText.setAlignment(Pos.CENTER_LEFT);
        HBox header = new HBox(headerText);
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setStyle("-fx-background-color: linear-gradient(to right, #004D40, #00796B);");

        // LIMIT CARD
        Label limitTitle = new Label("TODAY'S SAFE LIMIT");
        limitTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #80CBC4; -fx-letter-spacing: 2;");
        Label limitAmount = new Label(String.format("%.2f EGP", cycle.getSafeDailyLimit()));
        limitAmount.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-text-fill: white;");

        // progress bar manual
        double percent = cycle.getTotalSpent() / cycle.getTotalAllowance();
        String barColor = percent >= 0.8 ? "#f44336" : percent >= 0.5 ? "#FF9800" : "#69F0AE";
        Label progressText = new Label(String.format("%.0f%% of budget used", percent * 100));
        progressText.setStyle("-fx-font-size: 12px; -fx-text-fill: #B2DFDB;");

        Region progressFill = new Region();
        progressFill.setPrefHeight(6);
        progressFill.setPrefWidth(300 * percent);
        progressFill.setStyle("-fx-background-color: " + barColor + "; -fx-background-radius: 3;");
        Region progressBg = new Region();
        progressBg.setPrefHeight(6);
        progressBg.setPrefWidth(300);
        progressBg.setStyle("-fx-background-color: #004D40; -fx-background-radius: 3;");
        StackPane progressBar = new StackPane(progressBg, progressFill);
        StackPane.setAlignment(progressFill, Pos.CENTER_LEFT);

        VBox limitCard = new VBox(8, limitTitle, limitAmount, progressBar, progressText);
        limitCard.setAlignment(Pos.CENTER_LEFT);
        limitCard.setPadding(new Insets(30, 40, 30, 40));
        limitCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #00695C, #004D40);");

        // STATS ROW
        VBox balanceCard = statCard("💳 Balance", String.format("%.2f EGP", cycle.getRemainingBalance()), "#E3F2FD", "#1565C0");
        VBox daysCard = statCard("📅 Days Left", String.valueOf(cycle.getRemainingDays()), "#F3E5F5", "#6A1B9A");
        VBox spentCard = statCard("💸 Spent", String.format("%.2f EGP", cycle.getTotalSpent()), "#FFF3E0", "#E65100");

        HBox stats = new HBox(15, balanceCard, daysCard, spentCard);
        stats.setAlignment(Pos.CENTER);
        stats.setPadding(new Insets(20, 30, 10, 30));

        // BUTTONS
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 30, 30, 30));

        grid.add(btn("➕  Log Expense",     "#00897B"), 0, 0);
        grid.add(btn("📋  History",          "#1E88E5"), 1, 0);
        grid.add(btn("📊  Insights",         "#D81B60"), 0, 1);
        grid.add(btn("🔄  Rollover",         "#F4511E"), 1, 1);
        grid.add(btn("✏️  Edit / Delete",    "#FB8C00"), 0, 2);
        grid.add(btn("🔍  Filter",           "#8E24AA"), 1, 2);
        grid.add(btn("💾  Storage",          "#546E7A"), 0, 3);
        grid.add(btn("🗑️  Reset Cycle",     "#E53935"), 1, 3);

        // ACTIONS
        grid.getChildren().get(0).setOnMouseClicked(e -> nav(stage, cycle, 2));
        grid.getChildren().get(1).setOnMouseClicked(e -> nav(stage, cycle, 7));
        grid.getChildren().get(2).setOnMouseClicked(e -> nav(stage, cycle, 4));
        grid.getChildren().get(3).setOnMouseClicked(e -> nav(stage, cycle, 5));
        grid.getChildren().get(4).setOnMouseClicked(e -> nav(stage, cycle, 8));
        grid.getChildren().get(5).setOnMouseClicked(e -> nav(stage, cycle, 9));
        grid.getChildren().get(6).setOnMouseClicked(e -> nav(stage, cycle, 10));
        grid.getChildren().get(7).setOnMouseClicked(e -> nav(stage, cycle, 11));

        // ROOT
        VBox root = new VBox(limitCard, stats, grid);
        root.setStyle("-fx-background-color: #FAFAFA;");

        BorderPane main = new BorderPane();
        main.setTop(header);
        main.setCenter(root);

        Scene scene = new Scene(main,1000,700);
        boolean max = stage.isMaximized();
        stage.setTitle("Masroofy - Dashboard");
        stage.setScene(scene);
        stage.setMaximized(max);
        stage.show();
    }

    private void nav(Stage stage, BudgetCycle cycle, int us) {
        boolean max = stage.isMaximized();
        switch (us) {
            case 2  -> new US2_RapidExpenseLogging(stage, cycle);
            case 4  -> new US4_VisualSpendingInsights(stage, cycle);
            case 5  -> new US5_DailyRolloverManagement(stage, cycle);
            case 7  -> new US7_TransactionHistory(stage, cycle);
            case 8  -> new US8_EditOrDeleteTransaction(stage, cycle);
            case 9  -> new US9_FilterTransactionHistory(stage, cycle);
            case 10 -> new US10_OfflineLocalDataPersistence(stage, cycle);
            case 11 -> new US11_CycleResetAndDataClearance(stage, cycle);
        }
        stage.setMaximized(max);
    }

    private VBox statCard(String title, String value, String bg, String textColor) {
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 11px; -fx-text-fill: #777;");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        VBox card = new VBox(4, t, v);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(150);
        card.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 10;");
        return card;
    }

    private Button btn(String text, String color) {
        Button b = new Button(text);
        b.setPrefWidth(185);
        b.setPrefHeight(45);
        b.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 13px; -fx-background-radius: 8; -fx-cursor: hand;");
        return b;
    }
}