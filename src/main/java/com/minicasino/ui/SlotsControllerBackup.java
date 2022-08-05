package com.minicasino.ui;

import com.minicasino.data.SlotsData;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotsControllerBackup {
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

    private ObservableList<SlotsData.SlotSymbol> reel0SymbolList;
    private ObservableList<SlotsData.SlotSymbol> reel1SymbolList;
    private ObservableList<SlotsData.SlotSymbol> reel2SymbolList;

    // new approach
    private ObjectProperty<SlotsData.SlotSymbol> reel0symbol0;
    private ObjectProperty<SlotsData.SlotSymbol> reel0symbol1;
    private ObjectProperty<SlotsData.SlotSymbol> reel0symbol2;
    private ObjectProperty<SlotsData.SlotSymbol> reel0symbol3;
    private ObjectProperty<SlotsData.SlotSymbol> reel0symbol4;
    private ObjectProperty<SlotsData.SlotSymbol> reel1symbol0;
    private ObjectProperty<SlotsData.SlotSymbol> reel1symbol1;
    private ObjectProperty<SlotsData.SlotSymbol> reel1symbol2;
    private ObjectProperty<SlotsData.SlotSymbol> reel1symbol3;
    private ObjectProperty<SlotsData.SlotSymbol> reel1symbol4;
    private ObjectProperty<SlotsData.SlotSymbol> reel2symbol0;
    private ObjectProperty<SlotsData.SlotSymbol> reel2symbol1;
    private ObjectProperty<SlotsData.SlotSymbol> reel2symbol2;
    private ObjectProperty<SlotsData.SlotSymbol> reel2symbol3;
    private ObjectProperty<SlotsData.SlotSymbol> reel2symbol4;
    private boolean isSpinning;
    private Thread spin0Thread;
    private Thread spin1Thread;
    private Thread spin2Thread;

    @FXML
    public void initialize() {
        // title label setup:
        titleLabel.setFont(Font.font("Times New Roman", 20));

        // spin button setup:
        spinButton.idProperty().set("spinButtonStyle");
        spinButton.fontProperty().set(new Font("Arial Bold", 20.0));

        // list setup:
        reel0SymbolList = FXCollections.observableArrayList(SlotsData.getSlotsInstance().getSymbolsList());
        reel1SymbolList = FXCollections.observableArrayList(SlotsData.getSlotsInstance().getSymbolsList());
        reel2SymbolList = FXCollections.observableArrayList(SlotsData.getSlotsInstance().getSymbolsList());

        // symbols binding:
        // reel0
        reel0symbol0 = new SimpleObjectProperty<>();
        reel0symbol1 = new SimpleObjectProperty<>();
        reel0symbol2 = new SimpleObjectProperty<>();
        reel0symbol3 = new SimpleObjectProperty<>();
        reel0symbol4 = new SimpleObjectProperty<>();
        reel0symbol0.bind(Bindings.valueAt(reel0SymbolList, 0));
        reel0symbol1.bind(Bindings.valueAt(reel0SymbolList, 1));
        reel0symbol2.bind(Bindings.valueAt(reel0SymbolList, 2));
        reel0symbol3.bind(Bindings.valueAt(reel0SymbolList, 3));
        reel0symbol4.bind(Bindings.valueAt(reel0SymbolList, 4));
        // reel1
        reel1symbol0 = new SimpleObjectProperty<>();
        reel1symbol1 = new SimpleObjectProperty<>();
        reel1symbol2 = new SimpleObjectProperty<>();
        reel1symbol3 = new SimpleObjectProperty<>();
        reel1symbol4 = new SimpleObjectProperty<>();
        reel1symbol0.bind(Bindings.valueAt(reel1SymbolList, 0));
        reel1symbol1.bind(Bindings.valueAt(reel1SymbolList, 1));
        reel1symbol2.bind(Bindings.valueAt(reel1SymbolList, 2));
        reel1symbol3.bind(Bindings.valueAt(reel1SymbolList, 3));
        reel1symbol4.bind(Bindings.valueAt(reel1SymbolList, 4));
        // reel2
        reel2symbol0 = new SimpleObjectProperty<>();
        reel2symbol1 = new SimpleObjectProperty<>();
        reel2symbol2 = new SimpleObjectProperty<>();
        reel2symbol3 = new SimpleObjectProperty<>();
        reel2symbol4 = new SimpleObjectProperty<>();
        reel2symbol0.bind(Bindings.valueAt(reel2SymbolList, 0));
        reel2symbol1.bind(Bindings.valueAt(reel2SymbolList, 1));
        reel2symbol2.bind(Bindings.valueAt(reel2SymbolList, 2));
        reel2symbol3.bind(Bindings.valueAt(reel2SymbolList, 3));
        reel2symbol4.bind(Bindings.valueAt(reel2SymbolList, 4));

        // reel0 graphic setup:
        List<ObjectProperty<SlotsData.SlotSymbol>> tempSymbolList = new ArrayList<>();
        Collections.addAll(tempSymbolList, reel0symbol0, reel0symbol1, reel0symbol2, reel0symbol3, reel0symbol4);
        List<Label> tempLabelList = new ArrayList<>();
        Collections.addAll(tempLabelList, reel0pos0, reel0pos1, reel0pos2, reel0pos3, reel0pos4);
        initializeReels(tempSymbolList, tempLabelList);
        // reel1 graphic setup:
        tempSymbolList = new ArrayList<>();
        Collections.addAll(tempSymbolList, reel1symbol0, reel1symbol1, reel1symbol2, reel1symbol3, reel1symbol4);
        tempLabelList = new ArrayList<>();
        Collections.addAll(tempLabelList, reel1pos0, reel1pos1, reel1pos2, reel1pos3, reel1pos4);
        initializeReels(tempSymbolList, tempLabelList);
        // reel2 graphic setup:
        tempSymbolList = new ArrayList<>();
        Collections.addAll(tempSymbolList, reel2symbol0, reel2symbol1, reel2symbol2, reel2symbol3, reel2symbol4);
        tempLabelList = new ArrayList<>();
        Collections.addAll(tempLabelList, reel2pos0, reel2pos1, reel2pos2, reel2pos3, reel2pos4);
        initializeReels(tempSymbolList, tempLabelList);
    }

    private void initializeReels(List<ObjectProperty<SlotsData.SlotSymbol>> symbolList, List<Label> labelList) {
        // ImageView prep:
        for (ObjectProperty<SlotsData.SlotSymbol> symbol : symbolList) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50.0);
            imageView.setFitHeight(50.0);
            imageView.imageProperty().bind(Bindings.createObjectBinding(() -> symbol.getValue().getImage(), symbol));
            // ImageView binding to symbols (wrapped):
            labelList.get(symbol.get().getIndex()).graphicProperty().set(imageView);
        }
    }

    private void shiftSymbolsList(List<SlotsData.SlotSymbol> list) {
        Collections.rotate(list, 1);
    }

    @FXML
    public void handleSpinButton() {
        spinButton.setText("STOP");
        spinButton.idProperty().set("stopButtonStyle"); // for CSS
        spinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleStopButton();
            }
        });
        startSpinning();
    }

    private void startSpinning() {
        // reel0
        Runnable spinReel0 = new Runnable() {
            @Override
            public void run() {
                try {
                    isSpinning = true;
                    while (true) {
                        if (!isSpinning) {
                            break;
                        }

                        shiftSymbolsList(reel0SymbolList);
                        Thread.sleep(125);
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

        // reel1
        Runnable spinReel1 = new Runnable() {
            @Override
            public void run() {
                try {
                    isSpinning = true;
                    Thread.sleep(500);
                    while (true) {
                        if (!isSpinning) {
                            break;
                        }

                        shiftSymbolsList(reel1SymbolList);
                        Thread.sleep(125);
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

        // reel2
        Runnable spinReel2 = new Runnable() {
            @Override
            public void run() {
                try {
                    isSpinning = true;
                    Thread.sleep(1000);
                    while (true) {
                        if (!isSpinning) {
                            break;
                        }

                        shiftSymbolsList(reel2SymbolList);
                        Thread.sleep(125);
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

        spin0Thread = new Thread(spinReel0);
        spin1Thread = new Thread(spinReel1);
        spin2Thread = new Thread(spinReel2);

        spin0Thread.start();
        spin1Thread.start();
        spin2Thread.start();
    }

    private void stopSpinning() {
        isSpinning = false;
        System.out.println(Thread.activeCount());
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
