package com.minicasino.data;

public class ProfileData {
    private String name;
    private double balance;
    private double highestWin;

    public ProfileData(String name) {
        this.name = name;
        this.balance = 5000.0;
        this.highestWin = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void increaseBalance(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("ProfileData.increaseBalance() -> Can't update balance");
        }
    }

    public void decreaseBalance(double amount) {
        if ((amount > 0) && (amount <= balance)) {
            balance -= amount;
        } else {
            System.out.println("ProfileData.decreaseBalance() -> Can't update balance");
        }
    }

    public double getHighestWin() {
        return highestWin;
    }

    public void setHighestWin(double highestWin) {
        if (highestWin > this.highestWin) {
            this.highestWin = highestWin;
        } else {
            System.out.println("ProfileData.setHighestWin() -> Can't set highestWin");
        }
    }
}
