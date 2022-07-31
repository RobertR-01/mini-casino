package com.game.minicasino;

import com.game.logic.Slots;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.List;

public class SlotsControllerBackup {
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

    private ObservableList<Slots.SlotSymbol> slotSymbolsObservableList;
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
        // title label setup
        titleLabel.setFont(Font.font("Times New Roman", 20));

        // spin button setup
        spinButton.idProperty().set("spinButtonStyle");
        spinButton.fontProperty().set(new Font("Arial Bold", 20.0));

        // list modifications
        slotSymbolsObservableList = FXCollections.observableArrayList(Slots.getSlotsInstance().getSymbolsList());

        // initializing reels
        rollingSymbolsList = Slots.getSlotsInstance().getSymbolsList();
        firstReelPositionOneImageView = (ImageView) firstReelPositionOne.getGraphic();
        firstReelPositionTwoImageView = (ImageView) firstReelPositionTwo.getGraphic();
        firstReelPositionThreeImageView = (ImageView) firstReelPositionThree.getGraphic();
        globalCounter = 0;
        firstPositionCounter = 0;
        secondPositionCounter = 1;
        thirdPositionCounter = 2;

        // initial symbols on reels
        firstReelPositionThreeImageView.setImage(rollingSymbolsList.get(0).getImage());
        firstReelPositionTwoImageView.setImage(rollingSymbolsList.get(1).getImage());
        firstReelPositionOneImageView.setImage(rollingSymbolsList.get(2).getImage());
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
                        for (int i = 0; i < rollingSymbolsList.size(); i++) {
                            System.out.println("handleSpinButton -> for() -> globalCounter = " + globalCounter);
                            // fix code duplication
                            if (globalCounter >= 50) {
                                break;
                            }
//                            System.out.println("First counter: " + firstPositionCounter);
                            firstReelPositionThreeImageView.setImage(rollingSymbolsList.get(firstPositionCounter).getImage());
                            firstPositionCounter++;
                            if (firstPositionCounter > 9) {
                                firstPositionCounter = 0;
                            }

//                            System.out.println("Second counter: " + secondPositionCounter);
                            firstReelPositionTwoImageView.setImage(rollingSymbolsList.get(secondPositionCounter).getImage());
                            secondPositionCounter++;
                            if (secondPositionCounter > 9) {
                                secondPositionCounter = 0;
                            }

//                            System.out.println("Third counter: " + thirdPositionCounter);
                            firstReelPositionOneImageView.setImage(rollingSymbolsList.get(thirdPositionCounter).getImage());
                            thirdPositionCounter++;
                            if (thirdPositionCounter > 9) {
                                thirdPositionCounter = 0;
                            }

                            Thread.sleep(100);
                            globalCounter++;
                        }
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
