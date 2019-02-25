package com.greenmonkeys.handmade.payout_denominations;

public class PesoWallet {

    private int fiveHundred,twoHundred, oneHundred, fifty, twenty, ten, five, two, one, fiftyCent, twentyCent, tenCent, fiveCent, oneCent;

    public PesoWallet(int fiveHundred, int twoHundred, int oneHundred,
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
    }
    public int getFiveHundred(){
        return fiveHundred;
    }
    public int getTwoHundred(){
        return twoHundred;
    }
    public int getOneHundred(){
        return oneHundred;
    }
    public int getFifty(){
        return fifty;
    }
    public int getTwenty(){
        return twenty;
    }
    public int getTen(){
        return ten;
    }
    public int getFive(){
        return five;
    }
    public int getTwo(){
        return two;
    }
    public int getOne(){
        return one;
    }
    public int getFiftyCent(){
        return fiftyCent;
    }
    public int getTwentyCent(){
        return twentyCent;
    }
    public int getTenCent(){
        return tenCent;
    }
    public int getFiveCent(){
        return fiveCent;
    }
    public int getOneCent(){
        return oneCent;
    }

    /*
     *  Creates a new PesoWallet with the sum of the other PesoWallet and the second PesoWallet
     */
    public void add(PesoWallet other){
        this.fiveHundred += other.getFiveHundred();
        this.twoHundred += other.getTwoHundred();
        this.oneHundred += other.getOneHundred();
        this.fifty += other.getFifty();
        this.twenty += other.getTwenty();
        this.ten += other.getTen();
        this.five += other.getFive();
        this.two += other.getTwo();
        this.one += other.getOne();
        this.fiftyCent += other.getFiftyCent();
        this.twentyCent += other.getTwentyCent();
        this.tenCent += other.getTenCent();
        this.fiveCent += other.getFiveCent();
        this.oneCent += other.getOneCent();    }
}
