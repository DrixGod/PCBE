package interfaces;

public interface Stock {

    public void subtractAmount(int amount);
    public void modifyPrice(double price);
    public double getPrice();
    public double getAmount();
}
