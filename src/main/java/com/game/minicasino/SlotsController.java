package com.game.minicasino;

import com.game.data.Slots;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;
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
    private Label reel1pos1;
    @FXML
    private Label reel1pos2;
    @FXML
    private Label reel1pos3;
    @FXML
    private Button spinButton;

    //    private ObservableList<Slots.SlotSymbol> slotSymbolsObservableList;
    private ObservableList<Slots.SlotSymbol> symbolsList;

    // new approach
    private ObjectProperty<Slots.SlotSymbol> reel1symbol1;
    private ObjectProperty<Slots.SlotSymbol> reel1symbol2;
    private ObjectProperty<Slots.SlotSymbol> reel1symbol3;
    private Image image;
    private ObjectProperty<Image> imageObjectProperty;
    private ObjectProperty<Slots.SlotSymbol> slotSymbolObjectProperty;
    private Slots.SlotSymbol tempSymbol;


    private ImageView firstReelPositionOneImageView;
    private ImageView firstReelPositionTwoImageView;
    private ImageView firstReelPositionThreeImageView;
    private ObjectProperty<Image> firstReelPositionOneImageViewImageObjectProperty;
    private ObjectProperty<Image> firstReelPositionTwoImageViewImageObjectProperty;
    private ObjectProperty<Image> firstReelPositionThreeImageViewImageObjectProperty;
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
        symbolsList = FXCollections.observableArrayList(Slots.getSlotsInstance().getSymbolsList());

        // symbols binding:
        reel1symbol1 = new SimpleObjectProperty<>();
        reel1symbol2 = new SimpleObjectProperty<>();
        reel1symbol3 = new SimpleObjectProperty<>();
        reel1symbol1.bind(Bindings.valueAt(symbolsList, 0));
        reel1symbol2.bind(Bindings.valueAt(symbolsList, 1));
        reel1symbol3.bind(Bindings.valueAt(symbolsList, 2));



        // list <- symbol <- image <- imageview <- set for label
        ObjectProperty<Image> imageProp = new SimpleObjectProperty<>();
        // bind imageProp with reelsymbol
        ObjectProperty<Image> middleman = new SimpleObjectProperty<>();
        middleman.setValue(reel1symbol1.getValue().getImage());

        imageProp.bind(middleman);

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50.0);
        imageView.setFitHeight(50.0);
        imageView.imageProperty().bind(imageProp);
        reel1pos1.graphicProperty().set(imageView);


        // temp refs for midway binding:
//        ObjectProperty<Image> tempImageProp = new SimpleObjectProperty<>();
//        tempImageProp.setValue(reel1symbol1.get().getImage());

//        ObjectProperty<ImageView> tempImageViewProp = new SimpleObjectProperty<>();

//        tempImageProp.bind(reel1symbol1.getValue().getImage());
//        ImageView tempImageView = new ImageView(); // 1 imageview for labels
//        tempImageView.setFitWidth(50.0);
//        tempImageView.setFitHeight(50.0);
//        ObjectProperty<Image> tempObjectProperty = new SimpleObjectProperty<>(); // 1 property for temp imageview
//        tempImageView.imageProperty().bind(tempObjectProperty);


//        tempImageView.setImage(reel1symbol1.get().getImage());

        // labels setup:
//        reel1pos1.graphicProperty().set(tempImageView);
    }

    private void shiftSymbolsList(List<Slots.SlotSymbol> list) {
        Collections.rotate(list, 1);
    }

    private void setImageView(ImageView imageView, List<Slots.SlotSymbol> list, int listIndex) {
        imageView.setImage(list.get(listIndex).getImage());
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
        Runnable spinOneSymbol = new Runnable() {
            @Override
            public void run() {
                try {
                    globalCounter = 0;
                    while (true) {
                        if (globalCounter >= 50) {
                            break;
                        }

                        shiftSymbolsList(symbolsList);
//                        System.out.println(tempSymbol.getImage().getUrl());
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

    private void stopSpinning() {
        globalCounter = 50;
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
        startSpinning();
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
