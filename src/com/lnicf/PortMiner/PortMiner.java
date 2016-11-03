package com.lnicf.PortMiner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
PortMiner is created by smithy212000 (LNICF).
Requires cling-core, cling-support, seamless-http and seamless-util libraries all
available from http://4thline.org/projects/cling/
*/

public class PortMiner {
    // Main class.
    public static final double softVersion = 4.8;
    public static final double configVersion = 1.1;

    public static void main(String[] args) throws InterruptedException {
        // Initialise all the variables.
        int port = 0;
        File serverJar = null;
        String xms = null;
        String xmx = null;
        String maxpermsize = null;
        boolean safeClose = false;
        boolean runServer = true;
        String sServerJar = null;
        String protocol = null;
        File fMinTTY = new File("bin/mintty.exe");
        File eulaText = null;
        if (Multi.getOS() == "WINDOWS") {
            eulaText = new File("eula.txt");
        } else if (Multi.getOS() == "LINUX") {
            eulaText = new File(Multi.linHome() + "/PortMiner/eula.txt");
        } else {
            eulaText = new File("eula.txt");
        }

        // If the operating system is unknown or OSX, display message.
        if (Multi.getOS() == "UNKNOWN" || Multi.getOS() == "MAC") {
            JOptionPane.showMessageDialog(null,
                    "Your operating system is currently not supported by PortMiner.\nContinue at your own risk.",
                    "Unsupported OS", JOptionPane.WARNING_MESSAGE);
        }

        if (Double.parseDouble(PropParse.getProperty("config-version")) != configVersion) {
            JOptionPane.showMessageDialog(null,
                    "Your configuration is out of date, please delete it and reload PortMiner.",
                    "Outdated configuration", JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }

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
            Progress.setProgress("Getting property maxpermsize...", 16);
            maxpermsize = PropParse.getProperty("maxpermsize");
            Progress.setProgress("Getting property safe-close...", 18);
            safeClose = Boolean.parseBoolean(PropParse.getProperty("safe-close"));
            Progress.setProgress("Getting property run-server...", 20);
            runServer = Boolean.parseBoolean(PropParse.getProperty("run-server"));
            Progress.setProgress("Getting property run-server-false-protocol", 22);
            protocol = PropParse.getProperty("run-server-false-protocol");
            if (protocol.equals("TCP") || protocol.equals("UDP")) {
                
            } else {
                Logger.log("Exception while getting properties.", "error");
                JOptionPane.showMessageDialog(null,
                        "PortMiner had an error reading the configuration.\nTry deleting it, and restarting PortMiner.",
                        "Failure reading configuration", JOptionPane.WARNING_MESSAGE);
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            Logger.log("Exception while getting properties.", "error");
            JOptionPane.showMessageDialog(null,
                    "PortMiner had an error reading the configuration.\nTry deleting it, and restarting PortMiner.",
                    "Failure reading configuration", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        // Check if PortMiner closed correctly.
        if (safeClose == false) {
            Logger.log("PortMiner did not close correctly.", "warn");
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
            Logger.log("Server jar does not exist.", "warn");
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null,
                    "PortMiner could not find " + serverJar + ".\nDo you want to select the server jar?", "Server Jar",
                    dialogButton);

            if (dialogResult == JOptionPane.YES_OPTION) {
                Logger.log("Setting jar file.", "info");
                JFileChooser fChoose = new JFileChooser();
                fChoose.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fChoose.showOpenDialog(Progress.frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    serverJar = fChoose.getSelectedFile();
                    sServerJar = serverJar.getPath().toString();
                    PropParse.setProperty("server-jar", sServerJar);
                } else {
                    Logger.log("No jar file selected.", "info");
                    System.exit(0);
                }

            } else {
                Logger.log("No jar selected. Closing.", "info");
                System.exit(0);
            }
        }
            
            // Check for MinTTY if running Windows.
            if (Multi.getOS() == "WINDOWS") {
                if (!fMinTTY.exists()) {
                    Logger.log("MinTTY was not found.", "error");
                    JOptionPane.showMessageDialog(null, "MinTTY was not found. PortMiner can not run.", "No MinTTY",
                            JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }

            // Check if EULA exists.
            Progress.setProgress("Checking for EULA...", 40);
            if (!eulaText.exists()) {
                Logger.log("Prompting EULA.", "info");
                int dialogButtonB = JOptionPane.YES_NO_OPTION;
                int dialogResultB = JOptionPane.showConfirmDialog(null,
                        "Do you wish to agree to Mojangs EULA at\nhttps://account.mojang.com/documents/minecraft_eula",
                        "EULA", dialogButtonB);

                if (dialogResultB == JOptionPane.YES_OPTION) {

                    PrintWriter writer = null;

                    try {
                        if (Multi.getOS() == "WINDOWS") {
                            writer = new PrintWriter("eula.txt", "UTF-8");
                        } else if (Multi.getOS() == "LINUX") {
                            writer = new PrintWriter(Multi.linHome() + "/PortMiner/eula.txt", "UTF-8");
                        } else {
                            writer = new PrintWriter("eula.txt", "UTF-8");
                        }

                        writer.println("#Autogenerated by PortMiner at " + System.currentTimeMillis() + "ms.");
                        writer.println("eula=true");
                        writer.close();
                    } catch (FileNotFoundException | UnsupportedEncodingException e) {
                        Logger.log("Error creating eula.txt", "error");
                        e.printStackTrace();
                        System.exit(1);
                    }

                } else {
                    Logger.log("User declined EULA.", "info");
                    System.exit(0);
                }
            }

            // Open the port.
            Logger.log("Internal IP is " + Multi.internalIP(), "info");
            Logger.log("Opening port " + port, "info");
            Progress.setProgress("Attempting to open port " + port, 65);
            PortManager.openPort(Multi.internalIP(), port, "Minecraft Server", "TCP");

            // Set up process and runtime.
            Progress.setProgress("Setting up process and runtime...", 75);
            Runtime runTime = Runtime.getRuntime();
            String[] toRun = null;

            // Create runTime parameters.
            if (Multi.getOS() == "WINDOWS") {
                toRun = new String[] { "bin/mintty.exe", "-t", "PortMiner", "java.exe", "-Xms" + xms + "M", "-Xmx" + xmx + "M",
                        "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, "nogui" };
            } else if (Multi.getOS() == "LINUX") {
                toRun = new String[] { "xterm", "-e", "java", "-Xms" + xms + "M", "-Xmx" + xmx + "M",
                        "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, "nogui" };
            } else {
                toRun = new String[] { "bin/mintty.exe", "-t", "PortMiner", "java.exe", "-Xms" + xms + "M", "-Xmx" + xmx + "M",
                        "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, "nogui" };
            }

            Process proc = null;

            // Start the server.
            Progress.setProgress("Starting process...", 80);
            try {
                if (Multi.getOS() == "WINDOWS") {
                    proc = runTime.exec(toRun);
                } else if (Multi.getOS() == "LINUX") {
                    File serverDir = new File(Multi.linHome() + "/PortMiner/");
                    proc = runTime.exec(toRun, null, serverDir);
                } else {
                    proc = runTime.exec(toRun);
                }
            } catch (Exception e) {
                Logger.log("Exception while creating or running process.", "error");
                e.printStackTrace();
                Thread.sleep(1000);
                Logger.log("Closing port " + port, "info");
                PortManager.closePort();
                JOptionPane.showMessageDialog(null,
                        "Error while starting the main server process. Check console.\nPort " + port
                                + " has been cloesd as the server failed to start.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            // Ta-daah!
            Progress.setProgress("Done!", 100);
            Thread.sleep(250);
            Progress.closeFrame();

            // Keep looping every 5 seconds, checking if the server is still
            // running.
            while (proc.isAlive() == true) {
                Thread.sleep(5000);
            }

            // Reopen progress bar frame. Close ports and exit.
            Progress.setupFrame();
            Progress.setProgress("Closing port " + port, 25);
            Logger.log("Closing port " + port, "info");
            Thread.sleep(4000);
            PortManager.closePort();
            PropParse.setProperty("safe-close", "true");
            Progress.setProgress("Done!", 100);
            Thread.sleep(250);
            Progress.closeFrame();
            System.exit(0);
    }

}
