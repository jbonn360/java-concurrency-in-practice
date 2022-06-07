package page_91;

import java.time.Duration;
import java.time.Instant;

public class Benchmarker {
	private static Instant timerStart = null;

	public static void blockThreadWithWork() {
		long a = 0;
		for (int i = 1; i < Integer.MAX_VALUE; i++)
			for(int j = 1; j < 5; j++)
				a = j / j;	
	}

	public static void startTimer() {
		timerStart = Instant.now();
	}

	public static Duration endTimer() {
		if (timerStart != null)
			return Duration.between(timerStart, Instant.now());
		else
			throw new IllegalStateException("Timer not started!");
	}
}
