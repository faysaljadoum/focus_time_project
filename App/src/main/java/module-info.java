/**
 *
 */
module com.example.focus_time {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;

    opens com.example.focus_time to javafx.fxml;
    exports com.example.focus_time;
    exports com.example.focus_time.Controllers;
    exports com.example.focus_time.Models;
    opens com.example.focus_time.Controllers to javafx.fxml;
}