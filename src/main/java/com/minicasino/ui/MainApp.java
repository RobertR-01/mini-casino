package com.minicasino.ui;

import com.minicasino.data.ProfileData;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class MainApp extends Application {
    public final static String APP_VERSION = "0.1.0";
    private static Scene scene;
    private Exception exception;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main-window"), 800, 500);
        stage.setTitle("MiniCasino \u00A9");
        stage.resizableProperty().set(false);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(this::handleTopBarCloseButton);
        stage.setScene(scene);
        stage.show();

        if (exception != null) {
            ProfileData.getProfileDataInstance().showErrorDialog(scene.getWindow());
        }
    }

    @Override
    public void init() throws Exception {
        // moved from init()
        try {
            ProfileData.getProfileDataInstance().loadProfileData();
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
    }

    @Override
    public void stop() {
        // save profiles (ProfileData) to XML:
        ProfileData.getProfileDataInstance().saveProfileData(ProfileData.getProfileDataInstance().getProfileList());
    }

    public void handleTopBarCloseButton(WindowEvent event) {
        // runLater?
        event.consume();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("This button has been deactivated.");
        alert.setContentText("Use Close button in the Main Menu to close the game.");
        alert.showAndWait();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // utility method for FXMLLoader
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
