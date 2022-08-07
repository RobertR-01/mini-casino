package com.minicasino.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileChoiceController {
    @FXML
    private GridPane topLevelLayout;
    @FXML
    private Label headerLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private HBox profile0HBox;
    @FXML
    private ToggleGroup profileToggleGroup;
    @FXML
    private Button profile0Button;
    @FXML
    private Button profile1Button;
    @FXML
    private Button profile2Button;
    @FXML
    private Button profile3Button;
    @FXML
    private Button profile4Button;

    private Toggle previouslySelectedRadioButton;

    @FXML
    public void initialize() {
        // removing default focus in this window:
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                topLevelLayout.requestFocus();
            }
        });

        // header label setup:
        headerLabel.setFont(Font.font("Times New Roman", 20));

        // data binding
        List<Button> buttonList = new ArrayList<>();
        Collections.addAll(buttonList, profile0Button, profile1Button, profile2Button, profile3Button, profile4Button);
        ObservableList<String> listOfProfileNames = FXCollections.observableArrayList();
        for (int i = 0; i < 5; i++) {
//            buttonList.get(i).textProperty().bind(Bindings.valueAt(ProfileData.getProfileDataInstance().getProfileList().get(i).getName()));
        }
    }

    @FXML
    public void handleCancelButton() throws IOException {
        MainApp.setRoot("main-window");
    }

    @FXML
    public void handleRadioButton(ActionEvent event) {
        Toggle sourceRadioButton = (Toggle) event.getSource();
        ((Node) sourceRadioButton).getParent().setId("selectedProfileRow");

        if (previouslySelectedRadioButton != null) {
            ((Node) previouslySelectedRadioButton).getParent().setId("unselectedProfileRow");
        }
        previouslySelectedRadioButton = sourceRadioButton;


    }


}
