package stocks;

import companies.Company;
import utils.ConcurrentMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class StockManager {

	private volatile ConcurrentMap<Integer, Company> requestsMap;
	private volatile ConcurrentMap<Integer, Company> offersMap;
	private volatile ArrayList<Company> transactionList;

	public StockManager() {
		requestsMap = new ConcurrentMap<Integer, Company>();
		offersMap = new ConcurrentMap<Integer, Company>();
		transactionList = new ArrayList<Company>();
	}

	public synchronized void addAnOffer(int sellerID, Company stock) throws InterruptedException {
		System.out.println("Seller " + sellerID + " added " + stock);
		offersMap.put(sellerID, stock);
		notifyAll();
	}

	public synchronized void addARequest(int buyerID, Company request) throws InterruptedException {
		System.out.println("Buyer " + buyerID + " tries to buy " + request);
		requestsMap.put(buyerID, request);
		notifyAll();
	}

	public synchronized void lookForMatchingStock() throws InterruptedException {

		System.out.println("Current Offers. \n" + offersMap);
		System.out.println("Current Requests: \n" + requestsMap);
		System.out.println("Current Transactions list: \n" + transactionList);

		requestsMap.entrySet().iterator();
		Iterator it = requestsMap.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Integer, Company>  requestStock = (Map.Entry<Integer, Company>)it.next();

			int sellerId = findSellerForStockRequest(requestStock.getValue());

			if (sellerId != -1) {
				Company offerStock = offersMap.get(sellerId);
				int stockAmount = offerStock.getStockAmount();
				int requestAmount = requestStock.getValue().getStockAmount();

				if (stockAmount == requestAmount) {
					transactionList.add(offerStock);
					offersMap.delete(sellerId);
					it.remove();
					transactionList.add(offerStock);

				} else if (stockAmount > requestAmount) {
					transactionList.add(requestStock.getValue());
					it.remove();
					offerStock.setStockAmount(stockAmount - requestAmount);
				}
				else {
					transactionList.add(requestStock.getValue());
					offersMap.delete(sellerId);
					requestStock.getValue().setStockAmount(requestAmount - stockAmount);
					offerStock.setStockAmount(stockAmount - requestAmount);

				}
			}
		}
		wait();
	}

	private synchronized Integer findSellerForStockRequest(Company request){
		for (Map.Entry<Integer, Company> offerStock : offersMap.entrySet()) {
			Company offer = offerStock.getValue();
			if(offer.getCompanyName().equals(request.getCompanyName()) && offer.getStockPrice() == request.getStockPrice()) {
				return offerStock.getKey();
			}
		}
		return -1;
	}

}
