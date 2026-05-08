package Stories;

import Model.BudgetCycle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class US6_BudgetNotification {

    public static void checkAndNotify(BudgetCycle cycle) {
        double spentPercentage = cycle.getSpentPercentage();

        if (spentPercentage >= 100) {
            showAlert(Alert.AlertType.ERROR,
                    "Budget Exhausted!",
                    "You have used 100% of your allowance.");
        } else if (spentPercentage >= 80) {
            showAlert(Alert.AlertType.WARNING,
                    "Warning!",
                    "You have used 80% of your allowance.");
        }
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}