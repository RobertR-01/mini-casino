package com.game.minicasino;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class MainAppController {
    @FXML
    private BorderPane mainWindowTopLevelLayout;
    @FXML
    private Label titleLabel;

    public void initialize() {
        // title label
        titleLabel.setText("Mini Casino\nThe Game");
        titleLabel.setFont(Font.font("Algerian", 30));

    }

    @FXML
    public void handlePlayButton() {

    }

    @FXML
    public void handleOptionsButton() {

    }

    @FXML
    public void handleAboutButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Mini Casino");
        alert.setHeaderText("Mini Casino: The Game");
        alert.setContentText("v" + MainApp.APP_VERSION + "\n\nCopyright \u00A9 2022 Mr Smalec");
        alert.show();
    }

    @FXML
    public void handleExitButton() {
        Platform.exit();
    }
}