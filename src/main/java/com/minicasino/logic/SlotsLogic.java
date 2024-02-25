package com.minicasino.logic;

import com.minicasino.data.ProfileData;
import com.minicasino.data.SlotsData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotsLogic {
    private List<SlotsData.SlotSymbol> recentResultsReel0;
    private List<SlotsData.SlotSymbol> recentResultsReel1;
    private List<SlotsData.SlotSymbol> recentResultsReel2;
    private List<SlotsData.SlotSymbol> recentResultsAll;
    private List<List<SlotsData.SlotSymbol>> listOfCurrentReels;
    private SlotsData.SlotSymbol reel0PayPos;
    private SlotsData.SlotSymbol reel1PayPos;
    private SlotsData.SlotSymbol reel2PayPos;
    private double currentBet;
    private ProfileData.Profile activeProfile;
    private boolean isFreeSpin;

    public SlotsLogic(double currentBet) {
        this.recentResultsReel0 = new ArrayList<>();
        this.recentResultsReel1 = new ArrayList<>();
        this.recentResultsReel2 = new ArrayList<>();
        this.recentResultsAll = new ArrayList<>();
        this.listOfCurrentReels = new ArrayList<>();
        this.currentBet = currentBet;
        this.activeProfile = ProfileData.getProfileDataInstance().getActiveProfile();
    }

    /**
     * This method determines whether the last rolled pay-line is a winning one, and calculates the winnings. The only
     * available pay-line is the combination of elements of the SlotSymbols list with indexes: 2, 7 and 12.
     *
     * @return The winnings amount. Returns 0 if the recently rolled line is not a valid winning combo.
     */
    public double calculateWinnings() {
        // TODO: add index parameters to the signature instead of hardcoding them in the body
        double winnings = 0;
        int multi = getPayLineMultiplier(reel0PayPos, reel1PayPos, reel2PayPos);

        if (multi != 0) {
            winnings = currentBet * multi;
        } else {
            winnings = 0;
        }

        return winnings;
    }

    public ResultsContainer processResults(List<SlotsData.SlotSymbol> reel0, List<SlotsData.SlotSymbol> reel1,
                                           List<SlotsData.SlotSymbol> reel2) {
        // loads 3 symbols per reel (the middle ones, not greyed-out):
        loadRecentResults(reel0, reel1, reel2);

        List<List<SlotsData.SlotSymbol>> allReelsResults = new ArrayList<>();
        Collections.addAll(allReelsResults, recentResultsReel0, recentResultsReel1, recentResultsReel2);

        SlotsData.SlotSymbol emptySymbol = SlotsData.getSlotsDataInstance().getEmptySymbol();
        // TODO: write actual methods in SlotsData to replace that bs (see above):
//        SlotsData.SlotSymbol freeSymbol = SlotsData.getSlotsDataInstance().getSymbolsList().get(18);
//        SlotsData.SlotSymbol wildSymbol = SlotsData.getSlotsDataInstance().getSymbolsList().get(10);

        // TODO: write free spin check
        // check for free spins
//        isFreeSpin = reel0PayPos.equals(freeSymbol) && reel1PayPos.equals(freeSymbol) && reel2PayPos.equals(freeSymbol);

        // check for basic win:
        if (calculateWinnings() != 0) {
            int multi = getPayLineMultiplier(recentResultsReel0.get(1),
                                             recentResultsReel1.get(1),
                                             recentResultsReel2.get(1));
            return new ResultsContainer(calculateWinnings(), multi, 0, 0, 0);
        }

        // check for the number of possible reels to nudge:
        int nudgeCounter = 0;
        for (List<SlotsData.SlotSymbol> reelResults : allReelsResults) {
            nudgeCounter = (compareTwoSymbols(reelResults.get(1), emptySymbol)) ? nudgeCounter + 1 : nudgeCounter;
        }

        int highestMultiplier = 0;
        int reel0ShiftDistance = 0;
        int reel1ShiftDistance = 0;
        int reel2ShiftDistance = 0;
        List<List<SlotsData.SlotSymbol>> possibleWinningCombinations = new ArrayList<>();
        // looks for any nudge:
        for (SlotsData.SlotSymbol reel0Symbol : recentResultsReel0) {
            for (SlotsData.SlotSymbol reel1Symbol : recentResultsReel1) {
                for (SlotsData.SlotSymbol reel2Symbol : recentResultsReel2) {
                    int currentMultiplier = getPayLineMultiplier(reel0Symbol, reel1Symbol, reel2Symbol);
                    List<SlotsData.SlotSymbol> currentWinningCombination;
                    currentWinningCombination = new ArrayList<>();
                    Collections.addAll(currentWinningCombination, reel0Symbol, reel1Symbol, reel2Symbol);
                    if (currentMultiplier > highestMultiplier) {
                        // "resets" the list (higher multiplier found):
                        possibleWinningCombinations = new ArrayList<>();
                        possibleWinningCombinations.add(currentWinningCombination);
                        highestMultiplier = currentMultiplier;
                    } else if (currentMultiplier == highestMultiplier) {
                        // adds this combination to the existing list (same multiplier found):
                        possibleWinningCombinations.add(currentWinningCombination);
                    }
                }
            }
        }

        if (possibleWinningCombinations.size() != 0) {
            List<SlotsData.SlotSymbol> bestCombination = possibleWinningCombinations.get(0);
            for (List<SlotsData.SlotSymbol> combination : possibleWinningCombinations) {
                // looks for
                SlotsData.SlotSymbol symbol0 = combination.get(0);
                SlotsData.SlotSymbol symbol1 = combination.get(1);
                SlotsData.SlotSymbol symbol2 = combination.get(2);
                if (compareTwoSymbols(symbol0, symbol1) && compareTwoSymbols(symbol1, symbol2)) {
                    bestCombination = combination;
                }
            }

            // determines the distance each reel must be shifted to set the chosen symbol on the pay-line:
            // (0 if the symbol is already in the middle -> no list rotation needed)
            reel0ShiftDistance = findShiftDistance(recentResultsReel0, bestCombination, 0);
            reel1ShiftDistance = findShiftDistance(recentResultsReel1, bestCombination, 1);
            reel2ShiftDistance = findShiftDistance(recentResultsReel2, bestCombination, 2);
        }

        // TODO: implement free spin condition (triple nudge)
        return createResult(highestMultiplier, reel0ShiftDistance, reel1ShiftDistance, reel2ShiftDistance);
    }

    private int findShiftDistance(List<SlotsData.SlotSymbol> reelResults,
                                  List<SlotsData.SlotSymbol> checkedCombination, int checkedCombinationSymbolIndex) {
        int symbolListShiftDistance;
        if (reelResults.indexOf(checkedCombination.get(checkedCombinationSymbolIndex)) == 0) {
            symbolListShiftDistance = 1;
        } else if (reelResults.indexOf(checkedCombination.get(checkedCombinationSymbolIndex)) == 1) {
            symbolListShiftDistance = 0;
        } else {
            symbolListShiftDistance = -1;
        }

        return symbolListShiftDistance;
    }

    private ResultsContainer createResult(int multiplier, int reel0ShiftDistance, int reel1ShiftDistance,
                                          int reel2ShiftDistance) {
        double winnings = multiplier * currentBet;
        return new ResultsContainer(winnings, multiplier, reel0ShiftDistance, reel1ShiftDistance, reel2ShiftDistance);
    }

    public int getPayLineMultiplier(SlotsData.SlotSymbol symbol0,
                                    SlotsData.SlotSymbol symbol1,
                                    SlotsData.SlotSymbol symbol2) {
        int multiplier = 0;
        if (symbol0 != null || symbol1 != null || symbol2 != null) {
            List<SlotsData.SlotSymbol> symbols = new ArrayList<>();
            Collections.addAll(symbols, symbol0, symbol1, symbol2);

            if (compareTwoSymbols(symbol0, symbol1) && compareTwoSymbols(symbol0, symbol2)) {
                // standard 3-symbol combo (including 3x wild):
                multiplier = symbol0.getMultiplier();
            } else if (symbol0.isWild() && symbol1.isWild()
                       || symbol0.isWild() && symbol2.isWild()
                       || symbol1.isWild() && symbol2.isWild()) {
                // TODO: inspect those warnings
                // 2x wild combo:
                SlotsData.SlotSymbol nonWildSymbol;
                for (SlotsData.SlotSymbol symbol : symbols) {
                    if (!symbol.isWild()) {
                        nonWildSymbol = symbol;
                        multiplier = nonWildSymbol.getMultiplier();
                    }
                }
            } else if (symbol0.isWild() || symbol1.isWild() || symbol2.isWild()) {
                // combo with single wild:
                List<SlotsData.SlotSymbol> nonWildSymbols = new ArrayList<>();
                for (SlotsData.SlotSymbol symbol : symbols) {
                    if (!symbol.isWild()) {
                        nonWildSymbols.add(symbol);
                    }
                }
                // checking the 2 non-wild symbols
                if (compareTwoSymbols(nonWildSymbols.get(0), nonWildSymbols.get(1))) {
                    multiplier = nonWildSymbols.get(0).getMultiplier();
                }
            }
        }

        return multiplier;
    }

    /**
     * This method loads the current set of symbols from all 3 reels to the recentResultsList. Only the first 6 visible
     * elements from each reel list are loaded.
     * // TODO: update these docs (results split into 3 lists)
     *
     * @param reel0Results First reel symbols list.
     * @param reel1Results Second reel symbols list.
     * @param reel2Results Third reel symbols list.
     */
    public void loadRecentResults(List<SlotsData.SlotSymbol> reel0Results, List<SlotsData.SlotSymbol> reel1Results,
                                  List<SlotsData.SlotSymbol> reel2Results) {
        // TODO: add validation?
        for (int i = 1; i < 4; i++) {
            recentResultsReel0.add(reel0Results.get(i));
            recentResultsReel1.add(reel1Results.get(i));
            recentResultsReel2.add(reel2Results.get(i));
        }
        Collections.addAll(listOfCurrentReels, recentResultsReel0, recentResultsReel1, recentResultsReel2);
    }

    private boolean compareTwoSymbols(SlotsData.SlotSymbol symbol1, SlotsData.SlotSymbol symbol2) {
        if (symbol1 != null && symbol2 != null) {
            String image1URL = symbol1.getImage().getUrl();
            String image2URL = symbol2.getImage().getUrl();
            return image1URL.equals(image2URL);
        }

        return false;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public static class ResultsContainer {
        private final double winnings;
        private final double multiplier;
        private final int shiftReel0;
        private final int shiftReel1;
        private final int shiftReel2;

        private ResultsContainer(double winnings, double multiplier, int shiftReel0, int shiftReel1, int shiftReel2) {
            this.winnings = winnings;
            this.multiplier = multiplier;
            this.shiftReel0 = shiftReel0;
            this.shiftReel1 = shiftReel1;
            this.shiftReel2 = shiftReel2;
        }

        public double getWinnings() {
            return winnings;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public int getShiftReel0() {
            return shiftReel0;
        }

        public int getShiftReel1() {
            return shiftReel1;
        }

        public int getShiftReel2() {
            return shiftReel2;
        }
    }
}
