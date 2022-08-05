package com.minicasino.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.io.IOException;

public class MainWindowController {
    @FXML
    private BorderPane topLevelLayout;
    @FXML
    private Label titleLabel;
    @FXML
    private Label subTitleLabel;

    @FXML
    public void initialize() {
        // title label setup
        titleLabel.setText("Mini Casino");
        titleLabel.setFont(Font.font("Times New Roman", 30));
        subTitleLabel.setText("The Game");
        subTitleLabel.setFont(Font.font("Times New Roman", 20));
    }

    @FXML
    public void handlePlayButton() throws IOException {
        MainApp.setRoot("game-choice");
    }

    @FXML
    public void handleOptionsButton() {

    }

    @FXML
    public void handleAboutButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Mini Casino");
        alert.setHeaderText("Mini Casino: The Game");
        alert.setContentText("v" + MainApp.APP_VERSION + "\n\nCopyright \u00A9 2022 RobertR-01"
                             + "\nGitHub: https://github.com/RobertR-01/mini-casino");
        alert.show();
    }

    @FXML
    public void handleProfileButton() throws IOException {
        MainApp.setRoot("profile-choice");
    }

    @FXML
    public void handleExitButton() {
        Platform.exit();
    }
}
