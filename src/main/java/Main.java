
import sellers.Sellers;
import buyers.Buyers;
import companies.Company;
import transactions.Transaction;
import stocks.StockManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //Liste de threaduri pentru cumparatori si vanzatori
        ArrayList<Thread> sellers = new ArrayList<>();
        ArrayList<Thread> buyers = new ArrayList<>();

        //Obiect pentru managerul de bursa
        StockManager manager = new StockManager();

        Transaction stock = new Transaction(manager);

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

        Thread stockThread = new Thread(stock);
        stockThread.start();

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
            Thread.sleep(10000);

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

            stockThread.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Company> transactions = manager.getTransactionList();

        System.out.println("Transaction list contains " + transactions.size() + " items.\nThe sold stocks are:\n" + transactions);
    }

}

/*

Cazuri de concurenta care ar putea sa apara:

- bursa detine lock-ul pentru StockManager si asteapta sa apara un vanzator si un cumparator, insa acestia nu pot
scrie ofertele/cererile pentru ca nu au lock-ul
- bursa gaseste o potrivire intre cerere si oferta, insa in acel moment cererea sau oferta se modifica
-

 */