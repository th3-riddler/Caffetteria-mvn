module it.unife.lp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.annotation;
    requires java.prefs;

    opens it.unife.lp to javafx.fxml;
    opens it.unife.lp.view to javafx.fxml;

    exports it.unife.lp;
    exports it.unife.lp.model;
    exports it.unife.lp.view;
}
