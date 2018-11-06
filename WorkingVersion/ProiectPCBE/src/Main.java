
public class Main {
	public static int randomWithRange(int min, int max)
	{
	   int range = Math.abs(max - min) + 1;     
	   return (int)(Math.random() * range) + (min <= max ? min : max);
	}

	public static void main(String[] args) {
		StockManager manager = new StockManager();
		Sellers seller1 = new Sellers("Nokia", manager);
		Sellers seller2 = new Sellers("Hella", manager);
		Sellers seller3 = new Sellers("Conti", manager);
		
		Buyers buyer1 = new Buyers("Nokia", manager);
		Buyers buyer2 = new Buyers("Hella", manager);
		Buyers buyer3 = new Buyers("Conti", manager);

		Thread s1 = new Thread(seller1);
		Thread s2 = new Thread(seller2);
		Thread s3 = new Thread(seller3);
		
		Thread b1 = new Thread(buyer1);
		Thread b2 = new Thread(buyer2);
		Thread b3 = new Thread(buyer3);
		
		s1.start();
		s2.start();
		s3.start();
		
		b1.start();
		b2.start();
		b3.start();
		
		try {
			s1.join();
			s2.join();
			s3.join();
			b1.join();
			b2.join();
			b3.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(buyer1);
		System.out.println(buyer2);
		System.out.println(buyer3);
		
	}

}
