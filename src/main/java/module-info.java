module SystemHotel {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;

    // Controladores del sistema
    opens Controller to javafx.fxml;
    opens Model to javafx.fxml;
    opens Conection to javafx.fxml;
    opens Service to javafx.fxml;

    // Clase principal
    opens HotelApp to javafx.graphics, javafx.fxml;
    exports HotelApp;

    // Exports válidos
    exports Controller;
    exports Conection;
    exports Model;
    exports Service;

}
