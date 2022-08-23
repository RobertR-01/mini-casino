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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.*;

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
    private boolean isAutoOn;
    private boolean isAutoSpinDone;
    private boolean isAutoSessionDone;
    private boolean forceAutoStop;
    private boolean isTurboOn;
    private int turboModeDelay;

    @FXML
    public void initialize() {
        // removing default focus in this window:
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                topLevelLayout.requestFocus();
            }
        });

        // auto-spin initially off:
        isAutoOn = false;
        isAutoSpinDone = false;
        isAutoSessionDone = false;

        // turbo-mode initially off:
        turboModeDelay = 20;
        isTurboOn = false;

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

        // spinner setup:
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

        // TODO: consider synchronization for the reels' init
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

        // conditions list setup (initially off):
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
        final boolean[] isIterationDone = new boolean[1];
        for (int i = 0; i < 5; i++) {
            isIterationDone[0] = false;
            // ImageView prep:
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50.0);
            imageView.setFitHeight(50.0);
            imageView.imageProperty().set(symbolList.get(i).getImage());
            int index = i;
            // label update in the UI:
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    labelList.get(index).graphicProperty().set(imageView);
                    isIterationDone[0] = true;
                }
            });

            // TODO: check if this step is actually required
            // loop waits for the UI update before proceeding
            while (!isIterationDone[0]) {
                sleepCurrentThread(1); // TODO: try messing with that timer
            }
        }
    }

    private void shiftSymbolsList(List<SlotsData.SlotSymbol> list, int distance) {
        Collections.rotate(list, distance);
    }

    @FXML
    public void handleSpinButton() {
        // bet check:
        double bet = betAmountSpinner.getValue();
        if (bet > activeProfile.getBalance()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("You don't have enough cash for this! :(");
            alert.setContentText("Take out a second mortgage on your house or something.");
            alert.showAndWait();
            return;
        }
        // init current session SlotsLogic:
        currentSession = new SlotsLogic(betAmountSpinner.getValue());
        activeProfile.decreaseBalance(bet);
        // TODO: remove it, bad solution:
        ProfileData.getProfileDataInstance().forceListChange(); // UI Thread

        spinButton.setText("STOP");
        spinButton.idProperty().set("stopButtonStyle");
        spinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleStopButton();
            }
        });
        // disable all buttons to prevent the user from messing with the UI while the session is in progress:
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
                spinReelContinuously(reel0SymbolList, reel0LabelList, 0);
                if (!isTurboOn) {
                    sleepCurrentThread();
                }
                spinReelContinuously(reel1SymbolList, reel1LabelList, 1);
                if (!isTurboOn) {
                    sleepCurrentThread();
                }
                spinReelContinuously(reel2SymbolList, reel2LabelList, 2);
                // wait 1s before unlocking stop button:
                sleepCurrentThread(1000);
                // TODO: put into runLater?
                // unlocks stop button to allow stopping the reels (only in the manual mode):
                if (!isAutoOn) {
                    spinButton.disableProperty().set(false);
                }

                // gives the reels 3s of spinning in auto mode:
                if (isAutoOn) {
                    int secondsCounter = 0;
                    while (true) {
                        sleepCurrentThread(1000);
                        secondsCounter++;
                        if (secondsCounter == 2) {
                            isAutoSpinDone = true;
                            break;
                        }
                    }
                }
                return null;
            }
        };

        new Thread(startSpinningTask).start();
    }

    // the "RNG" one:
    private void sleepCurrentThread() {
        // re-instantiation to get a new seed for each step:
        // TODO: find a better way of randomization
        Random random = new Random();
        int min = random.nextInt(381, 578);
        random = new Random();
        int max = random.nextInt(721, 1210);
        random = new Random();
        sleepCurrentThread(random.nextInt(min, max));
    }

    // the fixed delay version:
    private void sleepCurrentThread(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // performs both continuous list shifting and UI (reel graphics) updates:
    // (the process is manually synchronized for now)
    // TODO: find a better way of synchronizing it
    private void spinReelContinuously(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList,
                                      int conditionIndex) {
        Task<Void> singleReelSpinTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (spinConditionList.get(conditionIndex)) {
                    nudgeReelOnce(symbolList, labelList, 1);
                    sleepCurrentThread(20);
                }
                return null;
            }
        };

        new Thread(singleReelSpinTask).start();
    }

    // used both as a nudge functionality and as a part of the spinReelContinuously method (single step):
    // (both instructions run on the same thread)
    private void nudgeReelOnce(List<SlotsData.SlotSymbol> symbolList, List<Label> labelList, int distance) {
        shiftSymbolsList(symbolList, distance);
        updateReel(symbolList, labelList);
    }

    @FXML
    public void handleStopButton() {
        spinButton.setText("SPIN");
        spinButton.idProperty().set("spinButtonStyle");
        spinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleSpinButton();
            }
        });
        // disable spin button to prevent re-spinning too early:
        spinButton.disableProperty().set(true);
        stopSpinning();
    }

    private void stopSpinning() {
        Task<Void> stopReelsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // stops the reels, one by one:
                for (int i = 0; i < 3; i++) {
                    spinConditionList.set(i, false);
                    if (isTurboOn) {
                        sleepCurrentThread(turboModeDelay);
                    } else {
                        sleepCurrentThread();
                    }
                }

                // to prevent any desync, probably crappy way of doing it:
                while (spinConditionList.get(0) || spinConditionList.get(1) || spinConditionList.get(2)) {
                    sleepCurrentThread(1);
                }

                // gets info on possible nudges, acquired multiplier and winnings:
                SlotsLogic.ResultsContainer result = currentSession.processResults(reel0SymbolList,
                                                                                   reel1SymbolList,
                                                                                   reel2SymbolList);

                double winnings = 0;

                if (result != null) {
                    winnings = result.getWinnings();
                } else {
                    System.out.println("null pointer exception - result probably == null (no nudge found)");
                }

                if (winnings > 0) {
                    if (isTurboOn) {
                        sleepCurrentThread(turboModeDelay);
                    } else {
                        sleepCurrentThread();
                    }
                    // which reels to shift:
                    int distance0 = result.getShiftReel0();
                    int distance1 = result.getShiftReel1();
                    int distance2 = result.getShiftReel2();

                    // the 3 statements below perform any possible nudges:
                    if (distance0 != 0) {
                        sleepCurrentThread(500);
                        nudgeReelOnce(reel0SymbolList, reel0LabelList, distance0);
                    }

                    if (distance1 != 0) {
                        sleepCurrentThread(500);
                        nudgeReelOnce(reel1SymbolList, reel1LabelList, distance1);
                    }

                    if (distance2 != 0) {
                        sleepCurrentThread(500);
                        nudgeReelOnce(reel2SymbolList, reel2LabelList, distance2);
                    }

                    if (isTurboOn) {
                        sleepCurrentThread(turboModeDelay);
                    } else {
                        sleepCurrentThread();
                    }

                    activeProfile.increaseBalance(winnings);
                    activeProfile.setHighestWin(winnings); // internal validation present

                    double finalWinnings = winnings;
                    final boolean[] isUIUpdateDone = new boolean[1];
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lastWinValueLabel.setText(String.valueOf(finalWinnings));
                            // TODO: remove it, possibly a bad way of updating the data bound objects
                            ProfileData.getProfileDataInstance().forceListChange();
                            isUIUpdateDone[0] = true;
                        }
                    });

                    // wait for the UI update:
                    while (!isUIUpdateDone[0]) {
                        sleepCurrentThread(1);
                    }
                }

                sleepCurrentThread(500);
                // re-enables all buttons (if not in auto-mode):
                if (!isAutoOn) {
                    spinButton.disableProperty().set(false);
                    setDisablePropertyForButtons(false);
                }

                // flag for auto mode:
                if (isAutoOn) {
                    isAutoSessionDone = true;
                }

                return null;
            }
        };

        new Thread(stopReelsTask).start();
    }

    @FXML
    public void handleMainMenuButton() throws IOException {
        MainApp.setRoot("main-window");
    }

    // sets spinner to the max value:
    @FXML
    public void maxBetHandler() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) betAmountSpinner.getValueFactory();
        int maxValue = valueFactory.getMax();
        valueFactory.setValue(maxValue);
    }

    // disables/enables all buttons but the spin/stop one:
    private void setDisablePropertyForButtons(boolean isDisabled) {
        // TODO: check if this really has to be done over UI Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainMenuButton.disableProperty().set(isDisabled);
                infoButton.disableProperty().set(isDisabled);
                betAmountSpinner.disableProperty().set(isDisabled);
                maxBetButton.disableProperty().set(isDisabled);
                turboButton.disableProperty().set(isDisabled);
                // to keep auto button enabled in auto mode (so it can turn the mode off):
                if (!isAutoOn || !isDisabled) {
                    autoSpinButton.disableProperty().set(isDisabled);
                }
            }
        });
    }

    @FXML
    public void autoSpinButtonHandler() {
        boolean isToggled = autoSpinButton.isSelected();

        if (isToggled) {
            isAutoOn = true;
            forceAutoStop = false;
        } else {
            forceAutoStop = true;
            autoSpinButton.disableProperty().set(true);
        }

        if (isToggled) {
            setDisablePropertyForButtons(true);
            spinButton.disableProperty().set(true);
            // the auto loop (in Task):
            Task<Void> autoSpinTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (true) {
                        isAutoSessionDone = false;
                        isAutoSpinDone = false;

                        if (forceAutoStop) {
                            break;
                        }

                        double bet = betAmountSpinner.getValue();
                        if (bet > activeProfile.getBalance()) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("You don't have enough cash for auto-spinning! :(");
                                    alert.setContentText("Take out a second mortgage on your house or something.");
                                    alert.showAndWait();
                                }
                            });
                            forceAutoStop = true;
                        } else {
                            currentSession = new SlotsLogic(betAmountSpinner.getValue());
                            activeProfile.decreaseBalance(bet);
                            final boolean[] isUIUpdateDone = new boolean[1];
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO: remove it, bad solution:
                                    ProfileData.getProfileDataInstance().forceListChange();
                                    isUIUpdateDone[0] = true;
                                }
                            });

                            // wait for the UI update:
                            while (!isUIUpdateDone[0]) {
                                sleepCurrentThread(1);
                            }

                            startSpinning();

                            // allowing the reels to spin for a while (timer in the startSpinning()):
                            while (!isAutoSpinDone) {
                                sleepCurrentThread(1);
                            }

                            stopSpinning();

                            // waiting for the current session to resolve (var set in the stopSpinning()):
                            while (!isAutoSessionDone) {
                                sleepCurrentThread(10);
                            }

                            // some additional grace period, for visual fluency only:
                            sleepCurrentThread(500);
                        }
                    }

                    // re-enabling buttons:
                    setDisablePropertyForButtons(false);
                    spinButton.disableProperty().set(false);

                    // TODO: inspect that bit (autoSpinButton is set in setDisablePropertyForButtons)
                    isAutoOn = false;
                    autoSpinButton.disableProperty().set(false);

                    return null;
                }
            };

            new Thread(autoSpinTask).start();
        }
    }

    @FXML
    public void turboButtonHandler() {
        isTurboOn = turboButton.isSelected();
    }

    @FXML
    public void handleInfoButton() {
        // set up the new dialog:
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(topLevelLayout.getScene().getWindow());
        dialog.setTitle("Info Dialog");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("slots-info-dialog-1.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog.");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        // adding arrow buttons:
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//
//        NewProfileDialogController controller = fxmlLoader.getController();
//
//        // event filter for input validation:
//        final Button buttonOK = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
//        buttonOK.addEventFilter(ActionEvent.ACTION, actionEvent -> {
//            // Check whether some conditions are fulfilled
//            if (controller.validateNameArgument() == null) {
//                // the TextField contents are prohibited, so we consume th event
//                // to prevent the dialog from closing
//                actionEvent.consume();
//                // warning alert:
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Profile edition error");
//                alert.setHeaderText("Invalid profile name!");
//                alert.setContentText("The profile name cannot be set to: \"Empty\" or left void.");
//                alert.showAndWait();
//            }
//        });

        // dialog result processing:
        Optional<ButtonType> result = dialog.showAndWait();
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            controller.processTextInput();
//            // TODO: do something about this god damnit
//            ProfileData.getProfileDataInstance().forceListChange();
//        }
//
//        editedProfile.setBeingEdited(false);
    }

}
