module com.example.desktoppet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.desktoppet to javafx.fxml;
    exports com.example.desktoppet;
    exports com.example.desktoppet.UI;
    opens com.example.desktoppet.UI to javafx.fxml;
    exports com.example.desktoppet.Controller;
    opens com.example.desktoppet.Controller to javafx.fxml;
    exports com.example.desktoppet.Model;
    opens com.example.desktoppet.Model to javafx.fxml;
    exports com.example.desktoppet.Interfaces;
    opens com.example.desktoppet.Interfaces to javafx.fxml;
}