package com.greenmonkeys.handmade.payout_denominations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculatePesos {
    private int[] pesos = new int[]{500,200,100,50,20,10,5,2,1};
    private int[] centavos = new int[]{50,20,10,5,1};

    /**
     * This method calculates the different payout denominations
     * and returns a wallet containing these denominations
     * @param totalAmount
     * @return
     */
    public PesoWallet calculateSingleWallet(double totalAmount){
        double amount = totalAmount;
        ArrayList<Integer> pesoCounter = new ArrayList<>();

        //do some whole peso calculations
        for(int i = 0; i<9; i++){
            if(amount >= pesos[i]){
                pesoCounter.add((int)amount / pesos[i]);
                amount = amount - pesoCounter.get(i) * pesos[i];
            }
            else{
                pesoCounter.add(0);
            }
        }

        //now do some centavos calculations
        amount*=100;
        for(int i  = 9; i<14; i++){
            if(amount >= centavos[i-9]){
                pesoCounter.add((int)amount / centavos[i-9]);
                amount = amount - pesoCounter.get(i) * centavos[i-9];
            }
            else{
                pesoCounter.add(0);
            }
        }

        return new PesoWallet(pesoCounter);
    }

    public PesoWallet calculateTotal(List<Double> list){
        ArrayList<Integer> newWallet = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0));
        PesoWallet result = new PesoWallet(newWallet);
        for(double item : list){
            result = add(result,calculateSingleWallet(item));
        }
        return result;
    }

    public PesoWallet add(PesoWallet w1, PesoWallet w2){
        ArrayList<Integer> newWallet = new ArrayList<>();
        newWallet.add(w1.getFiveHundred()+w2.getFiveHundred());
        newWallet.add(w1.getTwoHundred()+w2.getTwoHundred());
        newWallet.add(w1.getOneHundred()+w2.getOneHundred());
        newWallet.add(w1.getFifty()+w2.getFifty());
        newWallet.add(w1.getTwenty()+w2.getTwenty());
        newWallet.add(w1.getTen()+w2.getTen());
        newWallet.add(w1.getFive()+w2.getFive());
        newWallet.add(w1.getTwo()+w2.getTwo());
        newWallet.add(w1.getOne()+w2.getOne());
        newWallet.add(w1.getFiftyCent()+w2.getFiftyCent());
        newWallet.add(w1.getTwentyCent()+w2.getTwentyCent());
        newWallet.add(w1.getTenCent()+w2.getTenCent());
        newWallet.add(w1.getFiveCent()+w2.getFiveCent());
        newWallet.add(w1.getOneCent()+w2.getOneCent());
        return new PesoWallet(newWallet);
    }
}