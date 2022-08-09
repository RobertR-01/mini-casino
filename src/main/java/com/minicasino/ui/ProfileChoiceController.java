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
import org.jdom2.Element;

import java.io.IOException;
import java.util.*;

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
    private HBox profile1HBox;
    @FXML
    private HBox profile2HBox;
    @FXML
    private HBox profile3HBox;
    @FXML
    private HBox profile4HBox;
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
    private Map<Integer, HBox> profileHBoxMap;

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

        // HBox map for fetching Buttons, RadioButtons etc.:
        profileHBoxMap = Map.of(0, profile0HBox, 1, profile1HBox, 2, profile2HBox, 3, profile3HBox, 4, profile4HBox);

        // data binding
        for (Map.Entry<Integer, HBox> hBoxEntry : profileHBoxMap.entrySet()) {
            Button button = (Button) hBoxEntry.getValue().getChildren().get(1);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleProfileButton(event);
                }
            });
            int index = hBoxEntry.getKey();
            button.textProperty().bind(Bindings.createObjectBinding(
                    () -> ProfileData.getProfileDataInstance().getProfileList().get(index).getName(),
                    ProfileData.getProfileDataInstance().getProfileList()));
        }

        // selecting previously set active profile (row):
        ProfileData.Profile activeProfile = ProfileData.getProfileDataInstance().getActiveProfile();
        if (activeProfile != null) {
            int index = ProfileData.getProfileDataInstance().getProfileList().indexOf(activeProfile);
            profileHBoxMap.get(index).setId("selectedProfileRow");
            ((Toggle) profileHBoxMap.get(index).getChildren().get(2)).setSelected(true);
        }

/*        // old
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
        }*/
    }

    @FXML
    public void handleBackButton() throws IOException {
        MainApp.setRoot("main-window");
    }

    @FXML
    public void handleRadioButton(ActionEvent event) {
        // TODO: consider data binding for style property -> isActive of the profile in the profile list
        // finding event source - selected toggle:
        Toggle sourceRadioButton = (Toggle) event.getSource();
        int index = findHBoxMapKeyByChild(profileHBoxMap, (Node) sourceRadioButton);
        // setting all profiles to inactive first:
        for (ProfileData.Profile profile : ProfileData.getProfileDataInstance().getProfileList()) {
            profile.setActive(false);
        }
        // unselecting all rows:
        for (Map.Entry<Integer, HBox> entry : profileHBoxMap.entrySet()) {
            entry.getValue().setId("unselectedProfileRow");
        }
        // selecting the current row:
        profileHBoxMap.get(index).setId("selectedProfileRow"); // border
        // setting active profile:
        ProfileData.getProfileDataInstance().getProfileList().get(index).setActive(true);


/*

        // old - using current and previously active profile:
        // TODO: consider data binding for style property -> isActive of the profile in the profile list
        // selecting the current row:
        Toggle sourceRadioButton = (Toggle) event.getSource();
        int index = findHBoxMapKeyByChild(profileHBoxMap, (Node) sourceRadioButton);
        profileHBoxMap.get(index).setId("selectedProfileRow"); // border
        // setting active profile:
        ProfileData.getProfileDataInstance().getProfileList().get(index).setActive(true);

        // skipped if there was no active profile before:
        if (previouslySelectedRadioButton != null) {
            int previouslySelectedIndex = findHBoxMapKeyByChild(profileHBoxMap, (Node) previouslySelectedRadioButton);
            // setting previous profile as inactive:
            ProfileData.getProfileDataInstance().getProfileList().get(previouslySelectedIndex).setActive(false);
            // unselecting the previously selected row:
            ((Node) previouslySelectedRadioButton).getParent().setId("unselectedProfileRow"); // no border
        }
        previouslySelectedRadioButton = sourceRadioButton;*/
    }

    private int findHBoxMapKeyByChild(Map<Integer, HBox> map, Node child) {
        for (Map.Entry<Integer, HBox> entry : map.entrySet()) {
            for (Node node : entry.getValue().getChildren()) {
                if (node.equals(child)) {
                    return entry.getKey();
                }
            }
        }
        System.out.println("ProfileChoiceController.findHBoxMapKeyByChild() -> no such key");
        return -1;
    }

//    private int findMapKeyByButton(Map<Integer, HBox> map, Button button) {
//        for (Map.Entry<Integer, HBox> entry : map.entrySet()) {
//            if (entry.getValue().getChildren().get(1).equals(button)) {
//                return entry.getKey();
//            }
//        }
//        System.out.println("ProfileChoiceController.findMapKeyByButton() -> no such key");
//        return -1;
//    }

    @FXML
    public void handleProfileButton(ActionEvent event) {
        // set the currently edited profile:
        Button eventSource = (Button) event.getSource();
        // TODO: check for -1 from findMapKeyByButton
        int index = findHBoxMapKeyByChild(profileHBoxMap, eventSource);
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
