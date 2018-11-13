package buyers;

import companies.Company;
import stocks.StockManager;
import utils.RandomRange;

/*
Clasa Buyers este folosita pentru a creea o instanta a unui cumparator.
Clasa implementeaza interfata Runnable si suprascrie metoda run.
Atunci cand un thread este pornit, in metoda run se vor creea cereri de tipul numelui dat ca parametru si se vor adauca ca si cerere in StockManager
 */
public class Buyers implements Runnable {

	private StockManager stockManager;
	private static int totalBuyers = 0;
	private String companyName;
	private int buyerID;

    public Buyers(String companyName, StockManager manager) {
		totalBuyers++;
		this.companyName = companyName;
		this.stockManager = manager;
		this.buyerID = totalBuyers;
	}

	@Override
	public void run(){
		System.out.println("Buyer " + buyerID + " started!");
		try {
			while(true) {
				Company request = new Company(this.companyName);
				stockManager.addARequest(buyerID, request);

                int sleepTime = RandomRange.randomWithRange(2, 6) * 1000;
                Thread.sleep(sleepTime);
			}
		}catch(InterruptedException e) {
			System.out.println(this);
		}
	}

	@Override
	public String toString() {
		return "Buyer " + buyerID + " stoped buying.";
	}

}