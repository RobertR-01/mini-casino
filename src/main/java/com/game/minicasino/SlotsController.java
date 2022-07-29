package com.game.minicasino;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;

public class SlotsController {
    @FXML
    private Label titleLabel;
    @FXML
    private Button infoButton;

    @FXML
    public void initialize() {
        // title label setup
        titleLabel.setFont(Font.font("Arial", 20));


    }

    @FXML
    public void handleMainMenuButton() throws IOException {
        MainApp.setRoot("main-window");
    }

    @FXML
    public void clickDebug(Event event) {
        System.out.println("Width: " + ((Region) event.getSource()).getWidth());
        System.out.println("Height: " + ((Region) event.getSource()).getHeight());
    }
}
