package com.lnicf.PortMiner;

import javax.swing.JOptionPane;

public class NoServer {
	public static void run() {
		Logger.log("Starting PortMiner without running Minecraft server.", "info");
		JOptionPane.showMessageDialog(null,
				"Please note that when running PortMiner without a Minecraft server, you !MUST! tell PortMiner to close the port!\nContinue at your own risk.",
				"Warning", JOptionPane.WARNING_MESSAGE);
		int port = Integer.parseInt(PropParse.getProperty("port"));
		String protocol = PropParse.getProperty("run-server-false-protocol");
		Logger.log("Opening port " + port, "info");
		PortManager.openPort(Multi.internalIP(), port, "Port Miner", protocol);
		JOptionPane.showMessageDialog(null, "Press OK to close the port when you are done.", "PortMiner",
				JOptionPane.INFORMATION_MESSAGE);
		Logger.log("Closing port " + port, "info");
		PortManager.closePort();
		PropParse.setProperty("safe-close", "true");
		System.exit(0);
	}
}
