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
     *  Creates a new PesoWallet with the sum of the first PesoWallet and the second PesoWallet
     */
    public PesoWallet add(PesoWallet first, PesoWallet second){
        int fiveHundred = first.getFiveHundred() + second.getFiveHundred();
        int twoHundred = first.getTwoHundred() + second.getTwoHundred();
        int oneHundred = first.getOneHundred() + second.getOneHundred();
        int fifty = first.getFifty() + second.getFifty();
        int twenty = first.getTwenty() + second.getTwenty();
        int ten = first.getTen() + second.getTen();
        int five = first.getFive() + second.getFive();
        int two = first.getTwo() + second.getTwo();
        int one = first.getOne() + second.getOne();
        int fiftyCent = first.getFiftyCent() + second.getFiftyCent();
        int twentyCent = first.getTwentyCent() + second.getTwentyCent();
        int tenCent = first.getTenCent() + second.getTenCent();
        int fiveCent = first.getFiveCent() + second.getFiveCent();
        int oneCent = first.getOneCent() + second.getOneCent();
        return new PesoWallet(fiveHundred,twoHundred,oneHundred,
                                fifty,twenty,ten,five,
                                two,one,fiftyCent,twentyCent,
                                tenCent,fiveCent,oneCent);

    }
}
