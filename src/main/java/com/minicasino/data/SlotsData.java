package com.minicasino.data;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class SlotsData {
    private static final SlotsData SLOTS_DATA_INSTANCE = new SlotsData();
    private final List<SlotSymbol> symbolsList;
    private final SlotSymbol emptySymbol;
    private final List<SlotSymbol> leftSideSymbols;
    private final List<SlotSymbol> rightSideSymbols;

    private SlotsData() {
        emptySymbol = new SlotSymbol();
        symbolsList = new ArrayList<>();
        leftSideSymbols = new ArrayList<>();
        rightSideSymbols = new ArrayList<>();

        // left side symbols:
        addSymbol(leftSideSymbols, new SlotSymbol(new Image("slots/symbols/meat48.png"), 10, false, false, false));
        addSymbol(leftSideSymbols, new SlotSymbol());

        addSymbol(leftSideSymbols, new SlotSymbol(new Image("slots/symbols/hotdog48.png"), 5, false, false, false));
        addSymbol(leftSideSymbols, new SlotSymbol());

        addSymbol(leftSideSymbols, new SlotSymbol(new Image("slots/symbols/cheese48.png"), 3, false, false, false));
        addSymbol(leftSideSymbols, new SlotSymbol());

        addSymbol(leftSideSymbols, new SlotSymbol(new Image("slots/symbols/icecream48.png"), 2, false, false, false));
        addSymbol(leftSideSymbols, new SlotSymbol());

        addSymbol(leftSideSymbols, new SlotSymbol(new Image("slots/symbols/mustard48.png"), 1, false, false, false));
        addSymbol(leftSideSymbols, new SlotSymbol());

        // right side symbols:
        addSymbol(rightSideSymbols, new SlotSymbol(new Image("slots/symbols/drink48.png"), 75, false, true, false));
        addSymbol(rightSideSymbols, new SlotSymbol());

        addSymbol(rightSideSymbols, new SlotSymbol(new Image("slots/symbols/coffee48.png"), 50, false, false, false));
        addSymbol(rightSideSymbols, new SlotSymbol());

        addSymbol(rightSideSymbols, new SlotSymbol(new Image("slots/symbols/water48.png"), 25, false, false, false));
        addSymbol(rightSideSymbols, new SlotSymbol());

        addSymbol(rightSideSymbols, new SlotSymbol(new Image("slots/symbols/pear48.png"), 20, false, false, false));
        addSymbol(rightSideSymbols, new SlotSymbol());

        addSymbol(rightSideSymbols, new SlotSymbol(new Image("slots/symbols/cherries48.png"), 15, true, false, false));
        addSymbol(rightSideSymbols, new SlotSymbol());

        symbolsList.addAll(leftSideSymbols);
        symbolsList.addAll(rightSideSymbols);

        // the list for testing:
//        addSymbol(new SlotSymbol(new Image("slots/symbols/drink48.png"), 25, false, true, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/coffee48.png"), 15, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/drink48.png"), 25, false, true, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/coffee48.png"), 15, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/drink48.png"), 25, false, true, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/coffee48.png"), 15, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/drink48.png"), 25, false, true, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/coffee48.png"), 15, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/drink48.png"), 25, false, true, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/coffee48.png"), 15, false, false, false));
//        addSymbol(new SlotSymbol());
    }

    public static SlotsData getSlotsDataInstance() {
        return SLOTS_DATA_INSTANCE;
    }

    private void addSymbol(List<SlotSymbol> list, SlotSymbol symbol) {
        if (symbol != null && list != null) {
            list.add(symbol);
        }
    }

    public SlotSymbol getEmptySymbol() {
        return emptySymbol;
    }

    public List<SlotSymbol> getSymbolsList() {
//        return Collections.unmodifiableList(symbolsList);
        return new ArrayList<>(symbolsList);
    }

    public List<SlotSymbol> getLeftSideSymbols() {
        return new ArrayList<>(leftSideSymbols);
    }

    public List<SlotSymbol> getRightSideSymbols() {
        return new ArrayList<>(rightSideSymbols);
    }

    public static class SlotSymbol {
        private final Image image;
        private final int multiplier;
        private final boolean isFreeSpin;
        private final boolean isWild;
        private final boolean isEmpty;

        private SlotSymbol(Image image, int multiplier, boolean isFreeSpin, boolean isWild, boolean isEmpty) {
            this.image = image;
            this.multiplier = multiplier;
            this.isFreeSpin = isFreeSpin;
            this.isWild = isWild;
            this.isEmpty = isEmpty;
        }

        private SlotSymbol() {
            this(new Image("slots/symbols/empty48.png"), 0, false, false, true);
        }

        // should return copy?
        public Image getImage() {
            return image;
        }

        public int getMultiplier() {
            return multiplier;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public boolean isFreeSpin() {
            return isFreeSpin;
        }

        public boolean isWild() {
            return isWild;
        }

        @Override
        public String toString() {
            return "SlotSymbol {" +
                   ", image = " + image.getUrl() +
                   "}\n";
        }
    }
}
