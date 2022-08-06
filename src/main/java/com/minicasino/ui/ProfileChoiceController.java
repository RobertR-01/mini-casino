package com.minicasino.ui;

import com.minicasino.data.ProfileData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;

public class ProfileChoiceController {
    @FXML
    private Label headerLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private HBox profile0HBox;
    @FXML
    private ToggleGroup profileToggleGroup;

    private Toggle previouslySelectedRadioButton;

    @FXML
    public void initialize() {
        // header label setup:
        headerLabel.setFont(Font.font("Times New Roman", 20));
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

    @FXML
    public void testXMLSave() {
//        ProfileData.getProfileDataInstance().saveProfileData(ProfileData.getProfileDataInstance().getProfileList());
    }

    @FXML
    public void testXMLLoad() {
        System.out.println(ProfileData.getProfileDataInstance().getProfileList());
    }
}
