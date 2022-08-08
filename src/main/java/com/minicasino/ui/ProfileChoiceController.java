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

        NewProfileDialogController controller = fxmlLoader.getController();
        dialog.show();

//        Optional<ButtonType> result = dialog.showAndWait();
        System.out.println("after dialog.show");

        dialog.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                while (true) {
                    System.out.println("entering while");
                    ButtonType result = dialog.getResult();

                    System.out.println("result - " + result);
                    if ((result != null) && (result == ButtonType.OK)) {
                        System.out.println("OK clicked");
                        if (controller.processTextInput()) {
                            System.out.println("name correct. breaking while");
                            break;
                        } else {
                            System.out.println("name incorrect - showing alert");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Warning!");
                                    alert.setContentText("The profile name cannot be set to: \"" + controller.getTextFieldValue()
                                                         + "\" or be left empty");
                                    alert.initOwner(dialog.getDialogPane().getScene().getWindow());
                                    alert.initModality(Modality.APPLICATION_MODAL);
                                    System.out.println("before alert.show");
                                    alert.show();
                                    System.out.println("after alert.show");
                                }
                            });
                        }
                    } else {
                        System.out.println("cancel clicked");
                        break;
                    }
                }

                System.out.println("setting editable to false");
                System.out.println("name set to : " + editedProfile.getName());
                editedProfile.setBeingEdited(false);
                System.out.println("done");
            }
        });


//        System.out.println("setting editable to false");
//        System.out.println("name set to : " + editedProfile.getName());
//        editedProfile.setBeingEdited(false);
//        System.out.println("done");
    }
}
