package sellers;

import stocks.Stock;
import stocks.StockManager;
import utils.RandomRange;

public class Sellers implements Runnable{
	
	@Override
	public String toString() {
		return "Seller " + sellerID + " from " + companyName + " got out of stocks!";
	}

	private static int totalSellers = 0;
	private int sellerID;
	private String companyName;
	private StockManager manager;

	//Constructor pentru vanzatori. Pentru a initializa un nou vanzator este nevoie de un nume si un StockManager
	public Sellers(String companyName, StockManager manager) {
		totalSellers++;
		this.sellerID = totalSellers;
		this.companyName = companyName;
		this.manager = manager;
	}


	@Override
	public void run() {
		System.out.println("Seller " + sellerID + " started!");
		try {
			for(;;) {
				int sleepTime = RandomRange.randomWithRange(1, 3)*1000;
					Thread.sleep(sleepTime); //Folosimn functia sleep pentru a opri temporar thread-ul pentru o perioada random
					Stock stockToSell = Stock.createRandomStock(this.companyName);
					putOnSale(stockToSell, this.manager);
			}
		}catch(InterruptedException e) {
				System.out.println(this);
		}
			
	}

	//Metoda pentru a pune un stock in vanzare
	public void putOnSale(Stock stock, StockManager manager) throws InterruptedException {
		System.out.println("Seller " + sellerID + " tries to aquire...");
		manager.aquireMutex(); //Folosim un permit din semaphore
		System.out.println("Seller " + sellerID + " aquired lock.");
		manager.getStockList().add(stock); //Adaugam stockul la lista de stockuri
		System.out.println("Seller " + sellerID + " added " + stock);
		manager.releaseMutex(); //Dam release la permit
		System.out.println("Seller " + sellerID + " released lock.");
	}
	
	public int getSellerID() {
		return sellerID;
	}


}
