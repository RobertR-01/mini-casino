module com.game.minicasino {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.game.minicasino to javafx.fxml;
    exports com.game.minicasino;
}