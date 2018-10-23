import buyers.BuyerOne;
import buyers.BuyerTwo;
import stocks.ContiStock;
import stocks.HellaStock;
import stocks.NokiaStock;

public class Bursa {

    public static void main(String ... args){

        ContiStock contiStock = new ContiStock(123.12,100);
        NokiaStock nokiaStock = new NokiaStock(15.2,50);
        HellaStock hellaStock = new HellaStock(86.3,200);

        BuyerOne buyerOne = new BuyerOne(1000,contiStock,122);
        BuyerTwo buyerTwo = new BuyerTwo(2000,hellaStock,85);

        Runnable  trading1 = new Trading(buyerOne,contiStock);
        Runnable  trading2 = new Trading(buyerTwo,hellaStock);

        new Thread(trading1).start();
        new Thread(trading2).start();

        System.out.println();
    }
}
