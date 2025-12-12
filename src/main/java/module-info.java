module it.unife.lp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    opens it.unife.lp to javafx.fxml;
    opens it.unife.lp.view to javafx.fxml;

    exports it.unife.lp;
}
