package com.minicasino.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;

public class GameChoiceController {
    @FXML
    private GridPane topLevelLayout;
    @FXML
    private Label headerLabel;
    @FXML
    private Button slotsButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // removing default focus in this window:
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                topLevelLayout.requestFocus();
            }
        });

        // header label setup
        headerLabel.setFont(Font.font("Times New Roman", 20));
    }

    @FXML
    public void handleBackButton() throws IOException {
        MainApp.setRoot("main-window");
    }

    @FXML
    public void handleSlotsButton() throws IOException {
        MainApp.setRoot("slots");
    }
}
