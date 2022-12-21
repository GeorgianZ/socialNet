module com.example.socialnet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.socialnet to javafx.fxml;
    exports com.example.socialnet;
    exports controller;
    opens controller to javafx.fxml;
    opens domain to javafx.fxml;
    exports domain;
}