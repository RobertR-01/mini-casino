package com.minicasino.logic;

import com.minicasino.data.ProfileData;
import com.minicasino.data.SlotsData;
import javafx.scene.image.Image;

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

        // check for basic win
        if (calculateWinnings() != 0) {
            return new ResultsContainer(calculateWinnings(), 0, 0, 0);
        }

        // check for reels to nudge
        List<List<SlotsData.SlotSymbol>> reelsToNudge = new ArrayList<>();
        int nudgeCounter = 0;
        boolean isReel0ToNudge;
        boolean isReel1ToNudge;
        boolean isReel2ToNudge;

//        System.out.println("payline:");
//        System.out.println(reel0PayPos);
//        System.out.println(reel1PayPos);
//        System.out.println(reel2PayPos);
        System.out.println("results:");
        System.out.println(recentResultsReel0);
        System.out.println(recentResultsReel1);
        System.out.println(recentResultsReel2);

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
//        System.out.println("results");
//        System.out.println(reel0PreWin + " " + reel0PayPos + " " + reel0PostWin);
//        System.out.println(reel1PreWin + " " + reel1PayPos + " " + reel1PostWin);
//        System.out.println(reel2PreWin + " " + reel2PayPos + " " + reel2PostWin);

        // perform nudge
        if (nudgeCounter == 3 && isFreeSpin) {
            // triple nudge
            double currentWin = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 2 3
            // - - - empty
            // 4 5 6

            // 1 + 2 + 3
            int currentCheck = checkForValidLine(reel0PreWin, reel1PreWin, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = 1;
                shiftReel2 = 1;
            }
            // 1 + 2 + 6
            currentCheck = checkForValidLine(reel0PreWin, reel1PreWin, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = 1;
                shiftReel2 = -1;
            }
            // 1 + 5 + 3
            currentCheck = checkForValidLine(reel0PreWin, reel1PostWin, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = -1;
                shiftReel2 = 1;
            }
            // 1 + 5 + 6
            currentCheck = checkForValidLine(reel0PreWin, reel1PostWin, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = -1;
                shiftReel2 = -1;
            }
            // 4 + 2 + 3
            currentCheck = checkForValidLine(reel0PostWin, reel1PreWin, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = 1;
                shiftReel2 = 1;
            }
            // 4 + 2 + 6
            currentCheck = checkForValidLine(reel0PostWin, reel1PreWin, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = 1;
                shiftReel2 = -1;
            }
            // 4 + 5 + 3
            currentCheck = checkForValidLine(reel0PostWin, reel1PostWin, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = -1;
                shiftReel2 = 1;
            }
            // 4 + 5 + 6
            currentCheck = checkForValidLine(reel0PostWin, reel1PostWin, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = -1;
                shiftReel2 = -1;
            }

            return new ResultsContainer(currentWin, shiftReel0, shiftReel1, shiftReel2);

        } else if (nudgeCounter == 2 && !compareTwoSymbols(reel1PayPos, emptySymbol)) {
            // double nudge middle full
            double currentWin = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 - 3
            // - X -
            // 4 - 6

            // 1 + X + 3
            int currentCheck = checkForValidLine(reel0PreWin, reel1PayPos, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = 0;
                shiftReel2 = 1;
            }
            // 1 + X + 6
            currentCheck = checkForValidLine(reel0PreWin, reel1PayPos, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = 0;
                shiftReel2 = -1;
            }
            // 4 + X + 3
            currentCheck = checkForValidLine(reel0PostWin, reel1PayPos, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = 0;
                shiftReel2 = 1;
            }
            // 4 + X + 6
            currentCheck = checkForValidLine(reel0PostWin, reel1PayPos, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = 0;
                shiftReel2 = -1;
            }

            return new ResultsContainer(currentWin, shiftReel0, shiftReel1, shiftReel2);

        } else if (nudgeCounter == 2 && !compareTwoSymbols(reel0PayPos, emptySymbol)) {
            // double nudge left full
            double currentWin = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // - 2 3
            // X - -
            // - 5 6

            // X + 2 + 3
            int currentCheck = checkForValidLine(reel0PayPos, reel1PreWin, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = 1;
                shiftReel2 = 1;
            }
            // X + 2 + 6
            currentCheck = checkForValidLine(reel0PayPos, reel1PreWin, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = 1;
                shiftReel2 = -1;
            }
            // X + 5 + 3
            currentCheck = checkForValidLine(reel0PayPos, reel1PostWin, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = -1;
                shiftReel2 = 1;
            }
            // X + 5 + 6
            currentCheck = checkForValidLine(reel0PayPos, reel1PostWin, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = -1;
                shiftReel2 = -1;
            }

            return new ResultsContainer(currentWin, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 2 && !compareTwoSymbols(reel2PayPos, emptySymbol)) {
            // double nudge right full
            double currentWin = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 2 -
            // - - X
            // 4 5 -

            // 1 + 2 + X
            int currentCheck = checkForValidLine(reel0PreWin, reel1PreWin, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = 1;
                shiftReel2 = 0;
            }
            // 1 + 5 + X
            currentCheck = checkForValidLine(reel0PreWin, reel1PostWin, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = -1;
                shiftReel2 = 0;
            }
            // 4 + 2 + X
            currentCheck = checkForValidLine(reel0PostWin, reel1PreWin, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = 1;
                shiftReel2 = 0;
            }
            // 4 + 5 + X
            currentCheck = checkForValidLine(reel0PostWin, reel1PostWin, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = -1;
                shiftReel2 = 0;
            }

            return new ResultsContainer(currentWin, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 1 && compareTwoSymbols(reel0PayPos, emptySymbol)) {
            // single nudge left empty
            double currentWin = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // 1 - -
            // - X X
            // 4 - -

            // 1 + X + X
            int currentCheck = checkForValidLine(reel0PreWin, reel1PayPos, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 1;
                shiftReel1 = 0;
                shiftReel2 = 0;
            }
            // 4 + X + X
            currentCheck = checkForValidLine(reel0PostWin, reel1PayPos, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = -1;
                shiftReel1 = 0;
                shiftReel2 = 0;
            }

            return new ResultsContainer(currentWin, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 1 && compareTwoSymbols(reel1PayPos, emptySymbol)) {
            // single nudge middle empty
            double currentWin = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // - 2 -
            // X - X
            // - 5 -

            // X + 2 + X
            int currentCheck = checkForValidLine(reel0PayPos, reel1PreWin, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = 1;
                shiftReel2 = 0;
            }
            // X + 5 + X
            currentCheck = checkForValidLine(reel0PayPos, reel1PostWin, reel2PayPos);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = -1;
                shiftReel2 = 0;
            }

            return new ResultsContainer(currentWin, shiftReel0, shiftReel1, shiftReel2);
        } else if (nudgeCounter == 1 && compareTwoSymbols(reel2PayPos, emptySymbol)) {
            // single nudge middle empty
            double currentWin = 0;
            int shiftReel0 = 0;
            int shiftReel1 = 0;
            int shiftReel2 = 0;

            // - - 3
            // X X -
            // - - 6

            // X + X + 3
            int currentCheck = checkForValidLine(reel0PayPos, reel1PayPos, reel2PreWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = 0;
                shiftReel2 = 1;
            }
            // X + X + 6
            currentCheck = checkForValidLine(reel0PayPos, reel1PayPos, reel2PostWin);
            if (currentCheck > currentWin) {
                currentWin = currentCheck;
                shiftReel0 = 0;
                shiftReel1 = 0;
                shiftReel2 = -1;
            }

            return new ResultsContainer(currentWin, shiftReel0, shiftReel1, shiftReel2);
        }
        return null;
    }

    // returns multiplier
    public int checkForValidLine(SlotsData.SlotSymbol symbol0,
                                 SlotsData.SlotSymbol symbol1,
                                 SlotsData.SlotSymbol symbol2) {
        if (symbol0 == null || symbol1 == null || symbol2 == null) {
            System.out.println("SlotsLogic.checkForValidLine -> null symbol");
            return 0; // TODO multiple exit points
        }

        List<SlotsData.SlotSymbol> symbols = new ArrayList<>();
        Collections.addAll(symbols, symbol0, symbol1, symbol2);

        // standard combo (including 3x wild)
        if (compareTwoSymbols(symbol0, symbol1) && compareTwoSymbols(symbol0, symbol2)) {
            return symbol0.getMultiplier();
        }

        // 2x wild combo
        if (symbol0.isWild() && symbol1.isWild()
            || symbol0.isWild() && symbol2.isWild()
            || symbol1.isWild() && symbol2.isWild()) {
            SlotsData.SlotSymbol nonWildSymbol;
            for (SlotsData.SlotSymbol symbol : symbols) {
                if (!symbol.isWild()) {
                    nonWildSymbol = symbol;
                    return nonWildSymbol.getMultiplier(); // TODO: multiple exit points
                }
            }
        }

        // combo with 1 wild
        if (symbol0.isWild() || symbol1.isWild() || symbol2.isWild()) {
            List<SlotsData.SlotSymbol> nonWildSymbols = new ArrayList<>();
            for (SlotsData.SlotSymbol symbol : symbols) {
                if (!symbol.isWild()) {
                    nonWildSymbols.add(symbol);
                }
            }
            // checking the 2 non-wild symbols
            if (compareTwoSymbols(nonWildSymbols.get(0), nonWildSymbols.get(1))) {
                return nonWildSymbols.get(0).getMultiplier();
            }
        }

        return 0;
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
//        System.out.println("------------load params------------");
//        System.out.println(reel0Results);
//        System.out.println(reel1Results);
//        System.out.println(reel2Results);
//        System.out.println("------------load params------------");
        // TODO: add validation?
        for (int i = 0; i < 5; i++) {
            recentResultsReel0.add(reel0Results.get(i));
            recentResultsReel1.add(reel1Results.get(i));
            recentResultsReel2.add(reel2Results.get(i));
        }
        Collections.addAll(listOfCurrentReels, recentResultsReel0, recentResultsReel1, recentResultsReel2);
//        System.out.println("------------load results------------");
//        System.out.println(recentResultsReel0);
//        System.out.println(recentResultsReel1);
//        System.out.println(recentResultsReel2);
//        System.out.println("------------load results------------");
    }

    // implement process results method()
    // check for free spins active - if yes don't take bet amount and mark for triple nudge (possibility)
    // if not take bet amount -> check for win - if win return winnings
    // if not win -> check for reels to nudge - if none return 0 winnings
    // if there are any reels to nudge confront the triple nudge/free spins condition
    // and decide what nudge type to perform
    // process nudge
    // calculate winnings based on the new results


    // TODO: return a container class Nudge
    public void lookForNudge() {
        // TODO: add check for the free spins present (free spins allows 3 reel nudge)
        // TODO: validation - is it required? is it good enough?

        SlotsData.SlotSymbol emptySymbol = SlotsData.getSlotsDataInstance().getSymbolsList().get(1);
        if ((recentResultsReel0.size() != 0) && (recentResultsReel1.size() != 0) && (recentResultsReel2.size() != 0)) {
            // check for empty lists
            return;
        }

        // new approach !!!! - first find the reels to nudge (lists)
        // then see how many are there -> decide what type of nudge it requires: single, double, triple
        // implement nudges (or better: one mechanism reused for all cases)

        List<List<SlotsData.SlotSymbol>> listOfReels = new ArrayList<>();
        Collections.addAll(listOfReels, recentResultsReel0, recentResultsReel1, recentResultsReel2);
        List<List<SlotsData.SlotSymbol>> reelsToNudge = new ArrayList<>();
        for (List<SlotsData.SlotSymbol> reel : listOfReels) {
            if (compareTwoSymbols(reel.get(2), emptySymbol) && (reelsToNudge.size() < 3)) {
                reelsToNudge.add(reel);
            }
        }

        //--------------

        List<SlotsData.SlotSymbol> reelToNudge0 = new ArrayList<>();
        List<SlotsData.SlotSymbol> reelToNudge1 = new ArrayList<>();
        List<SlotsData.SlotSymbol> reelToNudge2 = new ArrayList<>();


        SlotsData.SlotSymbol reel0pos2 = recentResultsReel0.get(2);
        SlotsData.SlotSymbol reel1pos2 = recentResultsReel1.get(2);
        SlotsData.SlotSymbol reel2pos2 = recentResultsReel2.get(2);

        if (calculateWinnings() == 0) {
            // there's already a winning combo
            return;
        }

        // double nudge check:
        // - max 1 reel not empty; at least 2 empty and 1 not empty
        if ((!compareTwoSymbols(reel0pos2, emptySymbol) && !compareTwoSymbols(reel1pos2, emptySymbol) && compareTwoSymbols(reel2pos2, emptySymbol))
            || (compareTwoSymbols(reel0pos2, emptySymbol) && !compareTwoSymbols(reel1pos2, emptySymbol) && !compareTwoSymbols(reel2pos2, emptySymbol))
            || (!compareTwoSymbols(reel0pos2, emptySymbol) && compareTwoSymbols(reel1pos2, emptySymbol) && !compareTwoSymbols(reel2pos2, emptySymbol))) {
            // single reel nudge

            // R0 R1 R2
            // XX XX XX
            // P0 P0 P0 <- check for possible nudge
            // P1 P1 P1 <- pay-line
            // P2 P2 P2 <- check for possible nudge
            // XX XX XX


            return;
        }

        // TODO: possibly merge with the above as 'else'
        // single reel check:
        // - at least 2 reels not empty and 1 empty;  max 1 empty
        if ((!compareTwoSymbols(reel0pos2, emptySymbol) && compareTwoSymbols(reel1pos2, emptySymbol) && compareTwoSymbols(reel2pos2, emptySymbol))
            || (compareTwoSymbols(reel0pos2, emptySymbol) && !compareTwoSymbols(reel1pos2, emptySymbol) && compareTwoSymbols(reel2pos2, emptySymbol))
            || (compareTwoSymbols(reel0pos2, emptySymbol)) && compareTwoSymbols(reel1pos2, emptySymbol) && !compareTwoSymbols(reel2pos2, emptySymbol)) {
            // double reel nudge

            return;
        }
    }

    private boolean compareTwoSymbols(SlotsData.SlotSymbol symbol1, SlotsData.SlotSymbol symbol2) {
        if (symbol1 != null && symbol2 != null) {
            String image1URL = symbol1.getImage().getUrl();
            String image2URL = symbol2.getImage().getUrl();
            if (image1URL.equals(image2URL)) {
                return true;
            }
        }

        return false;
    }

    public double getCurrentBet() {
        return currentBet;
    }

    public static class ResultsContainer {
        private final double winnings;
        private final int shiftReel0;
        private final int shiftReel1;
        private final int shiftReel2;

        private ResultsContainer(double winnings, int shiftReel0, int shiftReel1, int shiftReel2) {
            this.winnings = winnings;
            this.shiftReel0 = shiftReel0;
            this.shiftReel1 = shiftReel1;
            this.shiftReel2 = shiftReel2;
        }

        public double getWinnings() {
            return winnings;
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
