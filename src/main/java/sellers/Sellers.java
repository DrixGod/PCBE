package sellers;

import companies.Company;
import stocks.StockManager;
import utils.RandomRange;

/*
Clasa Sellers este folosita pentru a creea o instanta a unui vanzator.
Clasa implementeaza interfata Runnable si suprascrie metoda run.
Atunci cand un thread este pornit, in metoda run se vor creea stocuri de tipul numelui dat ca parametru si se vor adauca ca si oferte in StockManager
 */
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
			for(int i=0;i<100;i++) {
				int sleepTime = RandomRange.randomWithRange(1, 3)*1000;
				Thread.sleep(sleepTime); // Folosim un timp random de asteptare pentru a simula cat mai bine un caz real
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