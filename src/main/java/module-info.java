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

    // Paquete donde están tus controladores FXML
    opens Formularios to javafx.fxml;

    // Paquetes exportados para toda la app
    exports Formularios;
    exports Clases;
    exports ConexionBD;
}