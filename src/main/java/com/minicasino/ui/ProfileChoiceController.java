package com.minicasino.ui;

import com.minicasino.data.ProfileData;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

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
            buttonList.get(i).textProperty().bind(Bindings.createObjectBinding(
                    () -> ProfileData.getProfileDataInstance().getProfileList().get(0).getName(),
                    ProfileData.getProfileDataInstance().getProfileList()));
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

    @FXML
    public void handleProfileButton(ActionEvent event) {
        // set the currently edited profile:
        Button eventSource = (Button) event.getSource();
        int index = buttonList.indexOf(eventSource);
        ProfileData.Profile editedProfile = ProfileData.getProfileDataInstance().getProfileList().get(index);
        editedProfile.setBeingEdited(true); // TODO: move it to the ProfileData from the Profile class
        ProfileData.getProfileDataInstance().setCurrentlyEditedProfile(editedProfile);

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

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewProfileDialogController controller = fxmlLoader.getController();

            // to access the DialogController method:
//            DialogController controller = fxmlLoader.getController();
//            TodoItem newItem = controller.processResults();

        } else {

        }

        editedProfile.setBeingEdited(false);
    }
}
