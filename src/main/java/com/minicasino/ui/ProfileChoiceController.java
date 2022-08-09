package com.minicasino.ui;

import com.minicasino.data.ProfileData;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProfileChoiceController {
    @FXML
    private GridPane topLevelLayout;
    @FXML
    private Label headerLabel;
    @FXML
    private Button backButton;
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
    private List<Button> buttonList;

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
        buttonList = new ArrayList<>();
        Collections.addAll(buttonList, profile0Button, profile1Button, profile2Button, profile3Button, profile4Button);
        for (int i = 0; i < 5; i++) {
            buttonList.get(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleProfileButton(event);
                }
            });
            int index = i;
            buttonList.get(i).textProperty().bind(Bindings.createObjectBinding(
                    () -> ProfileData.getProfileDataInstance().getProfileList().get(index).getName(),
                    ProfileData.getProfileDataInstance().getProfileList()));
        }
    }

    @FXML
    public void handleBackButton() throws IOException {
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

        // more
    }

    @FXML
    public void handleProfileButton(ActionEvent event) {
        // set the currently edited profile:
        Button eventSource = (Button) event.getSource();
        int index = buttonList.indexOf(eventSource);
        ProfileData.Profile editedProfile = ProfileData.getProfileDataInstance().getProfileList().get(index);
        editedProfile.setBeingEdited(true); // TODO: move it to the ProfileData from the Profile class
        // TODO: obsolete step?
//        ProfileData.getProfileDataInstance().setCurrentlyEditedProfile(editedProfile);

        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(topLevelLayout.getScene().getWindow());
        dialog.setTitle("Editing profile");
        dialog.setHeaderText("Edit the selected profile:");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("new-profile-dialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewProfileDialogController controller = fxmlLoader.getController();

        // event filter for input validation:
        final Button buttonOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        buttonOK.addEventFilter(ActionEvent.ACTION,
                                actionEvent -> {
                                    // Check whether some conditions are fulfilled
                                    if (controller.validateNameArgument() == null) {
                                        // the TextField contents are prohibited, so we consume th event
                                        // to prevent the dialog from closing
                                        actionEvent.consume();
                                        // warning alert:
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("Profile edition error");
                                        alert.setHeaderText("Invalid profile name!");
                                        alert.setContentText("The profile name cannot be set to: \"Empty\" "
                                                             + "or left void.");
                                        alert.showAndWait();
                                    }
                                }
        );

        // dialog result processing:
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.processTextInput();
            ProfileData.getProfileDataInstance().forceListChange();
        }

        editedProfile.setBeingEdited(false);
    }
}
