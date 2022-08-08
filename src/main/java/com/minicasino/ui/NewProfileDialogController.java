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

    public void initialize() {
        ProfileData.Profile editedProfile = ProfileData.getProfileDataInstance().getCurrentlyEditedProfile();
        profileNameTextField.setText(editedProfile.getName());
        balanceLabel.setText(String.valueOf(editedProfile.getBalance()));
        highestWinLabel.setText(String.valueOf(editedProfile.getHighestWin()));
    }
}
