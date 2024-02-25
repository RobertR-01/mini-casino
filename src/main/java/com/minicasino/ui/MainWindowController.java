package com.minicasino.ui;

import com.minicasino.data.ProfileData;
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
    private Label nameLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label highestWinLabel;

    @FXML
    public void initialize() {
        // removing default focus in this window:
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                topLevelLayout.requestFocus();
            }
        });

        // title label setup
        titleLabel.setText("Mini Casino");
        titleLabel.setFont(Font.font("Times New Roman", 30));
        subTitleLabel.setText("The Game");
        subTitleLabel.setFont(Font.font("Times New Roman", 20));

        // active profile labels setup:
        ProfileData.Profile activeProfile = ProfileData.getProfileDataInstance().getActiveProfile();
        if (activeProfile != null) {
            nameLabel.setText(activeProfile.getName());
            balanceLabel.setText(String.valueOf(activeProfile.getBalance()));
            highestWinLabel.setText(String.valueOf(activeProfile.getHighestWin()));
        } else {
            nameLabel.setText("No active profile selected!");
            balanceLabel.setText("No active profile selected!");
            highestWinLabel.setText("No active profile selected!");
        }
    }

    @FXML
    public void handlePlayButton() throws IOException {
        ProfileData.Profile activeProfile = ProfileData.getProfileDataInstance().getActiveProfile();
        if (activeProfile == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No active profile selected!");
            alert.setContentText("Select one of the previously created profiles as active before continuing. "
                                 + "(Main Menu -> Player Profile)");
            alert.showAndWait();
        } else {
            MainApp.setRoot("game-choice");
        }
    }

    @FXML
    public void handleOptionsButton() {

    }

    @FXML
    public void handleAboutButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Mini Casino");
        alert.setHeaderText("Mini Casino: The Game");
//        alert.setContentText("v" + MainApp.APP_VERSION + "\n\nCopyright \u00A9 2022 RobertR-01"
//                             + "\nSource code: https://github.com/RobertR-01/mini-casino");
        alert.setContentText("v" + MainApp.APP_VERSION + "\n\nCopyright \u00A9 2022 RobertR-01"
                + "\nSource code: https://github.com/RobertR-01/mini-casino\n"
                + """
                \n----------------------------------------------------------------------
                \nGraphics used for this application:
                
                food icon set
                https://freeicons.io/icon-list/food-icon-set
                author: Reda
            
                Icon Pack: Fruit | Flat
                https://www.flaticon.com/packs/fruit-80
                author: amonrat rungreangfangsai

                Icon Pack: Casino | Outline Color
                https://www.flaticon.com/packs/casino-144
                author: Backwoods

                Icon Pack: Casino | Flat
                https://www.flaticon.com/packs/casino-243
                author: Freepik
                    
                """);
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
