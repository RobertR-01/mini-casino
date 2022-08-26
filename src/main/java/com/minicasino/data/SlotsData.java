package com.minicasino.data;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class SlotsData {
    private static final SlotsData SLOTS_DATA_INSTANCE = new SlotsData();
    private final List<SlotSymbol> symbolsList;
    private final SlotSymbol emptySymbol;

    private SlotsData() {
        emptySymbol = new SlotSymbol();
        symbolsList = new ArrayList<>();
        // the first half of all symbols:
//        addSymbol(new SlotSymbol(new Image("slots/symbols/meat48.png"), 4, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/hotdog48.png"), 3, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/cheese48.png"), 3, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/icecream48.png"), 2, false, false, false));
//        addSymbol(new SlotSymbol());
//
//        addSymbol(new SlotSymbol(new Image("slots/symbols/mustard48.png"), 2, false, false, false));
//        addSymbol(new SlotSymbol());

        // with the second half only:
        addSymbol(new SlotSymbol(new Image("slots/symbols/drink48.png"), 25, false, true, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/coffee48.png"), 15, false, false, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/water48.png"), 7, false, false, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/pear48.png"), 1, false, false, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/cherries48.png"), 15, true, false, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/drink48.png"), 25, false, true, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/coffee48.png"), 15, false, false, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/water48.png"), 7, false, false, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/pear48.png"), 1, false, false, false));
        addSymbol(new SlotSymbol());

        addSymbol(new SlotSymbol(new Image("slots/symbols/cherries48.png"), 15, true, false, false));
        addSymbol(new SlotSymbol());

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

    private void addSymbol(SlotSymbol symbol) {
        if (symbol != null) {
            symbolsList.add(symbol);
        }
    }

    public SlotSymbol getEmptySymbol() {
        return emptySymbol;
    }

    public List<SlotSymbol> getSymbolsList() {
//        return Collections.unmodifiableList(symbolsList);
        return new ArrayList<>(symbolsList);
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
