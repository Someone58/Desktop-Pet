module com.example.desktoppet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.desktoppet to javafx.fxml;
    exports com.example.desktoppet;
}