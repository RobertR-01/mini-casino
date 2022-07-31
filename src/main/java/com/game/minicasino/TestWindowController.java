package com.game.minicasino;

import com.game.logic.Slots;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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

import java.util.Collections;
import java.util.List;

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
    private ObservableList<Slots.SlotSymbol> listView;

    @FXML
    private Label label;
    @FXML
    private ImageView imageView;
    @FXML
    private Button imageButton;

    private List<Image> imagesList;
    private ObjectProperty<Image> imageObjectProperty;

    public void initialize() {
        imagesList = FXCollections.observableArrayList();
        imagesList.add(new Image("set_two/meat48.png"));
        imagesList.add(new Image("set_two/hotdog48.png"));

        imageView = new ImageView();
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);

        imageObjectProperty = new SimpleObjectProperty<>();
        imageView.imageProperty().bind(imageObjectProperty);
        imageObjectProperty.set(imagesList.get(0));

        label.graphicProperty().set(imageView);
    }

    @FXML
    public void handleImageButton() {
        Collections.rotate(imagesList, 1);

        imageObjectProperty.setValue(imagesList.get(0));

//        label.graphicProperty().set(new ImageView(imagesList.get(0)));
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
        label.graphicProperty().set(imageView);
    }
}
