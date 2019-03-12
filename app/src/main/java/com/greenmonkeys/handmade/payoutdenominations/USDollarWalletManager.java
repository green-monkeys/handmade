package com.greenmonkeys.handmade.payoutdenominations;

public class USDollarWalletManager {
    public static USDollarWallet add(USDollarWallet first, USDollarWallet second) {
        return new USDollarWallet (
                first.getHundred() + second.getHundred(),
                first.getFifty() + second.getFifty(),
                first.getTwenty() + second.getTwenty(),
                first.getTen() + second.getTen(),
                first.getFive() + second.getFive(),
                first.getOne() + second.getOne(),
                first.getQuarter() + second.getQuarter(),
                first.getDime() + second.getDime(),
                first.getNickel() + second.getNickel(),
                first.getPenny() + second.getPenny()
        );
    }

    public static USDollarWallet addWalletsInArray(USDollarWallet[] wallets) {
        USDollarWallet result = new USDollarWallet(0.0);
        for (USDollarWallet wallet : wallets) {
            USDollarWallet previous = result;
            result = add(result, wallet);
        }
        return result;
    }
}
