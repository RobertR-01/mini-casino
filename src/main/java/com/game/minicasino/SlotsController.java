package com.game.minicasino;

import com.game.logic.Slots;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @FXML
    private Button spinButton;

    //    private ObservableList<Slots.SlotSymbol> slotSymbolsObservableList;
    private List<Slots.SlotSymbol> rollingSymbolsList;
    private ImageView firstReelPositionOneImageView;
    private ImageView firstReelPositionTwoImageView;
    private ImageView firstReelPositionThreeImageView;
    private int globalCounter;
    int firstPositionCounter;
    int secondPositionCounter;
    int thirdPositionCounter;

    @FXML
    public void initialize() {
        // title label setup:
        titleLabel.setFont(Font.font("Times New Roman", 20));

        // spin button setup:
        spinButton.idProperty().set("spinButtonStyle");
        spinButton.fontProperty().set(new Font("Arial Bold", 20.0));

        // list setup:
        rollingSymbolsList = new ArrayList<>(Slots.getSlotsInstance().getSymbolsList());
//        slotSymbolsObservableList = FXCollections.observableList(rollingSymbolsList);

        // reels (labels) setup:
        // first reel - position one:
        firstReelPositionOneImageView = new ImageView(rollingSymbolsList.get(0).getImage());
        firstReelPositionOneImageView.setFitWidth(50.0);
        firstReelPositionOneImageView.setFitHeight(50.0);
        ObjectProperty<Image> image = new SimpleObjectProperty<>(rollingSymbolsList.get(0).getImage());
//        firstReelPositionOneImageView.imageProperty().bind(rollingSymbolsList.get(0).getImage());
        // first reel - position two:
        firstReelPositionTwoImageView = new ImageView(rollingSymbolsList.get(1).getImage());
        firstReelPositionTwoImageView.setFitWidth(50.0);
        firstReelPositionTwoImageView.setFitHeight(50.0);
        // first reel - position one:
        firstReelPositionThreeImageView = new ImageView(rollingSymbolsList.get(2).getImage());
        firstReelPositionThreeImageView.setFitWidth(50.0);
        firstReelPositionThreeImageView.setFitHeight(50.0);

        // counters setup (old):
        globalCounter = 0;
        firstPositionCounter = 0;
        secondPositionCounter = 1;
        thirdPositionCounter = 2;
    }

    private void shiftSymbolsList(List<Slots.SlotSymbol> list) {
        Collections.rotate(list, 1);
    }

    private void setImageView(ImageView imageView, List<Slots.SlotSymbol> list, int listIndex) {
        imageView.setImage(list.get(listIndex).getImage());
    }

    @FXML
    public void handleSpinButton() {
        System.out.println("handleSpinButton -> globalCounter = " + globalCounter);
        spinButton.setText("STOP");
        spinButton.idProperty().set("stopButtonStyle");
        spinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleStopButton();
            }
        });

        Runnable spinOneSymbol = new Runnable() {
            @Override
            public void run() {
                try {
                    globalCounter = 0;
                    while (true) {
                        System.out.println("handleSpinButton -> while() -> globalCounter = " + globalCounter);
                        // fix code duplication
                        if (globalCounter >= 50) {
                            break;
                        }

                        shiftSymbolsList(rollingSymbolsList);

                        Thread.sleep(100);
                        globalCounter++;
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stopSpinning();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(spinOneSymbol).start();
    }

    private void startSpinning() {

    }

    private void stopSpinning() {
        spinButton.setText("SPIN");
        spinButton.idProperty().set("spinButtonStyle");
        spinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSpinButton();
            }
        });
        System.out.println("\thandleStopButton -> globalCounter = " + globalCounter);
    }

    @FXML
    public void handleStopButton() {
        globalCounter = 50;
        stopSpinning();
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
