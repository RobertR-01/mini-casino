package com.game.minicasino;

import com.game.logic.Slots;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class TestWindowController {
    @FXML
    private GridPane topLayout;
    @FXML
    private HBox hBox;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Label imageLabel;
    @FXML
    private ObservableList<Slots.SlotSymbol> listView;

    public void initialize() {

    }

    @FXML
    public void clickDebug(Event event) {
        System.out.println("Width: " + ((Region) event.getSource()).getWidth());
        System.out.println("Height: " + ((Region) event.getSource()).getHeight());
    }
    @FXML
    public void getElementsSize() {
        System.out.println("StackPane - height: " + stackPane.getHeight() + " - width: " + stackPane.getWidth());
        System.out.println("Button 1 - height: " + button1.getHeight() + " - width: " + button1.getWidth());
        System.out.println("Button 2 - height: " + button2.getHeight() + " - width: " + button2.getWidth());

    }
    @FXML
    public void displayGraphic() {
        Image image = new Image("set_two/meat48.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        imageLabel.graphicProperty().set(imageView);
    }
}
