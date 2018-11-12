package sellers;

import companies.Company;
import stocks.StockManager;
import utils.RandomRange;

public class Sellers implements Runnable{

	private static int totalSellers = 0;
	private int sellerID;
	private String companyName;
	private StockManager stockManager;
	
	public Sellers(String companyName, StockManager manager) {
		totalSellers++;
		this.sellerID = totalSellers;
		this.companyName = companyName;
		this.stockManager = manager;
	}

	@Override
	public void run() {
		System.out.println("Seller " + sellerID + " started!");
		try {
			for(;;) {
				int sleepTime = RandomRange.randomWithRange(1, 3)*1000;
				Thread.sleep(sleepTime);

				Company stockToSell = new Company(this.companyName);
                stockManager.addAnOffer(sellerID, stockToSell);
			}
		}catch(InterruptedException e) {
		    System.out.println(this);
		}
	}

    @Override
    public String toString() {
        return "Seller " + sellerID + " from " + companyName + " stopped.";
    }

}
