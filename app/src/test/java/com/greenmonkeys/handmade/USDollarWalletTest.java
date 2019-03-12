package com.greenmonkeys.handmade;

import com.greenmonkeys.handmade.payoutdenominations.USDollarWallet;
import com.greenmonkeys.handmade.payoutdenominations.USDollarWalletManager;
import org.junit.Assert;
import org.junit.Test;

public class USDollarWalletTest {
    @Test public void testAdd() {
        USDollarWallet wallet = new USDollarWallet(1010.0);
        USDollarWallet wallet2 = new USDollarWallet(1010.0);
        USDollarWallet combined = USDollarWalletManager.add(wallet, wallet2);
        Assert.assertEquals(2020.0, combined.total(), 0.001);
        Assert.assertEquals(20, combined.getHundred());
        Assert.assertEquals(2, combined.getTen());
    }

    @Test public void testComposition() {
        USDollarWallet wallet = new USDollarWallet(199.99);
        Assert.assertEquals(wallet.getHundred(), 1);
        Assert.assertEquals(wallet.getFifty(), 1);
        Assert.assertEquals(wallet.getTwenty(), 2);
        Assert.assertEquals(wallet.getTen(), 0);
        Assert.assertEquals(wallet.getFive(), 1);
        Assert.assertEquals(wallet.getOne(), 4);
        Assert.assertEquals(wallet.getQuarter(), 3);
        Assert.assertEquals(wallet.getDime(), 2);
        Assert.assertEquals(wallet.getNickel(), 0);
        Assert.assertEquals(wallet.getPenny(), 4);
    }

    @Test public void testArrayAdd() {
        USDollarWallet w1 = new USDollarWallet (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        USDollarWallet w2 = new USDollarWallet(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        USDollarWallet addedWallets = USDollarWalletManager.addWalletsInArray( new USDollarWallet[] {w1,w2} );
        Assert.assertEquals(addedWallets.getHundred(), 11);
        Assert.assertEquals(addedWallets.getFifty(), 11);
        Assert.assertEquals(addedWallets.getTwenty(), 11);
        Assert.assertEquals(addedWallets.getTen(), 11);
        Assert.assertEquals(addedWallets.getFive(), 11);
        Assert.assertEquals(addedWallets.getOne(), 11);
        Assert.assertEquals(addedWallets.getQuarter(), 11);
        Assert.assertEquals(addedWallets.getDime(), 11);
        Assert.assertEquals(addedWallets.getNickel(), 11);
        Assert.assertEquals(addedWallets.getPenny(), 11);
    }
    public void run() {
        testAdd();
        testComposition();
        testArrayAdd();
    }

}