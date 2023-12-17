package log;

import java.io.FileWriter;
import java.io.IOException;

public class Log {
	private static final String DEFAULT_LOG_FILE_NAME = "log.txt";
	
	// the string storing the message to be stored
	private StringBuffer message;
	
	private String filename;
	
	public Log (String filename) {
		this.filename = filename;
		this.message = new StringBuffer();
	}
	
	public Log () {
		this(DEFAULT_LOG_FILE_NAME);
	}
	
	public static String getDefaultLogFileName () {
		return Log.DEFAULT_LOG_FILE_NAME;
	}
	
	public void clear () {
		this.message = new StringBuffer();
	}
	
	public void addToMessage (String newMessage) {
		message.append(newMessage);
	}
	
	public void addToMessageln (String newMessage) {
		message.append(newMessage + "\n");
	}
	
	public void blankLines (int n) {
		for (int i = 0; i < n; i++) {
			message.append("\n");
		}
	}
	
	public void writeInFile () {
		try {
	      FileWriter myWriter = new FileWriter(this.filename);
	      myWriter.write(this.message.toString());
	      this.clear();
	      myWriter.close();
	    }
		catch (IOException e) {
	      System.out.println("Error in writing log file");
	      e.printStackTrace();
	    }
	}
}
