package com.lnicf.PortMiner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.lnicf.PortMiner.Logger.LogType;

/*
PortMiner is created by smithy212000 (LNICF).
Requires cling-core, cling-support, seamless-http and seamless-util libraries all
available from http://4thline.org/projects/cling/

This software is 75% thanks to Russian Standard :)
*/

public class PortMiner {
	// Main class.
	public static final double softVersion = 5.4;
	public static final double configVersion = 1.2;

	public static void main(String[] args) throws InterruptedException {
		// Initialise all the variables.
		int port = 0;
		File serverJar = null;
		String xms = null;
		String xmx = null;
		String maxpermsize = null;
		boolean safeClose = false;
		boolean runServer = true;
		boolean pluginSupport = false;
		int pluginPort = 0;
		String pluginProtocol = null;
		String sServerJar = null;
		String protocol = null;
		File eulaText = null;
		
		// Write cling output to file
		try {
			System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream(Multi.workingDir() + "/portminer-upnp.log"))));
		} catch (FileNotFoundException e1) {
			Logger.log("Could not redirect error stream output to file", LogType.WARN);
			e1.printStackTrace();
		}
		
		if (Multi.getOS() == OSType.Windows) {
			eulaText = new File("eula.txt");
		} else if (Multi.getOS() == OSType.Linux) {
			eulaText = new File(Multi.workingDir() + "/eula.txt");
		} else {
			eulaText = new File("eula.txt");
		}

		// If the operating system is unknown or OSX, display message.
		if (Multi.getOS() == OSType.Unknown || Multi.getOS() == OSType.Mac) {
			Logger.log("Unsupported operating system", LogType.WARN);
			JOptionPane.showMessageDialog(null,
					"Your operating system is currently not supported by PortMiner.\nContinue at your own risk.",
					"Unsupported OS", JOptionPane.WARNING_MESSAGE);
			
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"Do you want to run Portminer in manual mode? This will increase the chances of success."
					+ "\nNOTE: You will need to start the server manually.", "Unsupported OS",
					JOptionPane.YES_NO_OPTION);
			
			if (dialogResult == JOptionPane.YES_OPTION) {
				NoServer.run();
			} else {
				Logger.log("Continuing with Unknown OS type", LogType.WARN);
			}
		}

		if (Double.parseDouble(PropParse.getProperty("config-version")) != configVersion) {
			Logger.log("Configuartion out of date", LogType.ERROR);
			JOptionPane.showMessageDialog(null,
					"Your configuration is out of date, please delete it and reload PortMiner.",
					"Outdated configuration", JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}

		Logger.log("PortMiner uses LGPL software from https://github.com/4thline", LogType.INFO);
		Logger.log("Made with thanks to Russian Standard :)", LogType.INFO);
		
		// Runtime information
		Logger.log("Operating System: " + System.getProperty("os.name") + ", Arch: " + System.getProperty("os.arch"), LogType.INFO);
		Logger.log("Java Version: " + System.getProperty("java.version"), LogType.INFO);

		// Setup JFrame for progress bar and file selector, call update checker.
		Progress.setupFrame();
		Progress.setProgress("Checking for updates...", 5);
		Updater.checkForUpdates();

		// Parse configuration.
		try {
			Progress.setProgress("Getting property port...", 6);
			port = Integer.parseInt(PropParse.getProperty("port"));
			Progress.setProgress("Getting property server-jar", 8);
			sServerJar = PropParse.getProperty("server-jar");
			Progress.setProgress("Getting property server-jar...", 10);
			serverJar = new File(sServerJar);
			System.out.println(serverJar.toString());
			Progress.setProgress("Getting property xms...", 12);
			xms = PropParse.getProperty("xms");
			Progress.setProgress("Getting property xmx...", 14);
			xmx = PropParse.getProperty("xmx");
			Progress.setProgress("Getting property plugin-support...", 15);
			pluginSupport = Boolean.parseBoolean(PropParse.getProperty("plugin-support"));
			Progress.setProgress("Getting property maxpermsize...", 16);
			maxpermsize = PropParse.getProperty("maxpermsize");
			Progress.setProgress("Getting property plugin-port...", 17);
			pluginPort = Integer.parseInt(PropParse.getProperty("plugin-port"));
			Progress.setProgress("Getting property safe-close...", 18);
			safeClose = Boolean.parseBoolean(PropParse.getProperty("safe-close"));
			Progress.setProgress("Getting property plugin-protocol...", 19);
			pluginProtocol = PropParse.getProperty("plugin-protocol");
			Progress.setProgress("Getting property run-server...", 20);
			runServer = Boolean.parseBoolean(PropParse.getProperty("run-server"));
			Progress.setProgress("Getting property run-server-false-protocol", 22);
			protocol = PropParse.getProperty("run-server-false-protocol");
			if (protocol.equals("TCP") || protocol.equals("UDP") || protocol.equals("UDP_TCP")) {
				
			} else {
				Logger.log("Exception while getting properties.", LogType.ERROR);
				JOptionPane.showMessageDialog(null,
						"PortMiner had an error reading the configuration.\nTry deleting it, and restarting PortMiner.",
						"Failure reading configuration", JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}
		} catch (NumberFormatException e) {
			Logger.log("Exception while getting properties.", LogType.ERROR);
			JOptionPane.showMessageDialog(null,
					"PortMiner had an error reading the configuration.\nTry deleting it, and restarting PortMiner.",
					"Failure reading configuration", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		}

		// Check if PortMiner closed correctly.
		if (safeClose == false) {
			Logger.log("PortMiner did not close correctly.", LogType.WARN);
			JOptionPane.showMessageDialog(null,
					"PortMiner did not close correctly.\nIf this issue continues, please report it.", "Unsafe close",
					JOptionPane.WARNING_MESSAGE);
		} else {
			PropParse.setProperty("safe-close", "false");
		}

		if (runServer == false) {
			Progress.closeFrame();
			NoServer.run();
		}

		// Check for server jar file defined in configuration. If not found, ask
		// user to select it.
		Progress.setProgress("Checking for server jar...", 25);
		if (!serverJar.exists()) {
			Logger.log("Server jar does not exist.", LogType.WARN);
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(null,
					"PortMiner could not find " + serverJar + ".\nDo you want to select the server jar?", "Server Jar",
					dialogButton);

			if (dialogResult == JOptionPane.YES_OPTION) {
				Logger.log("Setting jar file.", LogType.INFO);
				JFileChooser fChoose = new JFileChooser();
				fChoose.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fChoose.showOpenDialog(Progress.frame);

				if (result == JFileChooser.APPROVE_OPTION) {
					serverJar = fChoose.getSelectedFile();
					sServerJar = serverJar.getPath().toString();
					PropParse.setProperty("server-jar", sServerJar);
				} else {
					Logger.log("No jar file selected.", LogType.INFO);
					System.exit(0);
				}

			} else {
				Logger.log("No jar selected. Closing.", LogType.INFO);
				System.exit(0);
			}
		}

		// Check if EULA exists.
		Progress.setProgress("Checking for EULA...", 40);
		if (!eulaText.exists()) {
			Logger.log("Prompting EULA.", LogType.INFO);
			int dialogButtonB = JOptionPane.YES_NO_OPTION;
			int dialogResultB = JOptionPane.showConfirmDialog(null,
					"Do you wish to agree to Mojangs EULA at\nhttps://account.mojang.com/documents/minecraft_eula",
					"EULA", dialogButtonB);

			if (dialogResultB == JOptionPane.YES_OPTION) {

				PrintWriter writer = null;

				try {
					if (Multi.getOS() == OSType.Windows) {
						writer = new PrintWriter("eula.txt", "UTF-8");
					} else if (Multi.getOS() == OSType.Linux) {
						writer = new PrintWriter(Multi.workingDir() + "/eula.txt", "UTF-8");
					} else {
						writer = new PrintWriter("eula.txt", "UTF-8");
					}

					writer.println("#Autogenerated by PortMiner at " + System.currentTimeMillis() + "ms.");
					writer.println("eula=true");
					writer.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					Logger.log("Error creating eula.txt", LogType.ERROR);
					e.printStackTrace();
					System.exit(1);
				}

			} else {
				Logger.log("User declined EULA.", LogType.INFO);
				System.exit(0);
			}
		}

		// Open the port(s).
		Logger.log("Internal IP is " + Multi.internalIP(), LogType.INFO);
		Logger.log("Opening port " + port, LogType.INFO);
		Progress.setProgress("Attempting to open port " + port, 65);
		PortManager.openPort(Multi.internalIP(), port, "Minecraft Server", "TCP");
		if (pluginSupport) {
			Logger.log("Plugin support enabled in configuration, attempting to open port " + pluginPort, LogType.INFO);
			Progress.setProgress("Attempting to open port " + pluginPort, 70);
			PortManager.openPortPlugin(Multi.internalIP(), pluginPort, "Minecraft Server Plugin", pluginProtocol);
		}

		// Set up process and runtime.
		Progress.setProgress("Setting up process and runtime...", 75);
		Runtime runTime = Runtime.getRuntime();
		String[] toRun = null;

		// Create runTime parameters.
		if (Multi.getOS() == OSType.Windows) {
			toRun = new String[] { "cmd", "/c", "start", "/WAIT", "java.exe", "-Xms" + xms + "M",
					"-Xmx" + xmx + "M", "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, "nogui" };
		} else if (Multi.getOS() == OSType.Linux) {
			toRun = new String[] { "xterm", "-e", "java", "-Xms" + xms + "M", "-Xmx" + xmx + "M",
					"-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, "nogui" };
		} else {
			toRun = new String[] { "cmd", "/c", "start", "/WAIT", "java.exe", "-Xms" + xms + "M",
					"-Xmx" + xmx + "M", "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, "nogui" };
		}

		// Check that xterm is installed for linux users
		if (Multi.getOS() == OSType.Linux) {
			try {
				runTime.exec(new String[] {"xterm", "-e", "exit"});
			} catch (Exception e) {
				Logger.log("xterm is not installed!", LogType.ERROR);
				e.printStackTrace();
				Thread.sleep(1000);
				Logger.log("Closing port " + port, LogType.INFO);
				PortManager.closePort();
				JOptionPane.showMessageDialog(null, "xterm is required for Portminer to run. Please install it and try again.\nPort "
						+ port + " has been closed as the server failed to start.", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
		
		Process proc = null;

		// Start the server.
		Progress.setProgress("Starting process...", 80);
		Logger.log("Starting process with: Xms="+xms+"M, Xmx="+xmx+"M, MaxPermSize="+maxpermsize+"M", LogType.INFO);
		try {
			if (Multi.getOS() == OSType.Windows) {
				proc = runTime.exec(toRun);
			} else if (Multi.getOS() == OSType.Linux) {
				File serverDir = Multi.workingDir();
				proc = runTime.exec(toRun, null, serverDir);
			} else {
				proc = runTime.exec(toRun);
			}
		} catch (Exception e) {
			Logger.log("Exception while creating or running process.", LogType.ERROR);
			e.printStackTrace();
			Thread.sleep(1000);
			Logger.log("Closing port " + port, LogType.INFO);
			PortManager.closePort();
			JOptionPane.showMessageDialog(null, "Error while starting the main server process. Check console.\nPort "
					+ port + " has been closed as the server failed to start.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		// Ta-daah!
		Progress.setProgress("Done!", 100);
		Thread.sleep(250);
		Progress.closeFrame();

		// Keep looping every 5 seconds, checking if the server is still
		// running.
		while (proc.isAlive() == true) {
			Thread.sleep(2500);
		}

		// Reopen progress bar frame. Close port(s) and exit.
		Progress.setupFrame();
		Progress.setProgress("Closing port " + port, 25);
		Logger.log("Closing port " + port, LogType.INFO);
		Thread.sleep(4000);
		PortManager.closePort();
		if (pluginSupport) {
			Progress.setProgress("Closing port " + pluginPort, 50);
			Logger.log("Closing port " + pluginPort, LogType.INFO);
			PortManager.closePortPlugin();
		}
		PropParse.setProperty("safe-close", "true");
		Progress.setProgress("Done!", 100);
		Thread.sleep(250);
		Progress.closeFrame();
		System.exit(0);
	}

}
