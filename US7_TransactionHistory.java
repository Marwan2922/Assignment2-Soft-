package Stories;

import Model.BudgetCycle;
import Model.Expense;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class US7_TransactionHistory {

    public US7_TransactionHistory(Stage stage, BudgetCycle cycle) {

        Label title = new Label("Transaction History");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        if (cycle.getExpenses().isEmpty()) {
            Label noData = new Label("No Transactions Found.");
            noData.setStyle("-fx-text-fill: gray; -fx-font-size: 16px;");

            Button logBtn = new Button("Log your first expense");
            logBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            logBtn.setOnAction(e -> new US2_RapidExpenseLogging(stage, cycle));

            Button backBtn = new Button("Back");
            backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

            layout.getChildren().addAll(title, noData, logBtn, backBtn);

        } else {
            TableView<Expense> table = new TableView<>();

            TableColumn<Expense, String> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            String.valueOf(data.getValue().getId())
                    )
            );
            idCol.setPrefWidth(50);

            TableColumn<Expense, String> amountCol = new TableColumn<>("Amount (EGP)");
            amountCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            String.format("%.2f", data.getValue().getAmount())
                    )
            );
            amountCol.setPrefWidth(120);

            TableColumn<Expense, String> categoryCol = new TableColumn<>("Category");
            categoryCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getCategory().displayName()
                    )
            );
            categoryCol.setPrefWidth(120);

            TableColumn<Expense, String> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(
                            data.getValue().getDate().toString()
                    )
            );
            dateCol.setPrefWidth(100);

            table.getColumns().addAll(idCol, amountCol, categoryCol, dateCol);

            List<Expense> sorted = cycle.getExpenses().stream()
                    .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                    .toList();
            table.getItems().addAll(sorted);

            Button backBtn = new Button("Back to Dashboard");
            backBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
            backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

            layout.getChildren().addAll(title, table, backBtn);
        }

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - History");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }
}