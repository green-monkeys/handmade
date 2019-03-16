package com.greenmonkeys.handmade.payout_denominations;

import java.util.ArrayList;

public class PesoWallet {

    private ArrayList<Integer> walletValues = new ArrayList<>();

    public PesoWallet(ArrayList<Integer> values){
        if(values.size()!=14){
            System.err.println("Creating a PesoWallet needs and arrayList of 14 values");
            System.exit(1);
        }
        walletValues.addAll(values);
    }

    /*int fiveHundred, int twoHundred, int oneHundred,
                      int fifty, int twenty, int ten, int five,
                      int two, int one, int fiftyCent, int twentyCent,
                      int tenCent, int fiveCent, int oneCent){

        this.fiveHundred=fiveHundred;
        this.twoHundred=twoHundred;
        this.oneHundred=oneHundred;
        this.fifty=fifty;
        this.twenty=twenty;
        this.ten=ten;
        this.five=five;
        this.two=two;
        this.one=one;
        this.fiftyCent=fiftyCent;
        this.twentyCent=twentyCent;
        this.tenCent=tenCent;
        this.fiveCent=fiveCent;
        this.oneCent=oneCent;
    }*/
    public int getFiveHundred(){
        return walletValues.get(0);
    }
    public int getTwoHundred(){
        return walletValues.get(1);
    }
    public int getOneHundred(){
        return walletValues.get(2);
    }
    public int getFifty(){
        return walletValues.get(3);
    }
    public int getTwenty(){
        return walletValues.get(4);
    }
    public int getTen(){
        return walletValues.get(5);
    }
    public int getFive(){
        return walletValues.get(6);
    }
    public int getTwo(){
        return walletValues.get(7);
    }
    public int getOne(){
        return walletValues.get(8);
    }
    public int getFiftyCent(){
        return walletValues.get(9);
    }
    public int getTwentyCent(){
        return walletValues.get(10);
    }
    public int getTenCent(){
        return walletValues.get(11);
    }
    public int getFiveCent(){
        return walletValues.get(12);
    }
    public int getOneCent(){
        return walletValues.get(13);
    }
}
