package Stories;

import Model.BudgetCycle;
import Model.Expense;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class US9_FilterTransactionHistory {

    public US9_FilterTransactionHistory(Stage stage, BudgetCycle cycle) {

        Label title = new Label("Filter Transactions");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Category filter
        Label categoryLabel = new Label("Filter by Category");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("ALL", "FOOD", "TRANSPORT", "ENTERTAINMENT", "OTHER");
        categoryBox.setValue("ALL");

        // Date filter
        Label dateLabel = new Label("Filter by Date");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date (optional)");

        // الجدول
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

        // تحميل كل المصاريف الأول
        loadTable(table, cycle.getExpenses());

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: gray;");

        // زر الفلتر
        Button filterBtn = new Button("Filter");
        filterBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        filterBtn.setOnAction(e -> {
            List<Expense> filtered = cycle.getExpenses().stream()
                    .filter(expense -> {
                        if (!categoryBox.getValue().equals("ALL")) {
                            if (!expense.getCategory().name().equals(categoryBox.getValue())) {
                                return false;
                            }
                        }
                        if (datePicker.getValue() != null) {
                            if (!expense.getDate().equals(datePicker.getValue())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                    .collect(Collectors.toList());

            table.getItems().clear();
            if (filtered.isEmpty()) {
                errorLabel.setText("No transactions found for the selected filter.");
            } else {
                errorLabel.setText("");
                loadTable(table, filtered);
            }
        });

        // زر reset
        Button resetBtn = new Button("Reset");
        resetBtn.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-size: 14px;");
        resetBtn.setOnAction(e -> {
            categoryBox.setValue("ALL");
            datePicker.setValue(null);
            errorLabel.setText("");
            table.getItems().clear();
            loadTable(table, cycle.getExpenses());
        });

        Button backBtn = new Button("Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.getChildren().addAll(filterBtn, resetBtn);

        HBox filterOptions = new HBox(15);
        filterOptions.setAlignment(Pos.CENTER);
        filterOptions.getChildren().addAll(categoryLabel, categoryBox, dateLabel, datePicker);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                title,
                filterOptions,
                filterBox,
                errorLabel,
                table,
                backBtn
        );

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Filter Transactions");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }

    private void loadTable(TableView<Expense> table, List<Expense> expenses) {
        List<Expense> sorted = expenses.stream()
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());
        table.getItems().addAll(sorted);
    }
}