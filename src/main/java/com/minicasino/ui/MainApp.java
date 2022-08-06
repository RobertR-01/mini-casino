package com.minicasino.ui;

import com.minicasino.data.ProfileData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public final static String APP_VERSION = "0.0.1";
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main-window"), 800, 500);
        stage.setTitle("MiniCasino \u00A9");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // save profiles (ProfileData) to XML:
        ProfileData.getProfileDataInstance().saveProfileData(ProfileData.getProfileDataInstance().getProfileList());
    }

    @Override
    public void init() {
        // load profiles (ProfileData) form XML:
        ProfileData.getProfileDataInstance().loadProfileData();
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
