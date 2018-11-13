package transactions;

import stocks.Stock;

public class Transaction implements Runnable {

	private Stock stockManager;

	public Transaction(Stock manager) {
		this.stockManager = manager;
	}

	@Override
	public void run(){
		while(true) {
			stockManager.lookForMatchingStock();
		}
	}

}
