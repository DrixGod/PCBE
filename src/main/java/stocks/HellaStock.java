package stocks;

import interfaces.Stock;

public class HellaStock implements Stock {

    private double stockPrice;
    private int stockAmount;

    public HellaStock(double stockPrice, int stockAmount) {
        this.stockPrice = stockPrice;
        this.stockAmount = stockAmount;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }

    public void modifyPrice(double price){
        stockPrice -= price;
    }

    public void subtractAmount(int amount){
        stockAmount -= amount;
    }

    @Override
    public double getPrice() {
        return stockPrice;
    }

    @Override
    public double getAmount() {
        return stockAmount;
    }
}
