package com.minicasino.ui;

import com.minicasino.data.ProfileData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NewProfileDialogController {
    @FXML
    private TextField profileNameTextField;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label highestWinLabel;

    private ProfileData.Profile editedProfile;

    public void initialize() {
        editedProfile = ProfileData.getProfileDataInstance().getCurrentlyEditedProfile();
        profileNameTextField.setText(editedProfile.getName());
        balanceLabel.setText(String.valueOf(editedProfile.getBalance()));
        highestWinLabel.setText(String.valueOf(editedProfile.getHighestWin()));
    }

    public String validateNameArgument() {
        String name = profileNameTextField.getText().trim();
        if (name.length() != 0 && !name.equalsIgnoreCase("empty")) {
            return name;
        }
        return null;
    }

    public void processTextInput() {
        // parameters validation done in ProfileData
        // TODO: check if editedProfile needs validation first
        if (validateNameArgument() != null) {
            editedProfile.setName(validateNameArgument());
            editedProfile.setEmpty(false);
        } else {
            System.out.println("NewProfileDialogController.processTextInput() -> can't set name;"
                               + " validateNameArgument returned null");
        }
    }

    public String getTextFieldValue() {
        return profileNameTextField.getText();
    }
}
