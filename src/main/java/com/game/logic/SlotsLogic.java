package com.game.logic;

import com.game.data.SlotsData;

import java.util.ArrayList;
import java.util.List;

public class SlotsLogic {
    private List<SlotsData.SlotSymbol> resultsList;
    private double currentBet;

    public SlotsLogic(List<SlotsData.SlotSymbol> resultsList, double currentBet) {
        this.resultsList = new ArrayList<>(resultsList);
        this.currentBet = currentBet;
    }

    /**
     * This method determines whether the last rolled pay-line is a winning one, and calculates the winnings. The only
     * available pay-line is the combination of elements of the SlotSymbols list with indexes: 2, 7 and 12.
     *
     * @return The winnings amount. Returns 0 if the recently rolled line is not a valid winning combo.
     */
    private double checkForStandardWin() {
        double winnings = 0;
        if (resultsList.get(2).getIndex() == resultsList.get(7).getIndex()
            && resultsList.get(7).getIndex() == resultsList.get(12).getIndex()) {
            int multiplier = resultsList.get(2).getMultiplier();
            winnings = currentBet * multiplier;
        } else {
            winnings = 0;
        }
        return winnings;
    }
}
