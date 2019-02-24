import org.junit.Assert;
import org.junit.Test;


public class Tests {
    @Test
    public static void testCalculatePesos1(){
        CalculatePesos test = new CalculatePesos();
        PesoWallet wallet = test.calculate(1397.97);
        Assert.assertEquals(wallet.getFiveHundred(),2);
        Assert.assertEquals(wallet.getTwoHundred(),1);
        Assert.assertEquals(wallet.getOneHundred(),1);
        Assert.assertEquals(wallet.getFifty(),1);
        Assert.assertEquals(wallet.getTwenty(), 2);
        Assert.assertEquals(wallet.getTen(),0);
        Assert.assertEquals(wallet.getFive(), 1);
        Assert.assertEquals(wallet.getTwo(),1);
        Assert.assertEquals(wallet.getOne(),0);
        Assert.assertEquals(wallet.getFiftyCent(),1);
        Assert.assertEquals(wallet.getTwentyCent(), 2);
        Assert.assertEquals(wallet.getTenCent(),0);
        Assert.assertEquals(wallet.getFiveCent(),1);
        Assert.assertEquals(wallet.getOneCent(),2);
        System.out.println("Success: Test 1");
    }
    public void run(){
        testCalculatePesos1();
    }
}
