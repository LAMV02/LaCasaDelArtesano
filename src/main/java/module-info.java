module org.example.lacasadelartesano {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    exports org.example.lacasadelartesano;
    exports org.example.lacasadelartesano.ui;
    exports org.example.lacasadelartesano.database;
    exports org.example.lacasadelartesano.modelo;

    opens org.example.lacasadelartesano.modelo to javafx.base;
}
