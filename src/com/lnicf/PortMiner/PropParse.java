package com.lnicf.PortMiner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropParse {
    public static void checkPropFile() {
        // Initialise File variable.
        File propFile = null;

        // Set propFile location, dependent on OS.
        if (Multi.getOS() == "WINDOWS") {
            propFile = new File("portminer.properties");
        } else if (Multi.getOS() == "LINUX") {
            propFile = new File(Multi.linHome() + "/PortMiner/portminer.properties");
        } else {
            Logger.log("Unknown OS for properties!", "warn");
            propFile = new File("portminer.properties");
        }
        
        // Initialise OutputStream, and Properties variables.
        OutputStream output = null;
        Properties prop = new Properties();

        // If the properties file does not exist, create it, set properties, and save.
        if (!propFile.exists()) {
            Logger.log("Properties file does not exist. Attempting to create.", "info");
            try {
                if (Multi.getOS() == "WINDOWS") {
                    output = new FileOutputStream("portminer.properties");
                } else if (Multi.getOS() == "LINUX") {
                    output = new FileOutputStream(Multi.linHome() + "/PortMiner/portminer.properties");
                } else {
                    output = new FileOutputStream("portminer.properties");
                }

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
                prop.store(output, null);
            } catch (IOException e) {
                Logger.log("Exception while creating properties file", "error");
                e.printStackTrace();
                System.exit(1);
            }

            // Close the output stream.
            try {
                output.close();
            } catch (IOException e) {
                Logger.log("Exception in PropParse", "error");
                e.printStackTrace();
                System.exit(1);
            }
            Logger.log("Done", "info");
        }
    }

    public static String getProperty(String property) {
        // Call the function above.
        PropParse.checkPropFile();
        Logger.log("Getting property: " + property, "info");
        
        // Initialise Properties and InputStream variables.
        Properties prop = new Properties();
        InputStream input = null;

        // Set InputStream from FileInputStream location, dependent on OS.
        try {
            if (Multi.getOS() == "WINDOWS") {
                input = new FileInputStream("portminer.properties");
            } else if (Multi.getOS() == "LINUX") {
                input = new FileInputStream(Multi.linHome() + "/PortMiner/portminer.properties");
            } else {
                input = new FileInputStream("portminer.properties");
            }

            // Load current properties file.
            prop.load(input);

            Logger.log("Found value: " + prop.getProperty(property), "info");
            // Get the property.
            return prop.getProperty(property);
        } catch (IOException e) {
            Logger.log("Exception while trying to get property!", "error");
            e.printStackTrace();
            System.exit(1);
        }

        return "";
    }

    public static void setProperty(String property, String value) {
        // Call checkPropFile function in this class.
        PropParse.checkPropFile();
        Logger.log("Setting property: " + property + " to " + value, "info");
        // Initialise Properties, InputStream and OutputStream variables.
        Properties prop = new Properties();
        InputStream input = null;
        OutputStream output = null;

        // Set 'em.
        try {
            if (Multi.getOS() == "WINDOWS") {
                input = new FileInputStream("portminer.properties");
                prop.load(input);
                input.close();
                output = new FileOutputStream("portminer.properties");
            } else if (Multi.getOS() == "LINUX") {
                input = new FileInputStream(Multi.linHome() + "/PortMiner/portminer.properties");
                prop.load(input);
                input.close();
                output = new FileOutputStream(Multi.linHome() + "/PortMiner/portminer.properties");
            } else {
                input = new FileInputStream("portminer.properties");
                prop.load(input);
                input.close();
                output = new FileOutputStream("portminer.properties");
            }
            
            // Set and store the property, then close the OutputStream.
            prop.setProperty(property, value);
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            Logger.log("Exception while trying to get property!", "error");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
