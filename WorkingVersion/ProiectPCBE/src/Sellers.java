public class Sellers implements Runnable{
	
	@Override
	public String toString() {
		return "Seller " + sellerID + " from " + companyName + " got out of stocks!";
	}

	private static int totalSellers = 0;
	private int sellerID;
	private String companyName;
	private StockManager manager;
	
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
				int sleepTime = Main.randomWithRange(1, 3)*1000;
					Thread.sleep(sleepTime);
					Stock stockToSell = Stock.createRandomStock(this.companyName);
					putOnSale(stockToSell, this.manager);
			}
		}catch(InterruptedException e) {
				System.out.println(this);
		}
			
	}
	
	public void putOnSale(Stock stock, StockManager manager) throws InterruptedException {
		//System.out.println("Seller " + sellerID + " tries to aquire...");
		manager.aquireMutex();
		//System.out.println("Seller " + sellerID + " aquired lock.");
		manager.getStockList().add(stock);
		System.out.println("Seller " + sellerID + " added " + stock);
		manager.releaseMutex();
		//System.out.println("Seller " + sellerID + " released lock.");
	}
	
	public int getSellerID() {
		return sellerID;
	}


}
