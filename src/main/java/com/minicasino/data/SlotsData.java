package com.minicasino.data;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class SlotsData {
    private static final SlotsData SLOTS_DATA_INSTANCE = new SlotsData();
    private List<SlotSymbol> symbolsList;

    private SlotsData() {
        symbolsList = new ArrayList<>();
        addSymbol(new SlotSymbol(0, new Image("set_two/meat48.png"), 4, false, false, false));
        addSymbol(new SlotSymbol(1));

        addSymbol(new SlotSymbol(2, new Image("set_two/hotdog48.png"), 3, false, false, false));
        addSymbol(new SlotSymbol(3));

        addSymbol(new SlotSymbol(4, new Image("set_two/cheese48.png"), 3, false, false, false));
        addSymbol(new SlotSymbol(5));

        addSymbol(new SlotSymbol(6, new Image("set_two/icecream48.png"), 2, false, false, false));
        addSymbol(new SlotSymbol(7));

        addSymbol(new SlotSymbol(8, new Image("set_two/mustard48.png"), 2, false, false, false));
        addSymbol(new SlotSymbol(9));

        addSymbol(new SlotSymbol(10, new Image("set_two/drink48.png"), 25, false, true, false));
        addSymbol(new SlotSymbol(11));

        addSymbol(new SlotSymbol(12, new Image("set_two/coffee48.png"), 15, false, false, false));
        addSymbol(new SlotSymbol(13));

        addSymbol(new SlotSymbol(14, new Image("set_two/water48.png"), 7, false, false, false));
        addSymbol(new SlotSymbol(15));

        addSymbol(new SlotSymbol(16, new Image("set_two/pear48.png"), 1, false, false, false));
        addSymbol(new SlotSymbol(17));

        addSymbol(new SlotSymbol(18, new Image("set_two/cherries48.png"), 15, true, false, false));
        addSymbol(new SlotSymbol(19));
    }

    public static SlotsData getSlotsInstance() {
        return SLOTS_DATA_INSTANCE;
    }

    private void addSymbol(SlotSymbol symbol) {
        if (symbol != null) {
            symbolsList.add(symbol);
        }
    }

    public List<SlotSymbol> getSymbolsList() {
//        return Collections.unmodifiableList(symbolsList);
        return new ArrayList<>(symbolsList);
    }

    public static class SlotSymbol {
        private final int index;
        private final Image image;
        private final int multiplier;
        private final boolean isFreeSpin;
        private final boolean isWild;
        private final boolean isEmpty;

        public SlotSymbol(int index, Image image, int multiplier, boolean isFreeSpin, boolean isWild, boolean isEmpty) {
            this.index = index;
            this.image = image;
            this.multiplier = multiplier;
            this.isFreeSpin = isFreeSpin;
            this.isWild = isWild;
            this.isEmpty = isEmpty;
        }

        public SlotSymbol(int index) {
            this(index, new Image("set_two/empty48.png"), 0, false, false, true);
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
                   "index = " + index +
                   ", image = " + image.getUrl() +
                   "}\n";
        }
    }
}
