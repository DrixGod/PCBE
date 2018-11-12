import sellers.Sellers;
import buyers.Buyers;
import companies.Company;
import stocks.Stock;
import stocks.StockManager;

import java.util.ArrayList;

public class Main {
	public static int randomWithRange(int min, int max)
	{
	   int range = Math.abs(max - min) + 1;     
	   return (int)(Math.random() * range) + (min <= max ? min : max);
	}

	public static void main(String[] args) {
		
		ArrayList<Thread> sellers = new ArrayList<>();
		ArrayList<Thread> buyers = new ArrayList<>();
		
		StockManager stockManager = new StockManager();

		Stock stock = new Stock(stockManager);

		Sellers seller1 = new Sellers("Nokia", stockManager);
		Sellers seller2 = new Sellers("Hella", stockManager);
		Sellers seller3 = new Sellers("Conti", stockManager);

		Buyers buyer1 = new Buyers("A", stockManager);
		Buyers buyer2 = new Buyers("B", stockManager);
		Buyers buyer3 = new Buyers("C", stockManager);
		Buyers buyer4 = new Buyers("D", stockManager);
		Buyers buyer5 = new Buyers("E", stockManager);
		Buyers buyer6 = new Buyers("F", stockManager);

		Thread stockThread = new Thread(stock);

		Thread s1 = new Thread(seller1);
		sellers.add(s1);
		Thread s2 = new Thread(seller2);
		sellers.add(s2);
		Thread s3 = new Thread(seller3);
		sellers.add(s3);
		
		Thread b1 = new Thread(buyer1);
		buyers.add(b1);
		Thread b2 = new Thread(buyer2);
		buyers.add(b2);
		Thread b3 = new Thread(buyer3);
		buyers.add(b3);
		Thread b4 = new Thread(buyer4);
		buyers.add(b4);
		Thread b5 = new Thread(buyer5);
		buyers.add(b5);
		Thread b6 = new Thread(buyer6);
		buyers.add(b6);

		for(Thread seller : sellers) {
			seller.start();
		}
		
		for(Thread buyer : buyers) {
			buyer.start();
		}

        stockThread.start();

        try {
			for(Thread seller : sellers) {
				seller.join(randomWithRange(5,10) * 1000);
			}
			
			for(Thread buyer : buyers) {
				buyer.join(randomWithRange(5,10) * 1000);
			}
			
			for(Thread seller : sellers) {
				if(seller.isAlive()) {
					seller.interrupt();
				}
			}
			
			for(Thread buyer : buyers) {
				if(buyer.isAlive()) {
					buyer.interrupt();
				}
			}

			stockThread.interrupt();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//ArrayList<Company> transactions = stockManager.getTransactionList();
		
	   //System.out.println("Transaction list contains " + transactions.size() + "items.\nThe sold stocks are:\n" + transactions);
		
	}

}
