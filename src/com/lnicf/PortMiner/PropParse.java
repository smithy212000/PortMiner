package com.lnicf.PortMiner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.lnicf.PortMiner.Logger.LogType;

public class PropParse {
	
	static File propFile = new File(Multi.workingDir() + "/portminer.properties");
	
	public static void checkPropFile() {

		// Initialise OutputStream, and Properties variables.
		OutputStream output = null;
		Properties prop = new Properties();

		// If the properties file does not exist, create it, set properties, and
		// save.
		if (!propFile.exists()) {
			Logger.log("Properties file does not exist. Attempting to create.", LogType.INFO);
			try {
				output = new FileOutputStream(propFile.toString());
				prop.setProperty("config-version", Double.toString(PortMiner.configVersion));
				prop.setProperty("find-ip", "true");
				prop.setProperty("internal-ip", "0.0.0.0");
				prop.setProperty("port", "25565");
				prop.setProperty("server-jar", "minecraft_server.jar");
				prop.setProperty("xms", "256");
				prop.setProperty("xmx", "1024");
				prop.setProperty("maxpermsize", "64");
				prop.setProperty("safe-close", "true");
				prop.setProperty("run-server", "true");
				prop.setProperty("run-server-false-protocol", "TCP");
				prop.setProperty("plugin-support", "false");
				prop.setProperty("plugin-port", "8080");
				prop.setProperty("plugin-protocol", "TCP");
				prop.store(output, null);
			} catch (IOException e) {
				Logger.log("Exception while creating properties file", LogType.ERROR);
				e.printStackTrace();
				System.exit(1);
			}

			// Close the output stream.
			try {
				output.close();
			} catch (IOException e) {
				Logger.log("Exception in PropParse", LogType.ERROR);
				e.printStackTrace();
				System.exit(1);
			}
			Logger.log("Done", LogType.INFO);
		}
	}

	public static String getProperty(String property) {
		// Call the function above.
		PropParse.checkPropFile();
		Logger.log("Getting property: " + property, LogType.INFO);

		// Initialise Properties and InputStream variables.
		Properties prop = new Properties();
		InputStream input = null;

		// Set InputStream from FileInputStream location, dependent on OS.
		try {
			input = new FileInputStream(propFile.toString());
			prop.load(input);
			Logger.log("Found value: " + prop.getProperty(property), LogType.INFO);
			// Get the property.
			return prop.getProperty(property);
		} catch (IOException e) {
			Logger.log("Exception while trying to get property!", LogType.ERROR);
			e.printStackTrace();
			System.exit(1);
		}

		return "";
	}

	public static void setProperty(String property, String value) {
		// Call checkPropFile function in this class.
		PropParse.checkPropFile();
		Logger.log("Setting property: " + property + " to " + value, LogType.INFO);
		// Initialise Properties, InputStream and OutputStream variables.
		Properties prop = new Properties();
		InputStream input = null;
		OutputStream output = null;

		// Set 'em.
		try {
			input = new FileInputStream(propFile.toString());
			prop.load(input);
			input.close();
			output = new FileOutputStream(propFile.toString());

			// Set and store the property, then close the OutputStream.
			prop.setProperty(property, value);
			prop.store(output, null);
			output.close();
		} catch (IOException e) {
			Logger.log("Exception while trying to get property!", LogType.ERROR);
			e.printStackTrace();
			System.exit(1);
		}
	}
}
