package com.game.minicasino;

import com.game.data.Slots;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

public class SlotsController {
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

    private List<Slots.SlotSymbol> reel0SymbolList;
    private List<Slots.SlotSymbol> reel1SymbolList;
    private List<Slots.SlotSymbol> reel2SymbolList;

    // new approach
    private Slots.SlotSymbol reel0symbol0;
    private Slots.SlotSymbol reel0symbol1;
    private Slots.SlotSymbol reel0symbol2;
    private Slots.SlotSymbol reel0symbol3;
    private Slots.SlotSymbol reel0symbol4;
    private Slots.SlotSymbol reel1symbol0;
    private Slots.SlotSymbol reel1symbol1;
    private Slots.SlotSymbol reel1symbol2;
    private Slots.SlotSymbol reel1symbol3;
    private Slots.SlotSymbol reel1symbol4;
    private Slots.SlotSymbol reel2symbol0;
    private Slots.SlotSymbol reel2symbol1;
    private Slots.SlotSymbol reel2symbol2;
    private Slots.SlotSymbol reel2symbol3;
    private Slots.SlotSymbol reel2symbol4;
    private boolean isSpinning;

    @FXML
    public void initialize() {
        // title label setup:
        titleLabel.setFont(Font.font("Times New Roman", 20));

        // spin button setup:
        spinButton.idProperty().set("spinButtonStyle");
        spinButton.fontProperty().set(new Font("Arial Bold", 20.0));

        // list setup:
        reel0SymbolList = new ArrayList<>(Slots.getSlotsInstance().getSymbolsList());
        reel1SymbolList = new ArrayList<>(Slots.getSlotsInstance().getSymbolsList());
        reel2SymbolList = new ArrayList<>(Slots.getSlotsInstance().getSymbolsList());

        // symbols binding:
        // reel0
        reel0symbol0 = reel0SymbolList.get(0);
        reel0symbol1 = reel0SymbolList.get(1);
        reel0symbol2 = reel0SymbolList.get(2);
        reel0symbol3 = reel0SymbolList.get(3);
        reel0symbol4 = reel0SymbolList.get(4);
        // reel1
        reel1symbol0 = reel1SymbolList.get(0);
        reel1symbol1 = reel1SymbolList.get(1);
        reel1symbol2 = reel1SymbolList.get(2);
        reel1symbol3 = reel1SymbolList.get(3);
        reel1symbol4 = reel1SymbolList.get(4);
        // reel2
        reel2symbol0 = reel2SymbolList.get(0);
        reel2symbol1 = reel2SymbolList.get(1);
        reel2symbol2 = reel2SymbolList.get(2);
        reel2symbol3 = reel2SymbolList.get(3);
        reel2symbol4 = reel2SymbolList.get(4);

        // reel0 graphic setup:
        List<Slots.SlotSymbol> tempSymbolList = new ArrayList<>();
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

    private void initializeReels(List<Slots.SlotSymbol> symbolList, List<Label> labelList) {
        // ImageView prep:
        for (Slots.SlotSymbol symbol : symbolList) {
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50.0);
            imageView.setFitHeight(50.0);
            imageView.imageProperty().set(symbol.getImage());
            // ImageView binding to symbols (wrapped):
            labelList.get(symbol.getIndex()).graphicProperty().set(imageView);
        }
    }

    private void shiftSymbolsList(List<Slots.SlotSymbol> list) {
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
        isSpinning = true;
        // task test
        // 0
        Task<Void> reel0Task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (isSpinning) {
                    shiftSymbolsList(reel0SymbolList);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView0 = new ImageView();
                            imageView0.setFitWidth(50.0);
                            imageView0.setFitHeight(50.0);
                            imageView0.imageProperty().set(reel0SymbolList.get(0).getImage());
                            reel0pos0.graphicProperty().set(imageView0);
                            ImageView imageView1 = new ImageView();
                            imageView1.setFitWidth(50.0);
                            imageView1.setFitHeight(50.0);
                            imageView1.imageProperty().set(reel0SymbolList.get(1).getImage());
                            reel0pos1.graphicProperty().set(imageView1);
                            ImageView imageView2 = new ImageView();
                            imageView2.setFitWidth(50.0);
                            imageView2.setFitHeight(50.0);
                            imageView2.imageProperty().set(reel0SymbolList.get(2).getImage());
                            reel0pos2.graphicProperty().set(imageView2);
                            ImageView imageView3 = new ImageView();
                            imageView3.setFitWidth(50.0);
                            imageView3.setFitHeight(50.0);
                            imageView3.imageProperty().set(reel0SymbolList.get(3).getImage());
                            reel0pos3.graphicProperty().set(imageView3);
                            ImageView imageView4 = new ImageView();
                            imageView4.setFitWidth(50.0);
                            imageView4.setFitHeight(50.0);
                            imageView4.imageProperty().set(reel0SymbolList.get(4).getImage());
                            reel0pos4.graphicProperty().set(imageView4);

                        }
                    });
                    Thread.sleep(150);
                }
                return null;
            }
        };

        new Thread(reel0Task).start();

        // 1
        Task<Void> reel1Task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(500);
                while (isSpinning) {
                    shiftSymbolsList(reel1SymbolList);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView0 = new ImageView();
                            imageView0.setFitWidth(50.0);
                            imageView0.setFitHeight(50.0);
                            imageView0.imageProperty().set(reel1SymbolList.get(0).getImage());
                            reel1pos0.graphicProperty().set(imageView0);
                            ImageView imageView1 = new ImageView();
                            imageView1.setFitWidth(50.0);
                            imageView1.setFitHeight(50.0);
                            imageView1.imageProperty().set(reel1SymbolList.get(1).getImage());
                            reel1pos1.graphicProperty().set(imageView1);
                            ImageView imageView2 = new ImageView();
                            imageView2.setFitWidth(50.0);
                            imageView2.setFitHeight(50.0);
                            imageView2.imageProperty().set(reel1SymbolList.get(2).getImage());
                            reel1pos2.graphicProperty().set(imageView2);
                            ImageView imageView3 = new ImageView();
                            imageView3.setFitWidth(50.0);
                            imageView3.setFitHeight(50.0);
                            imageView3.imageProperty().set(reel1SymbolList.get(3).getImage());
                            reel1pos3.graphicProperty().set(imageView3);
                            ImageView imageView4 = new ImageView();
                            imageView4.setFitWidth(50.0);
                            imageView4.setFitHeight(50.0);
                            imageView4.imageProperty().set(reel1SymbolList.get(4).getImage());
                            reel1pos4.graphicProperty().set(imageView4);

                        }
                    });
                    Thread.sleep(150);
                }
                return null;
            }
        };

        new Thread(reel1Task).start();

        // 2
        Task<Void> reel2Task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(1000);
                while (isSpinning) {
                    shiftSymbolsList(reel2SymbolList);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imageView0 = new ImageView();
                            imageView0.setFitWidth(50.0);
                            imageView0.setFitHeight(50.0);
                            imageView0.imageProperty().set(reel2SymbolList.get(0).getImage());
                            reel2pos0.graphicProperty().set(imageView0);
                            ImageView imageView1 = new ImageView();
                            imageView1.setFitWidth(50.0);
                            imageView1.setFitHeight(50.0);
                            imageView1.imageProperty().set(reel2SymbolList.get(1).getImage());
                            reel2pos1.graphicProperty().set(imageView1);
                            ImageView imageView2 = new ImageView();
                            imageView2.setFitWidth(50.0);
                            imageView2.setFitHeight(50.0);
                            imageView2.imageProperty().set(reel2SymbolList.get(2).getImage());
                            reel2pos2.graphicProperty().set(imageView2);
                            ImageView imageView3 = new ImageView();
                            imageView3.setFitWidth(50.0);
                            imageView3.setFitHeight(50.0);
                            imageView3.imageProperty().set(reel2SymbolList.get(3).getImage());
                            reel2pos3.graphicProperty().set(imageView3);
                            ImageView imageView4 = new ImageView();
                            imageView4.setFitWidth(50.0);
                            imageView4.setFitHeight(50.0);
                            imageView4.imageProperty().set(reel2SymbolList.get(4).getImage());
                            reel2pos4.graphicProperty().set(imageView4);

                        }
                    });
                    Thread.sleep(150);
                }
                return null;
            }
        };

        new Thread(reel2Task).start();
    }

    private void stopSpinning() {
        isSpinning = false;
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
