package buyers;

import java.util.ArrayList;

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
	private static ArrayList<Company> boughtStocks;

    public Buyers(String companyName, StockManager manager) {
		totalBuyers++;
		this.companyName = companyName;
		this.stockManager = manager;
		this.buyerID = totalBuyers;
		boughtStocks = new ArrayList<Company>();
	}

	@Override
	public void run(){
		System.out.println("Buyer " + buyerID + " started!");
		try {
			for(int i=0;i<100;i++) {
				int sleepTime = RandomRange.randomWithRange(2, 6) * 1000;
				Thread.sleep(sleepTime);
				Company request = new Company(this.companyName);
				stockManager.addARequest(buyerID, request);
			}
		}catch(InterruptedException e) {
			System.out.println(this);
		}
	}

    public ArrayList<Company> getBoughtStocks() {
        return boughtStocks;
    }

    public void setBoughtStocks(ArrayList<Company> boughtStocks) {
        Buyers.boughtStocks = boughtStocks;
    }

    public static void addBoughtStocks(Company stockToBeAdded){
        boughtStocks.add(stockToBeAdded);
    }

	@Override
	public String toString() {
		return "Buyer " + buyerID + " stoped buying.";
	}

}