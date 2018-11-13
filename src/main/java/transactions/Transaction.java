package transactions;

import stocks.StockManager;

public class Transaction implements Runnable {

	private StockManager stockManager;

	public Transaction(StockManager manager) {
		this.stockManager = manager;
	}

	@Override
	public void run(){
		while(true) {
			stockManager.lookForMatchingStock();
		}
	}

}
