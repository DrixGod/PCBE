public class Sellers implements Runnable{
	
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
			for(int i=0; i<10; i++) {
				int sleepTime = Main.randomWithRange(0, 0);
				try {
					Thread.sleep(sleepTime);
					Stock stockToSell = Stock.createRandomStock(this.companyName);
					putOnSale(stockToSell, this.manager);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
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
