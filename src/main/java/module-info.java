module com.desktopfx.desktopfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires java.sql;

    opens com.desktopfx.desktopfx to javafx.fxml;
    exports com.desktopfx.desktopfx;
}