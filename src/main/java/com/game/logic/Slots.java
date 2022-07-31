package com.game.logic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.List;

public class Slots {
    private static final Slots slotsInstance = new Slots();
    private  ObservableList<SlotSymbol> symbolsList;

    private Slots() {
        symbolsList = FXCollections.observableArrayList();
        addSymbol(new SlotSymbol(0, new Image("set_two/meat48.png"), 4, false, false));
        addSymbol(new SlotSymbol(1, new Image("set_two/hotdog48.png"), 3, false, false));
        addSymbol(new SlotSymbol(2, new Image("set_two/cheese48.png"), 3, false, false));
        addSymbol(new SlotSymbol(3, new Image("set_two/icecream48.png"), 2, false, false));
        addSymbol(new SlotSymbol(4, new Image("set_two/mustard48.png"), 2, false, false));
        addSymbol(new SlotSymbol(5, new Image("set_two/drink48.png"), 25, false, true));
        addSymbol(new SlotSymbol(6, new Image("set_two/coffee48.png"), 15, false, false));
        addSymbol(new SlotSymbol(7, new Image("set_two/water48.png"), 7, false, false));
        addSymbol(new SlotSymbol(8, new Image("set_two/pear48.png"), 1, false, false));
        addSymbol(new SlotSymbol(9, new Image("set_two/cherries48.png"), 15, true, false));
    }

    public static Slots getSlotsInstance() {
        return slotsInstance;
    }

    private void addSymbol(SlotSymbol symbol) {
        if (symbol != null) {
            symbolsList.add(symbol);
        }
    }

    public List<SlotSymbol> getSymbolsList() {
//        return Collections.unmodifiableList(symbolsList);
        return symbolsList;
    }

    public static class SlotSymbol {
        private final int index;
        private final Image image;
        private final int multiplier;
        private final boolean isFreeSpin;
        private final boolean isWild;

        public SlotSymbol(int index, Image image, int multiplier, boolean isFreeSpin, boolean isWild) {
            this.index = index;
            this.image = image;
            this.multiplier = multiplier;
            this.isFreeSpin = isFreeSpin;
            this.isWild = isWild;
        }

        public int getIndex() {
            return index;
        }

        public Image getImage() {
            return image;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public boolean isFreeSpin() {
            return isFreeSpin;
        }

        public boolean isWild() {
            return isWild;
        }
    }
}
