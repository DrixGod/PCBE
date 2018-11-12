import sellers.Sellers;
import buyers.Buyers;
import stocks.Stock;
import stocks.StockManager;

import java.util.ArrayList;

public class Main {

	private static int randomWithRange(int min, int max)
	{
	   int range = Math.abs(max - min) + 1;
	   return (int)(Math.random() * range) + (min <= max ? min : max);
	}

	public static void main(String[] args) {

		//Liste de threaduri pentru cumparatori si vanzatori
		ArrayList<Thread> sellers = new ArrayList<>();
		ArrayList<Thread> buyers = new ArrayList<>();

		//Obiect pentru managerul de bursa
		StockManager manager = new StockManager();

		//Initializare vanzatori
		Sellers seller1 = new Sellers("Nokia", manager);
		Sellers seller2 = new Sellers("Hella", manager);
		Sellers seller3 = new Sellers("Conti", manager);

		//Initializare cumparatori
		Buyers buyer1 = new Buyers("Nokia", manager);
		Buyers buyer2 = new Buyers("Hella", manager);
		Buyers buyer3 = new Buyers("Conti", manager);
		Buyers buyer4 = new Buyers("Nokia", manager);
		Buyers buyer5 = new Buyers("Hella", manager);
		Buyers buyer6 = new Buyers("Conti", manager);

		//Initializare threaduri de vanzatori
		Thread s1 = new Thread(seller1);
		Thread s2 = new Thread(seller2);
		Thread s3 = new Thread(seller3);

		//Adaugare threaduri in lista de vanzatori
		sellers.add(s1);
		sellers.add(s2);
		sellers.add(s3);

		//Initializare threaduri de cumparatori
		Thread b1 = new Thread(buyer1);
		Thread b2 = new Thread(buyer2);
		Thread b3 = new Thread(buyer3);
		Thread b4 = new Thread(buyer4);
		Thread b5 = new Thread(buyer5);
		Thread b6 = new Thread(buyer6);

		//Adaugare threaduri in lista de cumparatori
		buyers.add(b1);
		buyers.add(b2);
		buyers.add(b3);
		buyers.add(b4);
		buyers.add(b5);
		buyers.add(b6);

		//Pornire threaduri vanzare
		for(Thread seller : sellers) {
			seller.start();
		}

		//Pornire threaduri cumparare
		for(Thread buyer : buyers) {
			buyer.start();
		}
		
		try {
			/*
			Facem join pentru a astepta pana ce threadurile au terminat de rulat.
			Astfel metoda main va astepta pana ce lista de threaduri sellers si buyers vor fi terminate.
			 */
			for(Thread seller : sellers) {
				seller.join(randomWithRange(5,10)*1000);
			}
			
			for(Thread buyer : buyers) {
				buyer.join(randomWithRange(5,10)*1000);
			}
			
			for(Thread seller : sellers) {
				if(seller.isAlive()) { //verificare daca thread-ul inca ruleaza
					seller.interrupt(); //thread-ul este intrerupt
				}
			}
			
			for(Thread buyer : buyers) {
				if(buyer.isAlive()) {
					buyer.interrupt();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ArrayList<Stock> transactions = manager.getTransactionList();
		
		System.out.println("Transaction list contains " + transactions.size() + " items.\nThe sold stocks are:\n" + transactions);
		
	}

}