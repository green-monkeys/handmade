package com.greenmonkeys.handmade.payoutdenominations;

public class USDollarWallet {
    private int hundred, fifty, twenty, ten, five, one, quarter, dime, nickel, penny;

    public USDollarWallet(int hundred, int fifty, int twenty, int ten,
                          int five, int one, int quarter, int dime,
                          int nickel, int penny) {
        this.hundred = hundred;
        this.fifty = fifty;
        this.twenty = twenty;
        this.ten = ten;
        this.five = five;
        this.one = one;
        this.quarter = quarter;
        this.dime = dime;
        this.nickel = nickel;
        this.penny = penny;
    }

    public USDollarWallet(double balance) {
        double runningBalance = balance;

        // hundreds
        while (runningBalance >= 100.0) {
            this.hundred++;
            runningBalance -= 100.0;
        }
        while (runningBalance >= 50.0) {
            this.fifty++;
            runningBalance -= 50.0;
        }
        while (runningBalance >= 20.0) {
            this.twenty++;
            runningBalance -= 20.0;
        }
        while (runningBalance >= 10.0) {
            this.ten++;
            runningBalance -= 10.0;
        }
        while (runningBalance >= 5.0) {
            this.five++;
            runningBalance -= 5.0;
        }
        while (runningBalance >= 1.0) {
            this.one++;
            runningBalance -= 1.0;
        }
        while (runningBalance >= 0.25) {
            this.quarter++;
            runningBalance -= 0.25;
        }
        while (runningBalance >= 0.1) {
            this.dime++;
            runningBalance -= 0.1;
        }
        while (runningBalance >= 0.05) {
            this.nickel++;
            runningBalance -= 0.05;
        }
        while (runningBalance >= 0.01) {
            this.penny++;
            runningBalance -= 0.01;
        }

    }

    public double total() {
        return (hundred * 100.0) +
                (fifty * 50.0) +
                (twenty * 20.0) +
                (ten * 10.0) +
                (five * 5.0) +
                (one * 1.0) +
                (quarter * 0.25) +
                (dime * 0.1) +
                (nickel * 0.05) +
                (penny * 0.01);
    }

    public int getHundred() {
        return hundred;
    }

    public int getFifty() {
        return fifty;
    }

    public int getTwenty() {
        return twenty;
    }

    public int getTen() {
        return ten;
    }

    public int getFive() {
        return five;
    }

    public int getOne() {
        return one;
    }

    public int getQuarter() {
        return quarter;
    }

    public int getDime() {
        return dime;
    }

    public int getNickel() {
        return nickel;
    }

    public int getPenny() {
        return penny;
    }

    @Override
    public String toString() {
        return this.hundred + " hundreds\n" +
                this.fifty + " fifties\n" +
                this.twenty + " twenties\n" +
                this.ten + " tens\n" +
                this.five + " fives\n" +
                this.one + " ones\n" +
                this.quarter + " quarters\n" +
                this.dime + " dimes\n" +
                this.nickel + " nickels\n" +
                this.penny + " pennies\n";
    }
}