package stocks;

import utils.RandomRange;

public class Stock {
	
	private String companyName;
	private int stockAmount;
	private int stockPrice;
	
	public Stock(String companyName, int stockAmount, int stockPrice) {
		this.companyName = companyName;
		this.stockAmount = stockAmount;
		this.stockPrice = stockPrice;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getStockAmount() {
		return stockAmount;
	}
	public void setStockAmount(int stockAmount) {
		this.stockAmount = stockAmount;
	}
	public int getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(int stockPrice) {
		this.stockPrice = stockPrice;
	}

	//Creeaza un nou stock dupa un nume dat ca si parametru cu actiuni si pret random
	public static Stock createRandomStock(String companyName) {
		int stockAmount = RandomRange.randomWithRange(10, 30);
		int stockPrice = RandomRange.randomWithRange(55, 60);
		return new Stock(companyName, stockAmount, stockPrice);
	}

	@Override
	public String toString() {
		return "Stock: [companyName=" + companyName + ", stockAmount=" + stockAmount + ", stockPrice=" + stockPrice
				+ "\u20ac]\n";
	}
	
	
	
	
}
