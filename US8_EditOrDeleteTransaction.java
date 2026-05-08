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

import java.util.List;

public class US8_EditOrDeleteTransaction {

    public US8_EditOrDeleteTransaction(Stage stage, BudgetCycle cycle) {

        Label title = new Label("Edit / Delete Transaction");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        if (cycle.getExpenses().isEmpty()) {
            Label noData = new Label("No Transactions Found.");
            noData.setStyle("-fx-text-fill: gray; -fx-font-size: 16px;");

            Button backBtn = new Button("Back");
            backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

            layout.getChildren().addAll(title, noData, backBtn);

        } else {
            TableView<Expense> table = new TableView<>();
            table.setEditable(false);

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

            Label errorLabel = new Label();
            errorLabel.setStyle("-fx-text-fill: red;");

            Button deleteBtn = new Button("Delete");
            deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px;");
            deleteBtn.setOnAction(e -> {
                Expense selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    errorLabel.setText("Please select a transaction first.");
                    return;
                }

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Delete Transaction");
                confirm.setHeaderText(null);
                confirm.setContentText("Are you sure you want to delete this? This will update your daily limit.");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            cycle.removeExpense(selected.getId());
                            DataStorage.saveExpenses(cycle.getExpenses());
                            new US8_EditOrDeleteTransaction(stage, cycle);
                        } catch (Exception ex) {
                            errorLabel.setText(ex.getMessage());
                        }
                    }
                });
            });

            Button editBtn = new Button("Edit");
            editBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px;");
            editBtn.setOnAction(e -> {
                Expense selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    errorLabel.setText("Please select a transaction first.");
                    return;
                }
                showEditDialog(stage, cycle, selected);
            });

            Button backBtn = new Button("Back to Dashboard");
            backBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
            backBtn.setOnAction(e -> new US3_DynamicDailyLimitView(stage, cycle));

            HBox btnBox = new HBox(10);
            btnBox.setAlignment(Pos.CENTER);
            btnBox.getChildren().addAll(editBtn, deleteBtn, backBtn);

            layout.getChildren().addAll(title, table, errorLabel, btnBox);
        }

        Scene scene = new Scene(layout,1000,700);
        stage.setTitle("Masroofy - Edit/Delete");
        boolean maximized = stage.isMaximized();
        stage.setScene(scene);
        stage.setMaximized(maximized);
        stage.show();
    }

    private void showEditDialog(Stage stage, BudgetCycle cycle, Expense expense) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Transaction");

        Label amountLabel = new Label("Amount (EGP)");
        TextField amountField = new TextField(String.valueOf(expense.getAmount()));

        Label categoryLabel = new Label("Category");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("FOOD", "TRANSPORT", "ENTERTAINMENT", "OTHER");
        categoryBox.setValue(expense.getCategory().name());

        Label editError = new Label();
        editError.setStyle("-fx-text-fill: red;");

        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(20));
        dialogLayout.getChildren().addAll(amountLabel, amountField, categoryLabel, categoryBox, editError);

        dialog.getDialogPane().setContent(dialogLayout);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    double newAmount = Double.parseDouble(amountField.getText());
                    if (newAmount <= 0) {
                        editError.setText("Please enter a valid number.");
                        return null;
                    }
                    expense.setAmount(newAmount);
                    expense.setCategory(Category.valueOf(categoryBox.getValue()));
                    DataStorage.saveExpenses(cycle.getExpenses());
                    new US8_EditOrDeleteTransaction(stage, cycle);
                } catch (NumberFormatException ex) {
                    editError.setText("Please enter a valid number.");
                } catch (Exception ex) {
                    editError.setText(ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}