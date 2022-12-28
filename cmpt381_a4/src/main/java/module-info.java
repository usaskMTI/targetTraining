module com.example.cmpt381_a4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cmpt381_a4 to javafx.fxml;
    exports com.example.cmpt381_a4;
}