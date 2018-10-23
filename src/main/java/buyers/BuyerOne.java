package buyers;

import interfaces.Buyer;
import interfaces.Stock;

public class BuyerOne implements Buyer {

    private double money;
    private Stock stock;
    private double startingSum;
    private int totalStocks;

    public BuyerOne(double money, Stock stock,double startingSum) {
        this.money = money;
        this.stock = stock;
        this.startingSum = startingSum;
    }

    public void subtractMoney(double amount){
        money -= amount;
    }

    @Override
    public double getStartingSum() {
        return startingSum;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public Stock getStock() {
        return stock;
    }

    @Override
    public void addStocks() {
        totalStocks++;
    }

    public int getTotalStocks() {
        return totalStocks;
    }
}
