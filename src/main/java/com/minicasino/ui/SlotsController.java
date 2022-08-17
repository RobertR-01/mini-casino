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
import javafx.scene.control.*;
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
    @FXML
    private Label wildInfoLabel;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button maxBetButton;
    @FXML
    private ToggleButton turboButton;
    @FXML
    private ToggleButton autoSpinButton;

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
        // TODO: instead of loading full balance, load a fraction (with a popup and a slider); update balance on
        //  quitting
        activeProfile = ProfileData.getProfileDataInstance().getActiveProfile();
        ObservableList<ProfileData.Profile> profileList = ProfileData.getProfileDataInstance().getProfileList();
        // TODO: make active profile observable in ProfileData or something; same for edited one
        int index = ProfileData.getProfileDataInstance().getProfileList().indexOf(activeProfile);
        // TODO: potential problem with binding due to lack of list update
        ProfileData.Profile profile = profileList.get(index); // TODO: remove it in favor of profileList.get(i)?
        balanceValueLabel.textProperty().bind(Bindings.createObjectBinding(() -> String.valueOf(profile.getBalance()), profileList));

        // title label setup:
        titleLabel.setFont(Font.font("Times New Roman", 20));

        // spinner setup
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(500, 5000, 500);
        valueFactory.amountToStepByProperty().set(500);
        betAmountSpinner.setValueFactory(valueFactory);

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
        initializeReels(reel0SymbolList, reel0LabelList);
        // reel1 graphic setup:
        reel1LabelList = new ArrayList<>();
        Collections.addAll(reel1LabelList, reel1pos0, reel1pos1, reel1pos2, reel1pos3, reel1pos4);
        initializeReels(reel1SymbolList, reel1LabelList);
        // reel2 graphic setup:
        reel2LabelList = new ArrayList<>();
        Collections.addAll(reel2LabelList, reel2pos0, reel2pos1, reel2pos2, reel2pos3, reel2pos4);
        initializeReels(reel2SymbolList, reel2LabelList);

        // conditions list setup:
        spinConditionList = new ArrayList<>();
        Collections.addAll(spinConditionList, false, false, false);
    }

    // updateReel() in Task; to prevent endless loop in initialize():
    private void initializeReels(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList) {
        Task<Void> initializeReelTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateReel(symbolList, labelList);
                return null;
            }
        };

        new Thread(initializeReelTask).start();
    }

    // used to redraw reels while spinning/nudging:
    private void updateReel(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList) {
        // ImageView prep:
        final boolean[] isIterationDone = {true}; // loop waits for the UI update
        for (int i = 0; i < 5; i++) {
            while (!isIterationDone[0]) {
                sleepCurrentThread(10);
            }
            isIterationDone[0] = false;
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50.0);
            imageView.setFitHeight(50.0);
            imageView.imageProperty().set(symbolList.get(i).getImage());
            int index = i;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelList.get(index).graphicProperty().set(imageView);
                    isIterationDone[0] = true;
                }
            });
        }
    }

    private void shiftSymbolsList(List<SlotsData.SlotSymbol> list, int distance) {
        Collections.rotate(list, distance);
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
        spinButton.disableProperty().set(true);
        setDisablePropertyForButtons(true);
        startSpinning();
    }

    private void startSpinning() {
        Task<Void> startSpinningTask = new Task<Void>() {
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
                sleepCurrentThread(1000);
                // TODO: put into runLater?
                spinButton.disableProperty().set(false);
                return null;
            }
        };

        new Thread(startSpinningTask).start();
    }

    // the "RNG" one:
    private void sleepCurrentThread() {
        // TODO: check if there is actual need for re-instantiation
        Random random = new Random();
        int min = random.nextInt(171, 479);
        random = new Random();
        int max = random.nextInt(475, 822);
        random = new Random();
        sleepCurrentThread(random.nextInt(min, max));
    }

    private void sleepCurrentThread(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void spinReel(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList, int conditionIndex) {
        Task<Void> singleReelSpinTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (spinConditionList.get(conditionIndex)) {
                    shiftSymbolsList(symbolList, 1);
                    updateReel(symbolList, labelList);
                    sleepCurrentThread(20);
                }
                return null;
            }
        };

        new Thread(singleReelSpinTask).start();
    }

    private void nudgeReelFXThread(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList, int distance) {
        shiftSymbolsList(symbolList, distance);
        updateReel(symbolList, labelList);
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
        spinButton.disableProperty().set(true);
        stopSpinning();
    }

    private void stopSpinning() {
        Task<Void> stopReelsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < 3; i++) {
                    spinConditionList.set(i, false);
                    sleepCurrentThread();
                }

                // to prevent any desync, probably crappy way of doing it:
                while (spinConditionList.get(0) || spinConditionList.get(1) || spinConditionList.get(2)) {
                    sleepCurrentThread(500);
                }

                SlotsLogic.ResultsContainer result = currentSession.processResults(reel0SymbolList,
                                                                                   reel1SymbolList,
                                                                                   reel2SymbolList);

                double winnings = 0;

                if (result != null) {
                    winnings = result.getWinnings();
                    System.out.println("there was non-null result produced");
                    System.out.println("ultimate multi: " + result.getMultiplier());
                } else {
                    System.out.println("null pointer exception - result probably == null (no nudge found)");
                }

                System.out.println("calculated winnings: " + winnings);

                if (winnings > 0) {
                    System.out.println("winnings good - updating UI");

                    sleepCurrentThread();
                    // shifting:
                    int distance0 = result.getShiftReel0();
                    int distance1 = result.getShiftReel1();
                    int distance2 = result.getShiftReel2();

                    if (distance0 != 0) {
                        sleepCurrentThread(1000);
                        nudgeReelFXThread(reel0SymbolList, reel0LabelList, distance0);
                        System.out.println("nudging reel0");
                    } else {
                        System.out.println("not nudging reel0");
                    }

                    if (distance1 != 0) {
                        sleepCurrentThread(1000);
                        nudgeReelFXThread(reel1SymbolList, reel1LabelList, distance1);
                        System.out.println("nudging reel1");
                    } else {
                        System.out.println("not nudging reel1");
                    }

                    if (distance2 != 0) {
                        sleepCurrentThread(1000);
                        nudgeReelFXThread(reel2SymbolList, reel2LabelList, distance2);
                        System.out.println("nudging reel2");
                    } else {
                        System.out.println("not nudging reel2");
                    }

                    sleepCurrentThread();

                    activeProfile.increaseBalance(winnings);
                    activeProfile.setHighestWin(winnings); // internal validation

                    double finalWinnings = winnings;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lastWinValueLabel.setText(String.valueOf(finalWinnings));
                            // TODO: remove it
                            ProfileData.getProfileDataInstance().forceListChange(); // moved inside run later due to ex.
                        }
                    });
                } else {
                    System.out.println("winnings busted - not updating UI");
                }

                sleepCurrentThread(1000);
                spinButton.disableProperty().set(false);
                setDisablePropertyForButtons(false);
                System.out.println("fin.");

                return null;
            }
        };

        new Thread(stopReelsTask).start();
    }

    @FXML
    public void handleMainMenuButton() throws IOException {
        MainApp.setRoot("main-window");
    }

    @FXML
    public void maxBetHandler() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) betAmountSpinner.getValueFactory();
        int maxValue = valueFactory.getMax();
        valueFactory.setValue(maxValue);
    }

    private void setDisablePropertyForButtons(boolean isDisabled) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainMenuButton.disableProperty().set(isDisabled);
                infoButton.disableProperty().set(isDisabled);
                betAmountSpinner.disableProperty().set(isDisabled);
                maxBetButton.disableProperty().set(isDisabled);
                turboButton.disableProperty().set(isDisabled);
                autoSpinButton.disableProperty().set(isDisabled);
            }
        });
    }
}
