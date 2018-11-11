package stocks;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class StockManager {
	
	private ArrayList<Stock> stockList;
	private ArrayList<Stock> transactionList;
	private ArrayList<Stock> requestList;
	
	private static final Semaphore mutex = new Semaphore(1);

	public StockManager() {
		this.stockList = new ArrayList<Stock>();
		this.transactionList = new ArrayList<Stock>();
		this.requestList = new ArrayList<Stock>();
	}
	
	public ArrayList<Stock> getStockList(){
		return this.stockList;
	}

	public ArrayList<Stock> getTransactionList(){
		return transactionList;
	}

	public ArrayList<Stock> getRequestList() {
		return requestList;
	}
	
	public void aquireMutex() throws InterruptedException {
		StockManager.mutex.acquire();
	}
	
	public void releaseMutex() {
		StockManager.mutex.release();
	}
	
}
