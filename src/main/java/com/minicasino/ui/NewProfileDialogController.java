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

    public boolean processTextInput() {
        // TODO: can possibly get rid of trim()
        String name = profileNameTextField.getText().trim();

        // parameters validation done in ProfileData
        // TODO: can possibly get rid of null check for editedProfile
        return editedProfile.setName(name) && (editedProfile != null);
    }

    public String getTextFieldValue() {
        return profileNameTextField.getText();
    }
}
