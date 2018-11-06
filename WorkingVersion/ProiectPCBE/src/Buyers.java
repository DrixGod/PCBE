import java.util.ArrayList;

public class Buyers implements Runnable{
	
	private StockManager manager;
	private static int totalBuyers = 0;
	private String companyName;
	private int buyerID;
	private ArrayList<Stock> boughtStocks;
	
	public Buyers(String companyName, StockManager manager) {
		this.companyName = companyName;
		this.manager = manager;
		totalBuyers++;
		this.buyerID = totalBuyers;
		this.boughtStocks = new ArrayList<Stock>();
	}

	@Override
	public void run(){
		System.out.println("Buyer " + buyerID + " started!");
		try {
			Stock request = Stock.createRandomStock(this.companyName);
			for(;;) {
				int sleepTime = Main.randomWithRange(2, 6)*1000;
				Thread.sleep(sleepTime);
				if(tryToBuy(request)) {
					request = Stock.createRandomStock(this.companyName);		
				}
			}
		}catch(InterruptedException e) {
			System.out.println(this);
		}
	}

	public int getBuyerID() {
		return buyerID;
	}
	
	public ArrayList<Stock> getBoughtStocks(){
		return this.boughtStocks;
	}
	
	@Override
	public String toString() {
		return "Buyer " + buyerID + " stoped buying.\nHe bought " + boughtStocks.size() + " stocks " + boughtStocks;
	}

	public boolean tryToBuy(Stock request) throws InterruptedException {
		
		//System.out.println("Buyer " + buyerID + " tries to aquire...");
		manager.aquireMutex();
		//System.out.println("Buyer " + buyerID + " aquired lock.");
		
		ArrayList<Stock> stockList = manager.getStockList();
		ArrayList<Stock> requestList = manager.getRequestList();
		ArrayList<Stock> transactionList = manager.getTransactionList();
		
		System.out.println("Buyer " + buyerID + " tries to buy " + request);
		System.out.println("Current Stock list: \n" + stockList);
		System.out.println("Current Request list: \n" + requestList);
		System.out.println("Current Transaction list: \n" + transactionList);
		
		for(Stock stock: stockList) {
			
			if(stock.getCompanyName().equals(request.getCompanyName())
					&& stock.getStockPrice() == request.getStockPrice()) {
				
				int stockAmount = stock.getStockAmount();
				int requestAmount =  request.getStockAmount();
				
				if(stockAmount >= requestAmount) {
					
					if(stockAmount == requestAmount) {
						stockList.remove(request);
					}else {
						stock.setStockAmount(stockAmount - requestAmount);
					}
					this.boughtStocks.add(request);
					transactionList.add(request);
					if(requestList.contains(request)) {
						requestList.remove(request);
					}
					
					System.out.println("Current Stock list: \n" + stockList);
					System.out.println("Current Request list: \n" + requestList);
					System.out.println("Current Transaction list: \n" + transactionList);
					manager.releaseMutex();
					//System.out.println("Buyer " + buyerID + " released lock.");
					return true;
					
				}else if(stockAmount < requestAmount){
					if(requestList.contains(request)) {
						int index = requestList.indexOf(request);
						requestList.get(index).setStockAmount(requestAmount - stockAmount);
					}
					this.boughtStocks.add(request);
					transactionList.add(request);
					request.setStockAmount(requestAmount - stockAmount);
					stockList.remove(stock);
					System.out.println("Current Stock list: \n" + stockList);
					System.out.println("Current Request list: \n" + requestList);
					System.out.println("Current Transaction list: \n" + transactionList);
					manager.releaseMutex();
					//System.out.println("Buyer " + buyerID + " released lock.");
					return false;
					
				}
			}
		}
	
		if(!requestList.contains(request)) {
			requestList.add(request);
		}
		System.out.println("Current Stock list: \n" + stockList);
		System.out.println("Current Request list: \n" + requestList);
		System.out.println("Current Transaction list: \n" + transactionList);
		manager.releaseMutex();
		//System.out.println("Buyer " + buyerID + " released lock.");
		return false;
	}

}
