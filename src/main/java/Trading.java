import interfaces.Buyer;
import interfaces.Stock;

public class Trading implements Runnable {

    private Buyer buyer;
    private Stock stock;

    public Trading(Buyer buyer, Stock stock){
        this.buyer = buyer;
        this.stock = stock;
    }

    @Override
    public void run() {

        for(int i=0;i<1000;i++){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Generates a random number between -.03 and .03
            double stockChange = (Math.random() * (.06)) - .03;
            stock.modifyPrice(stockChange);
            System.out.println(stock + " Amount: " + stock.getAmount() + " Price: " + stock.getPrice() + " Change: " + stockChange);
            if(buyer.getStartingSum() == stock.getPrice()){
                while (stock.getAmount()>0 && buyer.getMoney()>stock.getPrice()){
                    buyer.addStocks();
                    stock.subtractAmount(1);
                }
            }

        }

    }
}
