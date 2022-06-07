package page_91;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

//happy face
public class Indexer implements Runnable {
	private final BlockingQueue<File> queue;
	private final HashMap<String, Long> indexMap;

	public Indexer(BlockingQueue<File> queue) {
		this.queue = queue;
		indexMap = new HashMap<String, Long>();
	}

	public void run() {
		try {
			while (true) {
				indexFile(queue.take());

				if (queue.isEmpty()) {
					System.out.printf(
							Main.ANSI_RESET + "[" + Thread.currentThread().getId()
									+ "] Queue is empty, time elapsed: %d seconds\n",
							Benchmarker.endTimer().toSeconds());
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isIndexed(String path) {
		return indexMap.containsKey(path);
	}

	private void indexFile(File file) throws IOException, InterruptedException {
		System.out.printf(Main.ANSI_GREEN + "[" + Thread.currentThread().getId() + "] Indexing file %s...\n",
				file.getAbsoluteFile());
		indexMap.put(file.getAbsolutePath(), getCreationDateTime(file));

		// blocking thread with actual work to simulate a lengthy task
		// has the advantage (over Thread.sleep) of actually registering thread load on
		// an OS CPU monitoring tool
		Benchmarker.blockThreadWithWork();
	}

	private long getCreationDateTime(File file) throws IOException {
		return Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().to(TimeUnit.MILLISECONDS);
	}
}
