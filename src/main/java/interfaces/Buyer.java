package interfaces;

public interface Buyer {

    public void subtractMoney(double amount);
    public double getStartingSum();
    public double getMoney();
    public Stock getStock();
    public void addStocks();
}
