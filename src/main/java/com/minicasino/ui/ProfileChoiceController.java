package com.minicasino.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;

public class ProfileChoiceController {
    @FXML
    private GridPane topLevelLayout;
    @FXML
    private Label headerLabel;
    @FXML
    private Button slotsButton;
    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        // header label setup
        headerLabel.setFont(Font.font("Times New Roman", 20));
    }

    @FXML
    public void handleCancelButton() throws IOException {
        MainApp.setRoot("main-window");
    }

    @FXML
    public void handleSlotsButton() throws IOException {
        MainApp.setRoot("slots");
    }
}
