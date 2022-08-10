module com.game.minicasino {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdom;

    opens com.minicasino.ui to javafx.fxml;
    exports com.minicasino.ui;
}