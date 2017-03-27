package com.lnicf.PortMiner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	public static void log(String input, String type) {
		// Could use something better to log, but this works well.
		// Initialise file variable.
		File logFile = null;

		// Set logFile variable, dependent on OS.
		if (Multi.getOS() == "WINDOWS") {
			logFile = new File("portminer.log");
		} else if (Multi.getOS() == "LINUX") {
			logFile = new File(Multi.linHome() + "/PortMiner/portminerlog.log");
			if (!logFile.exists()) {
				File pmDir = new File(Multi.linHome() + "/PortMiner");
				pmDir.mkdir();
			}
		} else {
			System.out.println(
					"Unknown or unsupported operating system. File logging disabled, command line output only.");
			System.out.println("[" + Multi.currentTime() + "][" + type + "]" + input);
			return;
		}

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

		if (type == "info") {
			System.out.println("[" + Multi.currentTime() + "][INFO] " + input);

			try {
				bWriter.write("[" + Multi.currentTime() + "][INFO] " + input + "\n");
			} catch (IOException e) {
				System.out.println("Exception while writing to logging file.");
				e.printStackTrace();
				System.exit(1);
			}
		} else if (type == "warn") {
			System.out.println("[" + Multi.currentTime() + "][WARN] " + input);

			try {
				bWriter.write("[" + Multi.currentTime() + "][WARN] " + input + "\n");
			} catch (IOException e) {
				System.out.println("Exception while writing to logging file.");
				e.printStackTrace();
				System.exit(1);
			}
		} else if (type == "error") {
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
