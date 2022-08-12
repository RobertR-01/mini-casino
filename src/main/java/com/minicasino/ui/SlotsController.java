package com.minicasino.ui;

import com.minicasino.data.ProfileData;
import com.minicasino.data.SlotsData;
import com.minicasino.logic.SlotsLogic;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SlotsController {
    @FXML
    private BorderPane topLevelLayout;
    @FXML
    private GridPane nestedGridPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Button infoButton;
    @FXML
    private Label reel0pos0;
    @FXML
    private Label reel0pos1;
    @FXML
    private Label reel0pos2;
    @FXML
    private Label reel0pos3;
    @FXML
    private Label reel0pos4;
    @FXML
    private Label reel1pos0;
    @FXML
    private Label reel1pos1;
    @FXML
    private Label reel1pos2;
    @FXML
    private Label reel1pos3;
    @FXML
    private Label reel1pos4;
    @FXML
    private Label reel2pos0;
    @FXML
    private Label reel2pos1;
    @FXML
    private Label reel2pos2;
    @FXML
    private Label reel2pos3;
    @FXML
    private Label reel2pos4;
    @FXML
    private Button spinButton;
    @FXML
    private Spinner<Integer> betAmountSpinner;
    @FXML
    private Label lastWinValueLabel;
    @FXML
    private Label balanceValueLabel;

    private List<SlotsData.SlotSymbol> reel0SymbolList;
    private List<SlotsData.SlotSymbol> reel1SymbolList;
    private List<SlotsData.SlotSymbol> reel2SymbolList;
    private List<Label> reel0LabelList;
    private List<Label> reel1LabelList;
    private List<Label> reel2LabelList;
    private List<Boolean> spinConditionList; // which reels should spin/stop
    private SlotsLogic currentSession;
    private ProfileData.Profile activeProfile;

    @FXML
    public void initialize() {
        // removing default focus in this window:
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                topLevelLayout.requestFocus();
            }
        });

        // active profile setup:
        // TODO: instead of loading full balance load a fraction (with a popup and a slider); update balance on quitting
        activeProfile = ProfileData.getProfileDataInstance().getActiveProfile();
        ObservableList<ProfileData.Profile> profileList = ProfileData.getProfileDataInstance().getProfileList();
        // TODO: make active profile observable in ProfileData or something; same for edited one
        int i = ProfileData.getProfileDataInstance().getProfileList().indexOf(activeProfile);
        // TODO: potential problem with binding due to lack of list update
        ProfileData.Profile profile = profileList.get(i); // TODO: remove it in favor of profileList.get(i)?
        balanceValueLabel.textProperty().bind(Bindings.createObjectBinding(() -> String.valueOf(profile.getBalance()), profileList));

        // title label setup:
        titleLabel.setFont(Font.font("Times New Roman", 20));

        // spin button setup:
        spinButton.idProperty().set("spinButtonStyle");
        spinButton.fontProperty().set(new Font("Arial Bold", 20.0));

        // symbol lists setup:
        reel0SymbolList = new ArrayList<>(SlotsData.getSlotsDataInstance().getSymbolsList());
        reel1SymbolList = new ArrayList<>(SlotsData.getSlotsDataInstance().getSymbolsList());
        reel2SymbolList = new ArrayList<>(SlotsData.getSlotsDataInstance().getSymbolsList());

        // reel0 graphic setup:
        reel0LabelList = new ArrayList<>();
        Collections.addAll(reel0LabelList, reel0pos0, reel0pos1, reel0pos2, reel0pos3, reel0pos4);
        initializeReel(reel0SymbolList, reel0LabelList);
        // reel1 graphic setup:
        reel1LabelList = new ArrayList<>();
        Collections.addAll(reel1LabelList, reel1pos0, reel1pos1, reel1pos2, reel1pos3, reel1pos4);
        initializeReel(reel1SymbolList, reel1LabelList);
        // reel2 graphic setup:
        reel2LabelList = new ArrayList<>();
        Collections.addAll(reel2LabelList, reel2pos0, reel2pos1, reel2pos2, reel2pos3, reel2pos4);
        initializeReel(reel2SymbolList, reel2LabelList);

        // conditions list setup:
        spinConditionList = new ArrayList<>();
        Collections.addAll(spinConditionList, false, false, false);
    }

    private void initializeReel(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList) {
        // ImageView prep:
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50.0);
            imageView.setFitHeight(50.0);
            imageView.imageProperty().set(symbolList.get(i).getImage());
            labelList.get(i).graphicProperty().set(imageView);
        }
    }

    private void shiftSymbolsList(List<SlotsData.SlotSymbol> list) {
        Collections.rotate(list, 1);
    }

    @FXML
    public void handleSpinButton() {
        // init current session SlotsLogic:
        double bet = betAmountSpinner.getValue();
        if (bet > activeProfile.getBalance()) {
            // TODO: create warning alert
            // warning alert:
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("You don't have enough cash for this! :(");
            alert.setContentText("Take out a second mortgage on your house or something.");
            alert.showAndWait();
            return;
        }
        currentSession = new SlotsLogic(betAmountSpinner.getValue());
        activeProfile.decreaseBalance(bet);
        // TODO: remove it, bad solution:
        ProfileData.getProfileDataInstance().forceListChange();

        spinButton.setText("STOP");
        spinButton.idProperty().set("stopButtonStyle"); // to ref CSS stylesheet id
        spinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleStopButton();
            }
        });
        startSpinning();
    }

    private void startSpinning() {
        Task<Void> spinReelsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < 3; i++) {
                    spinConditionList.set(i, true);
                }
                spinReel(reel0SymbolList, reel0LabelList, 0);
                sleepCurrentThread();
                spinReel(reel1SymbolList, reel1LabelList, 1);
                sleepCurrentThread();
                spinReel(reel2SymbolList, reel2LabelList, 2);
                return null;
            }
        };

        new Thread(spinReelsTask).start();
    }

    private void sleepCurrentThread() {
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(173, 391));
//            Thread.sleep(0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void spinReel(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList, int conditionIndex) {
        Task<Void> reelTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (spinConditionList.get(conditionIndex)) {
                    shiftSymbolsList(symbolList);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 5; i++) {
                                ImageView imageView = new ImageView();
                                imageView.setFitWidth(50.0);
                                imageView.setFitHeight(50.0);
                                imageView.imageProperty().set(symbolList.get(i).getImage());
                                labelList.get(i).graphicProperty().set(imageView);
                            }
                        }
                    });
                    Thread.sleep(20);
                }
                return null;
            }
        };

        new Thread(reelTask).start();
    }

    @FXML
    public void handleStopButton() {
        spinButton.setText("SPIN");
        spinButton.idProperty().set("spinButtonStyle"); // to ref CSS stylesheet id
        spinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSpinButton();
            }
        });
        stopSpinning();

        // resolving the current game session (calculations etc.):
        currentSession.loadRecentResults(reel0SymbolList, reel1SymbolList, reel2SymbolList);
//        double winnings = currentSession.calculateWinnings();
        SlotsLogic.ResultsContainer result = currentSession.processResults(reel0SymbolList,
                                                                           reel1SymbolList,
                                                                           reel2SymbolList);
        double winnings = result.getWinnings();
        if (winnings != 0) {
            // UI update
            // ----test----
//            sleepCurrentThread();// sleep
//            Task<Void> nudgeTask = new Task<>() {
//                @Override
//                protected Void call() throws Exception {
//                    Collections.rotate(reel0SymbolList, result.getShiftReel0());
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < 5; i++) {
//                                ImageView imageView = new ImageView();
//                                imageView.setFitWidth(50.0);
//                                imageView.setFitHeight(50.0);
//                                imageView.imageProperty().set(reel0SymbolList.get(i).getImage());
//                                reel0LabelList.get(i).graphicProperty().set(imageView);
//                            }
//                        }
//                    });
//                    return null;
//                }
//            };
//
//            new Thread(nudgeTask).start();
//            Task<Void> nudge1Task = new Task<>() {
//                @Override
//                protected Void call() throws Exception {
//                    Collections.rotate(reel1SymbolList, result.getShiftReel1());
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < 5; i++) {
//                                ImageView imageView = new ImageView();
//                                imageView.setFitWidth(50.0);
//                                imageView.setFitHeight(50.0);
//                                imageView.imageProperty().set(reel1SymbolList.get(i).getImage());
//                                reel1LabelList.get(i).graphicProperty().set(imageView);
//                            }
//                        }
//                    });
//                    return null;
//                }
//            };
//
//            new Thread(nudge1Task).start();
//            Task<Void> nudge2Task = new Task<>() {
//                @Override
//                protected Void call() throws Exception {
//                    Collections.rotate(reel2SymbolList, result.getShiftReel2());
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < 5; i++) {
//                                ImageView imageView = new ImageView();
//                                imageView.setFitWidth(50.0);
//                                imageView.setFitHeight(50.0);
//                                imageView.imageProperty().set(reel2SymbolList.get(i).getImage());
//                                reel2LabelList.get(i).graphicProperty().set(imageView);
//                            }
//                        }
//                    });
//                    return null;
//                }
//            };
//
//            new Thread(nudge2Task).start();
            // ----test----

            activeProfile.increaseBalance(winnings);
            activeProfile.setHighestWin(winnings); // internal validation
            // TODO: remove it
            ProfileData.getProfileDataInstance().forceListChange();

            // update last win label:
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lastWinValueLabel.setText(String.valueOf(winnings));
                }
            });
        }
    }

    private void stopSpinning() {
        Task<Void> stopReelsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < 3; i++) {
                    spinConditionList.set(i, false);
                    sleepCurrentThread();
                }
                return null;
            }
        };

        new Thread(stopReelsTask).start();
    }

    public void performNudge() {

    }

    @FXML
    public void handleMainMenuButton() throws IOException {
        MainApp.setRoot("main-window");
    }
}
