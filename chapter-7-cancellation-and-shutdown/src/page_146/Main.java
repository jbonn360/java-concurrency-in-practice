package page_146;

import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		Runnable toRun = new Runnable() {			
			@Override
			public void run() {
				for(int i = 0; i < 10000000; i++) {
					System.out.println(i);
					if(Thread.currentThread().isInterrupted())
						throw new Error("Thread interrupted...");							
				}			
			}
		};
		
		try {
			TimedRun.timedRun(toRun, 2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
