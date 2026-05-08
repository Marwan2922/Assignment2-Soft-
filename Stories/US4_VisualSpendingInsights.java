package Stories;

import Model.BudgetCycle;
import Model.Category;
import Model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class US4_VisualSpendingInsights {

    public US4_VisualSpendingInsights(Stage stage, BudgetCycle cycle) {

        Label title = new Label("Spending Insights");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        // لو مفيش مصاريف
        if (cycle.getExpenses().isEmpty()) {
            Label noData = new Label("No data available. Log an expense to see your insights.");
            noData.setStyle("-fx-text-fill: gray;");
            Button backBtn = new Button("Back");
            backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));
            layout.getChildren().addAll(title, noData, backBtn);
        } else {
            // حساب المصاريف لكل category
            Map<Category, Double> categoryTotals = new HashMap<>();
            for (Expense expense : cycle.getExpenses()) {
                categoryTotals.merge(expense.getCategory(), expense.getAmount(), Double::sum);
            }

            // عمل الـ Pie Chart
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            for (Map.Entry<Category, Double> entry : categoryTotals.entrySet()) {
                pieData.add(new PieChart.Data(entry.getKey().displayName(), entry.getValue()));
            }

            PieChart pieChart = new PieChart(pieData);
            pieChart.setTitle("Spending by Category");

            Button backBtn = new Button("Back");
            backBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
            backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

            layout.getChildren().addAll(title, pieChart, backBtn);
        }

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Spending Insights");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }
}