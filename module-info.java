module Stories {   // بدل com.example.assignment2
    requires javafx.controls;
    requires javafx.fxml;

    opens Stories to javafx.fxml;
    exports Stories;
}