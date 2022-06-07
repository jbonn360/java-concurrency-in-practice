package page_91;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

// happy face
public class FileCrawler implements Runnable {
	private final BlockingQueue<File> fileQueue;
	private final FileFilter fileFilter;
	private final File root;

	public FileCrawler(final BlockingQueue<File> fileQueue, final FileFilter fileFilter, final File root) {
		this.fileQueue = fileQueue;
		this.fileFilter = fileFilter;
		this.root = root;
	}

	public void run() {
		try {
			crawl(root);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void crawl(File root) throws InterruptedException {
		File[] entries = root.listFiles(fileFilter);
		if (entries != null) {
			for (File entry : entries) {
				if (entry.isDirectory())
					crawl(entry);
				else if (!alreadyIndexed(entry.getAbsolutePath())) {
					System.out.printf(Main.ANSI_RED + "[" + Thread.currentThread().getId() + "] Putting file in queue %s...\n", entry.getAbsolutePath());
					fileQueue.put(entry);
					Thread.sleep(1000l); // sleep to wait for the slow human to catch up
				}
			}
		}
	}

	private boolean alreadyIndexed(String absolutePath) {
		return false;
	}
}
