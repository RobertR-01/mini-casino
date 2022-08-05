package com.game.logic;

import com.game.data.SlotsData;

import java.util.ArrayList;
import java.util.List;

public class SlotsLogic {
    private List<SlotsData.SlotSymbol> recentResultsList;
    private double currentBet;

    public SlotsLogic(double currentBet) {
        this.recentResultsList = new ArrayList<>();
        this.currentBet = currentBet;
    }

    /**
     * This method determines whether the last rolled pay-line is a winning one, and calculates the winnings. The only
     * available pay-line is the combination of elements of the SlotSymbols list with indexes: 2, 7 and 12.
     *
     * @return The winnings amount. Returns 0 if the recently rolled line is not a valid winning combo.
     */
    public double calculateWinnings() {
        double winnings = 0;
        System.out.println(recentResultsList.get(2).getIndex());
        System.out.println(recentResultsList.get(7).getIndex());
        System.out.println(recentResultsList.get(12).getIndex());
        System.out.println("===");
        if (recentResultsList.get(2).getIndex() == recentResultsList.get(7).getIndex()
            && recentResultsList.get(7).getIndex() == recentResultsList.get(12).getIndex()) {
            int multiplier = recentResultsList.get(2).getMultiplier();
            winnings = currentBet * multiplier;
        } else {
            winnings = 0;
        }
        return winnings;
    }

    /**
     * This method loads the current set of symbols from all 3 reels to the recentResultsList. Only the first 6 visible
     * elements from each reel list are loaded.
     *
     * @param reel0Results First reel symbols list.
     * @param reel1Results Second reel symbols list.
     * @param reel2Results Third reel symbols list.
     */
    public void setRecentResultsList(List<SlotsData.SlotSymbol> reel0Results, List<SlotsData.SlotSymbol> reel1Results,
                                     List<SlotsData.SlotSymbol> reel2Results) {
        for (int i = 0; i < 5; i++) {
            recentResultsList.add(reel0Results.get(i));
        }
        for (int i = 0; i < 5; i++) {
            recentResultsList.add(reel1Results.get(i));
        }
        for (int i = 0; i < 5; i++) {
            recentResultsList.add(reel2Results.get(i));
        }
    }

    public List<SlotsData.SlotSymbol> getRecentResultsList() {
        return recentResultsList;
    }
}
