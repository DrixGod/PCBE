package stocks;

public class Stock implements Runnable {

    private StockManager stockManager;

    public Stock(StockManager manager) {
        this.stockManager = manager;
    }

    @Override
    public void run(){
        try {
            while(true) {
                stockManager.lookForMatchingStock();
            }
        }catch(InterruptedException e) {
            System.out.println(this);
        }
    }

}
