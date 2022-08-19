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
        // TODO: replace indexes with objects' equals() and see if it works
        double winnings = 0;
        int multi = checkForValidLine(reel0PayPos, reel1PayPos, reel2PayPos);

        if (multi != 0) {
            winnings = currentBet * multi;
        } else {
            winnings = 0;
        }

        return winnings;
    }

    public ResultsContainer processResults(List<SlotsData.SlotSymbol> reel0, List<SlotsData.SlotSymbol> reel1,
                                           List<SlotsData.SlotSymbol> reel2) {
        loadRecentResults(reel0, reel1, reel2); // external load

        reel0PayPos = recentResultsReel0.get(2);
        reel1PayPos = recentResultsReel1.get(2);
        reel2PayPos = recentResultsReel2.get(2);

        SlotsData.SlotSymbol emptySymbol = SlotsData.getSlotsDataInstance().getSymbolsList().get(1);
        SlotsData.SlotSymbol freeSymbol = SlotsData.getSlotsDataInstance().getSymbolsList().get(18);
        SlotsData.SlotSymbol wildSymbol = SlotsData.getSlotsDataInstance().getSymbolsList().get(10);

        // TODO: can't be done in here
        // check for free spins
//        isFreeSpin = reel0PayPos.equals(freeSymbol) && reel1PayPos.equals(freeSymbol) && reel2PayPos.equals(freeSymbol);

        // check for basic win:
        if (calculateWinnings() != 0) {
            int multi = checkForValidLine(reel0PayPos, reel1PayPos, reel2PayPos);
            return new ResultsContainer(calculateWinnings(), multi, 0, 0, 0);
        }

        // check for the reels to nudge:
        List<List<SlotsData.SlotSymbol>> reelsToNudge = new ArrayList<>();
        int nudgeCounter = 0;
        boolean isReel0ToNudge;
        boolean isReel1ToNudge;
        boolean isReel2ToNudge;

        if (compareTwoSymbols(reel0PayPos, emptySymbol)) {
            isReel0ToNudge = true;
            reelsToNudge.add(recentResultsReel0);
            nudgeCounter++;
        }
        if (compareTwoSymbols(reel1PayPos, emptySymbol)) {
            isReel1ToNudge = true;
            reelsToNudge.add(recentResultsReel1);
            nudgeCounter++;
        }
        if (compareTwoSymbols(reel2PayPos, emptySymbol)) {
            isReel2ToNudge = true;
            reelsToNudge.add(recentResultsReel2);
            nudgeCounter++;
        }

        SlotsData.SlotSymbol reel0PreWin = recentResultsReel0.get(1);
        SlotsData.SlotSymbol reel0PostWin = recentResultsReel0.get(3);
        SlotsData.SlotSymbol reel1PreWin = recentResultsReel1.get(1);
        SlotsData.SlotSymbol reel1PostWin = recentResultsReel1.get(3);
        SlotsData.SlotSymbol reel2PreWin = recentResultsReel2.get(1);
        SlotsData.SlotSymbol reel2PostWin = recentResultsReel2.get(3);
        System.out.println("nudge counter: " + nudgeCounter);
        System.out.println("is free spin: " + isFreeSpin);

        // find possible nudges:

        if (nudgeCounter == 3 && isFreeSpin) {
            // triple nudge; 3 empty symbols:
            double lastCheckedMultiplier = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 2 3
            // - - -
            // 4 5 6

            // 1 + 2 + 3
            int currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PreWin, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = 1;
                shiftReel2 = 1;
            }
            // 1 + 2 + 6
            currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PreWin, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = 1;
                shiftReel2 = -1;
            }
            // 1 + 5 + 3
            currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PostWin, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = -1;
                shiftReel2 = 1;
            }
            // 1 + 5 + 6
            currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PostWin, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = -1;
                shiftReel2 = -1;
            }
            // 4 + 2 + 3
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PreWin, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = 1;
                shiftReel2 = 1;
            }
            // 4 + 2 + 6
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PreWin, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = 1;
                shiftReel2 = -1;
            }
            // 4 + 5 + 3
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PostWin, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = -1;
                shiftReel2 = 1;
            }
            // 4 + 5 + 6
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PostWin, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = -1;
                shiftReel2 = -1;
            }

            double winnings = lastCheckedMultiplier * currentBet;
            return new ResultsContainer(winnings, lastCheckedMultiplier, shiftReel0, shiftReel1, shiftReel2);

        } else if (nudgeCounter == 2 && !compareTwoSymbols(reel1PayPos, emptySymbol)) {
            // double nudge; middle symbol non-empty:
            double lastCheckedMultiplier = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 - 3
            // - X -
            // 4 - 6

            // 1 + X + 3
            int currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PayPos, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = 0;
                shiftReel2 = 1;
            }
            // 1 + X + 6
            currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PayPos, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = 0;
                shiftReel2 = -1;
            }
            // 4 + X + 3
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PayPos, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = 0;
                shiftReel2 = 1;
            }
            // 4 + X + 6
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PayPos, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = 0;
                shiftReel2 = -1;
            }

            double winnings = lastCheckedMultiplier * currentBet;
            return new ResultsContainer(winnings, lastCheckedMultiplier, shiftReel0, shiftReel1, shiftReel2);

        } else if (nudgeCounter == 2 && !compareTwoSymbols(reel0PayPos, emptySymbol)) {
            // double nudge; left symbol non-empty:
            double lastCheckedMultiplier = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // - 2 3
            // X - -
            // - 5 6

            // X + 2 + 3
            int currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PreWin, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = 1;
                shiftReel2 = 1;
            }
            // X + 2 + 6
            currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PreWin, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = 1;
                shiftReel2 = -1;
            }
            // X + 5 + 3
            currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PostWin, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = -1;
                shiftReel2 = 1;
            }
            // X + 5 + 6
            currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PostWin, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = -1;
                shiftReel2 = -1;
            }

            double winnings = lastCheckedMultiplier * currentBet;
            return new ResultsContainer(winnings, lastCheckedMultiplier, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 2 && !compareTwoSymbols(reel2PayPos, emptySymbol)) {
            // double nudge; right symbol non-empty:
            double lastCheckedMultiplier = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 2 -
            // - - X
            // 4 5 -

            // 1 + 2 + X
            int currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PreWin, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = 1;
                shiftReel2 = 0;
            }
            // 1 + 5 + X
            currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PostWin, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = -1;
                shiftReel2 = 0;
            }
            // 4 + 2 + X
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PreWin, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = 1;
                shiftReel2 = 0;
            }
            // 4 + 5 + X
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PostWin, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = -1;
                shiftReel2 = 0;
            }

            double winnings = lastCheckedMultiplier * currentBet;
            return new ResultsContainer(winnings, lastCheckedMultiplier, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 1 && compareTwoSymbols(reel0PayPos, emptySymbol)) {
            // single nudge; left symbol empty:
            double lastCheckedMultiplier = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 - -
            // - X X
            // 4 - -

            // 1 + X + X
            int currentMultiplierCheck = checkForValidLine(reel0PreWin, reel1PayPos, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 1;
                shiftReel1 = 0;
                shiftReel2 = 0;
            }
            // 4 + X + X
            currentMultiplierCheck = checkForValidLine(reel0PostWin, reel1PayPos, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = -1;
                shiftReel1 = 0;
                shiftReel2 = 0;
            }

            double winnings = lastCheckedMultiplier * currentBet;
            return new ResultsContainer(winnings, lastCheckedMultiplier, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 1 && compareTwoSymbols(reel1PayPos, emptySymbol)) {
            // single nudge; middle symbol empty:
            double lastCheckedMultiplier = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // - 2 -
            // X - X
            // - 5 -

            // X + 2 + X
            int currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PreWin, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = 1;
                shiftReel2 = 0;
            }
            // X + 5 + X
            currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PostWin, reel2PayPos);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = -1;
                shiftReel2 = 0;
            }

            double winnings = lastCheckedMultiplier * currentBet;
            return new ResultsContainer(winnings, lastCheckedMultiplier, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 1 && compareTwoSymbols(reel2PayPos, emptySymbol)) {
            // single nudge; middle symbol empty:
            double lastCheckedMultiplier = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // - - 3
            // X X -
            // - - 6

            // X + X + 3
            int currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PayPos, reel2PreWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = 0;
                shiftReel2 = 1;
            }
            // X + X + 6
            currentMultiplierCheck = checkForValidLine(reel0PayPos, reel1PayPos, reel2PostWin);
            if (currentMultiplierCheck > lastCheckedMultiplier) {
                lastCheckedMultiplier = currentMultiplierCheck;
                shiftReel0 = 0;
                shiftReel1 = 0;
                shiftReel2 = -1;
            }

            double winnings = lastCheckedMultiplier * currentBet;
            return new ResultsContainer(winnings, lastCheckedMultiplier, shiftReel0, shiftReel1, shiftReel2);
        }
        return null;
    }

    // returns multiplier:
    public int checkForValidLine(SlotsData.SlotSymbol symbol0,
                                 SlotsData.SlotSymbol symbol1,
                                 SlotsData.SlotSymbol symbol2) {
        int multiplier = 0;
        if (symbol0 != null || symbol1 != null || symbol2 != null) {
            List<SlotsData.SlotSymbol> symbols = new ArrayList<>();
            Collections.addAll(symbols, symbol0, symbol1, symbol2);

            // standard 3-symbol combo (including 3x wild):
            if (compareTwoSymbols(symbol0, symbol1) && compareTwoSymbols(symbol0, symbol2)) {
                multiplier = symbol0.getMultiplier();
            }

            // TODO: inspect those warnings
            // 2x wild combo:
            if (symbol0.isWild() && symbol1.isWild()
                || symbol0.isWild() && symbol2.isWild()
                || symbol1.isWild() && symbol2.isWild()) {
                SlotsData.SlotSymbol nonWildSymbol;
                for (SlotsData.SlotSymbol symbol : symbols) {
                    if (!symbol.isWild()) {
                        nonWildSymbol = symbol;
                        multiplier = nonWildSymbol.getMultiplier();
                    }
                }
            }

            // combo with single wild:
            if (symbol0.isWild() || symbol1.isWild() || symbol2.isWild()) {
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
        for (int i = 0; i < 5; i++) {
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
