package com.lnicf.PortMiner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	public enum LogType {
		INFO,
		WARN,
		ERROR
	}
	
	public static void log(String input, LogType type) {
		// Could use something better to log, but this works well.
		// Initialise file variable.
		File logFile = new File(Multi.workingDir() + "/portminer.log");

		// Check if the logfile exists.
		if (!logFile.exists()) {
			try {
				// Create if it doesn't.
				logFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Exception while creating logging file.");
				e.printStackTrace();
				System.exit(1);
			}
		}

		// Initialise FileWriter and BufferedWriter.
		FileWriter fileW = null;
		BufferedWriter bWriter = null;

		try {
			// Try to write to the logfile.
			fileW = new FileWriter(logFile, true);
			bWriter = new BufferedWriter(fileW);
		} catch (IOException e) {
			// Oh no!
			System.out.println("Exception while writing to logging file.");
			e.printStackTrace();
			System.exit(1);
		}

		// Get argument type and process it, then print to console.

		if (type == LogType.INFO) {
			System.out.println("[" + Multi.currentTime() + "][INFO] " + input);

			try {
				bWriter.write("[" + Multi.currentTime() + "][INFO] " + input + "\n");
			} catch (IOException e) {
				System.out.println("Exception while writing to logging file.");
				e.printStackTrace();
				System.exit(1);
			}
		} else if (type == LogType.WARN) {
			System.out.println("[" + Multi.currentTime() + "][WARN] " + input);

			try {
				bWriter.write("[" + Multi.currentTime() + "][WARN] " + input + "\n");
			} catch (IOException e) {
				System.out.println("Exception while writing to logging file.");
				e.printStackTrace();
				System.exit(1);
			}
		} else if (type == LogType.ERROR) {
			System.out.println("[" + Multi.currentTime() + "][ERROR] " + input);

			try {
				bWriter.write("[" + Multi.currentTime() + "][ERROR] " + input + "\n");
			} catch (IOException e) {
				System.out.println("Exception while writing to logging file.");
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			System.out.println("[" + Multi.currentTime() + "][UNKNOWN] " + input);

			try {
				bWriter.write("[" + Multi.currentTime() + "][UNKNOWN] " + input + "\n");
			} catch (IOException e) {
				System.out.println("Exception while writing to logging file.");
				e.printStackTrace();
				System.exit(1);
			}
		}

		try {
			// Close the BufferedWriter.
			bWriter.close();
		} catch (IOException e) {
			System.out.println("Exception while closing BufferedWriter.");
			e.printStackTrace();
			System.exit(1);
		}

	}

}
