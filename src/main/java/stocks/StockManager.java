package stocks;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class StockManager {
	
	private ArrayList<Stock> stockList;
	private ArrayList<Stock> transactionList;
	private ArrayList<Stock> requestList;

	/*
	Initializarea unui obiect de tip Semaphore.
	Semaphore este o clasa care mentine un count numit "permits". Ea este folosita pentru a gestiona resursele
	Permiturile sunt folosite ca un "lacat" pentru a putea
	 */
	private static final Semaphore mutex = new Semaphore(1);

	public StockManager() {
		this.stockList = new ArrayList<>();
		this.transactionList = new ArrayList<>();
		this.requestList = new ArrayList<>();
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

	/*
	Metoda acquire reduce numarul de permits din mutex cu 1
	aqcuire() va astepta daca nu exista nici un permit available pana cand un permit va fi released.
	 */
	public void aquireMutex() throws InterruptedException {
		StockManager.mutex.acquire();
	}

	//Metoda release adauga un permit obiectului mutex de tip Semaphore
	public void releaseMutex() {
		StockManager.mutex.release();
	}
	
}
