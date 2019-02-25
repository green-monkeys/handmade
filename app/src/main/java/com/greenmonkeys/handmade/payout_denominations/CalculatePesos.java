package com.greenmonkeys.handmade.payout_denominations;

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
    public PesoWallet calculateSingle(double totalAmount){
        double amount = totalAmount;
        int[] pesoCounter = new int[9];

        //do some whole peso calculations
        for(int i = 0; i<9; i++){
            if(amount >= pesos[i]){
                pesoCounter[i] = (int)amount / pesos[i];
                amount = amount - pesoCounter[i] * pesos[i];
            }
            else{pesoCounter[i] = 0;}
        }

        //now do some centavos calculations
        amount*=100;
        int[] centCounter = new int[6];
        for(int i  = 0; i<5; i++){
            if(amount >= centCounter[i]){
                centCounter[i] = (int)amount / centavos[i];
                amount = amount - centCounter[i] * centavos[i];
            }
            else{centCounter[i] = 0;}
        }

        return new PesoWallet(pesoCounter[0], pesoCounter[1], pesoCounter[2],
                pesoCounter[3], pesoCounter[4], pesoCounter[5], pesoCounter[6],
                pesoCounter[7], pesoCounter[8], centCounter[0], centCounter[1], centCounter[2],
                centCounter[3], centCounter[4]);
    }

    /**
     * This method calculates the payout denominations for each double in the list.
     * It then adds all of the denominations together to return a new wallet that
     * has total denomination values from the list of payouts.
     * @param list
     * @return
     */
    public PesoWallet calculateTotal(List<Double> list){
        PesoWallet result = new PesoWallet(0,0,0,0,0,0,0,0,0,0,0,0,0,0);
        for(double item : list){
            result.add(calculateSingle(item));
        }
        return result;
    }
}
