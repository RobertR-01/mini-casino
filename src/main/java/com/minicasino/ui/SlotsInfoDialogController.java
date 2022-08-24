package com.minicasino.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SlotsInfoDialogController {
    @FXML
    private StackPane topLevelLayout;
    @FXML
    private Button previousButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label p0leftTitleLabel;
    @FXML
    private Label p0leftHeaderLabel;
    @FXML
    private Label p0leftContentLabel;
    @FXML
    private Label p0rightTitleLabel;
    @FXML
    private Label p0rightHeaderLabel;
    @FXML
    private Label p0rightContentLabel;
    @FXML
    private Label p0Title;

    private int activePage;

    public void initialize() {
        activePage = 1;




    }

    @FXML
    public void handleChangePageButton() {

    }

    @FXML
    public void handleContinueButton(ActionEvent event) {
        ((Stage) topLevelLayout.getScene().getWindow()).close();
    }
}
