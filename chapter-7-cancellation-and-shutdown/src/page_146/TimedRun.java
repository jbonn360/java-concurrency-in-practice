package page_146;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// skeptical face
public class TimedRun {
	static ScheduledThreadPoolExecutor cancelExec = new ScheduledThreadPoolExecutor(0);

	public static void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException {
		class RethrowableTask implements Runnable {
			private volatile Throwable t;

			public void run() {
				try {
					r.run();
				} catch (Throwable t) {
					this.t = t;
				}
			}

			void rethrow() {
				if (t != null)
					throw launderThrowable(t);
			}
		}

		RethrowableTask task = new RethrowableTask();
		final Thread taskThread = new Thread(task);
		taskThread.start();
		cancelExec.schedule(new Runnable() {
			@Override
			public void run() {
				taskThread.interrupt();
			}
		}, timeout, unit);
		taskThread.join(unit.toMillis(timeout));
		task.rethrow();
	}

	public static RuntimeException launderThrowable(Throwable t) {
		if (t instanceof RuntimeException)
			return (RuntimeException) t;
		else if (t instanceof Error)
			throw (Error) t;
		else
			throw new IllegalStateException("Not unchecked", t);
	}
}
