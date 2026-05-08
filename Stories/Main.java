package Stories;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new US1_SetInitialBudgetCycle(stage);
        stage.getIcons().add(new javafx.scene.image.Image("/icon.jpg") // add image icon
        );

    }

    public static void main(String[] args) {
        launch(args);
    }
}