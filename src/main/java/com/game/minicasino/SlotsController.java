package com.game.minicasino;

import com.game.logic.Slots;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;

public class SlotsController {
    @FXML
    private GridPane nestedGridPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Button infoButton;
    @FXML
    private GridPane firstReel;
    @FXML
    private Label firstReelPositionOne;
    @FXML
    private Label firstReelPositionTwo;
    @FXML
    private Label firstReelPositionThree;

    private ObservableList<Slots.SlotSymbol> observableFirstReel;

    @FXML
    public void initialize() {
        // title label setup
        titleLabel.setFont(Font.font("Times New Roman", 20));

        observableFirstReel = FXCollections.observableArrayList(Slots.getSlotsInstance().getSymbolsList());


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
