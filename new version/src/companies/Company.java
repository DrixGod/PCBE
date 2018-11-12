package companies;

import utils.RandomRange;

public class Company {
	
	private String companyName;
	private int stockAmount;
	private int stockPrice;

	public Company(String companyName) {
		this.companyName = companyName;
		this.stockAmount = RandomRange.randomWithRange(10, 30);
		this.stockPrice =  RandomRange.randomWithRange(55, 60);
	}
	
	public String getCompanyName() {
	    return companyName;
	}

	public int getStockAmount() {
	    return stockAmount;
	}

	public synchronized void setStockAmount(int stockAmount) {
	    this.stockAmount = stockAmount;
	}

	public int getStockPrice() {
		return stockPrice;
	}

	@Override
	public String toString() {
		return "companies.Company [companyName=" + companyName + ", stockAmount=" +
				stockAmount + "," + " stockPrice=" + stockPrice + "\u20ac]\n";
	}

}
