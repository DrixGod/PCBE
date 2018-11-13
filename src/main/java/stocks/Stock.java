package stocks;

import buyers.Buyers;
import companies.Company;
import utils.ConcurrentMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/*
Clasa Stock se ocupa de adaugare de cereri/oferte, gasirea cererilor si ofertelor care se potrivesc si realizarea de vanzari efective
Folosirea metodelor synchronized:
    Metodele synchronized folosesc o strategie care nu permite interferente intre threaduri si erori de memorie.
    Daca un obiect este visibil mai multor threaduri, orice scriere/citire asupra acelui obiect se face prin metode synchronized
    Fara utilizarea cuvantului cheie un thread nu ar putea vedea schimbarile facute de un alt thread asupra unui obiect pe care il folosesc amandoua threadurile.

In cazul de fata Bursa, cumparatorii cat si vanzatorii trebuie sa poata vedea toate schimbarile care apar pentru a putea realiza schimburi in timp real.
 */
public class Stock {

    private volatile ConcurrentMap<Integer, Company> requestsMap;
    private volatile ConcurrentMap<Integer, Company> offersMap;
    private static volatile ArrayList<Company> transactionList;
    private volatile boolean newOfferOrRequest = false;

    public Stock() {
        requestsMap = new ConcurrentMap<Integer, Company>();
        offersMap = new ConcurrentMap<Integer, Company>();
        transactionList = new ArrayList<Company>();
    }

    //Folosim notifyAll() pentru a alerta toate threadurile de adaugarea unei oferte
    public synchronized void addAnOffer(int sellerID, Company stock) {
        System.out.println("Seller " + sellerID + " added " + stock);
        offersMap.put(sellerID, stock);
        newOfferOrRequest = true;
        notifyAll();
    }

    //Folosim notifyAll() pentru a alerta toate threadurile de adaugarea unei cereri
    public synchronized void addARequest(int buyerID, Company request) {
        System.out.println("Buyer " + buyerID + " tries to buy " + request);
        requestsMap.put(buyerID, request);
        newOfferOrRequest = true;
        notifyAll();
    }

    /*
    Metoda folosita pentru a cauta cereri si oferte care corespund pentru a realiza o vanzare de stockuri
     */
    public void lookForMatchingStock(){

        /*System.out.println("Current Offers. \n" + offersMap);
        System.out.println("Current Requests: \n" + requestsMap);
        System.out.println("Current Transactions list: \n" + transactionList);*/

        synchronized (this) {
            Iterator it = requestsMap.entrySet().iterator(); //Luam toate cererile curente.

            while (it.hasNext()) {
                Map.Entry<Integer, Company> requestStock = (Map.Entry<Integer, Company>) it.next();

                int sellerId = findSellerForStockRequest(requestStock.getValue());

                if (sellerId != -1) {
                    Company offerStock = offersMap.get(sellerId);
                    int stockAmount = offerStock.getStockAmount();
                    int requestAmount = requestStock.getValue().getStockAmount();
                    //Daca numarul de actiuni din cerere este egal cu cel din oferta vom face vanzarea si vom sterge oferta respectiva deoarece nu vor mai exista actiuni in ea
                    if (stockAmount == requestAmount) {
                        addTransaction(offerStock);
                        offersMap.delete(sellerId);
                        it.remove();
                        System.out.println("Buyer " + requestStock.getKey() + " bought " + stockAmount + " stocks from " + offerStock.getCompanyName() + " at " + offerStock.getStockPrice() + "\n");

                        //Daca numarul de actiuni din cerere este mai mic decat cel din oferta vom face vanzarea si vom substrage numarul de actiuni vandute din oferta
                    } else if (stockAmount > requestAmount) {
                        addTransaction(requestStock.getValue());
                        it.remove();
                        offerStock.setStockAmount(stockAmount - requestAmount);
                        System.out.println("Buyer " + requestStock.getKey() + " bought " + stockAmount + " stocks from " + offerStock.getCompanyName() + " at " + offerStock.getStockPrice() + "\n");
                    }

                    //Daca numarul de actiuni din cerere este mai mare decat cel din oferta vom face vanzarea si vom substrage numarul de actiuni vandute din cerere
                    else {
                        addTransaction(requestStock.getValue());
                        offersMap.delete(sellerId);
                        requestStock.getValue().setStockAmount(requestAmount - stockAmount);
                        offerStock.setStockAmount(stockAmount - requestAmount);
                        System.out.println("Buyer " + requestStock.getKey() + " bought " + stockAmount + " stocks from " + offerStock.getCompanyName() + " at " + offerStock.getStockPrice() + "\n");
                    }
                }
            }
            newOfferOrRequest = false;
            while (!newOfferOrRequest) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /*
    Metoda folosita pentru a verifica daca cererea si oferta corespund.
    Trimitem o cerere ca si parametru si iteram prin toate ofertele active pentru a vedea daca exista un match.
     */
    private synchronized Integer findSellerForStockRequest(Company request){
        for (Map.Entry<Integer, Company> offerStock : offersMap.entrySet()) {
            Company offer = offerStock.getValue();
            if(offer.getCompanyName().equals(request.getCompanyName()) && offer.getStockPrice() == request.getStockPrice()) {
                return offerStock.getKey();
            }
        }
        return -1;
    }

    public ArrayList<Company> getTransactionList() {
        return transactionList;
    }

    private synchronized void addTransaction(Company company){
        transactionList.add(company);
    }
}