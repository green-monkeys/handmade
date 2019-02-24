public class Main {
    public static void main(String[] args){
        CalculatePesos testCalculate = new CalculatePesos();
        PesoWallet testWallet = testCalculate.calculate(1197.97);
        Tests test = new Tests();
        test.run();
    }
}
