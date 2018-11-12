package buyers;

import java.util.ArrayList;

import companies.Company;
import stocks.StockManager;
import utils.RandomRange;

public class Buyers implements Runnable {
	
	private StockManager stockManager;
	private static int totalBuyers = 0;
	private String companyName;
	private int buyerID;
	private ArrayList<Company> boughtStocks;
	
	public Buyers(String companyName, StockManager manager) {
        totalBuyers++;
		this.companyName = companyName;
		this.stockManager = manager;
		this.buyerID = totalBuyers;
		this.boughtStocks = new ArrayList<Company>();
	}

    @Override
	public void run(){
		System.out.println("Buyer " + buyerID + " started!");
		try {
			while(true) {
			    int sleepTime = RandomRange.randomWithRange(2, 6) * 1000;
				Thread.sleep(sleepTime);

                Company request = new Company(this.companyName);
                stockManager.addARequest(buyerID, request);
			}

		}catch(InterruptedException e) {
			System.out.println(this);
		}
	}

    @Override
    public String toString() {
        return "Buyer " + buyerID + " stoped buying.\nHe bought " + boughtStocks.size() + " stocks " + boughtStocks;
    }

}
