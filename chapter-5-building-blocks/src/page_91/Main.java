package page_91;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	
	private final static String PROJECT_PATH = System.getProperty("user.dir");
	private final static String PATH_TO_INDEX = "/resources/to-index/";
	
	private final static byte BOUND = 15;
	private final static byte N_CONSUMERS = 3;

	public static void main(String... args) {
		startIndexing(new File[] { new File(PROJECT_PATH.concat(PATH_TO_INDEX)) });
	}

	public static void startIndexing(File[] roots) {
		final BlockingQueue<File> fileQueue = new LinkedBlockingQueue<File>(BOUND);
		final FileFilter fileFilter = (File file) -> true;
		
		Benchmarker.startTimer();
		
		for(File root : roots)
			//new FileCrawler(fileQueue, fileFilter, root).run();
			new Thread(new FileCrawler(fileQueue, fileFilter, root)).start();
		
		for(byte i = 0; i < N_CONSUMERS; i++)
			//new Indexer(fileQueue).run();
			new Thread(new Indexer(fileQueue)).start();		
		
		//System.out.printf("Time taken is: %d seconds", Benchmarker.endTimer().toSeconds());
	}
}
