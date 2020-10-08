package com.lnicf.PortMiner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
PortMiner is created by smithy212000 (LNICF).
Requires cling-core, cling-support, seamless-http and seamless-util libraries all
available from http://4thline.org/projects/cling/
This software is 75% thanks to Russian Standard :)
*/

public class PortMiner {
  public static final double softVersion = 5.4D;
  
  public static final double configVersion = 1.2D;
  
  public static boolean firstRun = false;
  
  public static void main(String[] args) throws InterruptedException {
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
    if (Multi.getOS() == "WINDOWS") {
      eulaText = new File("eula.txt");
    } else if (Multi.getOS() == "LINUX") {
      eulaText = new File(Multi.workingDir() + "/eula.txt");
    } else {
      eulaText = new File("eula.txt");
    } 
    if (Multi.getOS() == "UNKNOWN" || Multi.getOS() == "MAC") {
      Logger.log("Unsupported operating system", "warn");
      JOptionPane.showMessageDialog(null, 
          "Your operating system is currently not supported by PortMiner.\nContinue at your own risk.", 
          "Unsupported OS", 2);
    } 
    if (Double.parseDouble(PropParse.getProperty("config-version")) != 1.2D) {
      Logger.log("Configuartion out of date", "error");
      JOptionPane.showMessageDialog(null, 
          "Your configuration is out of date, please delete it and reload PortMiner.", 
          "Outdated configuration", 2);
      System.exit(1);
    } 
    Logger.log("PortMiner uses LGPL software from https://github.com/4thline", "info");
    Logger.log("Made with thanks to Russian Standard :)", "info");
    
    if(Trac.allowTrac()) {
    	String tracUrl = new String("https://trac-url-not-in-use.com/?k="+Multi.getOS()+"&dt="+Multi.currentTimeTrac()+"&firstRun="+firstRun+"&id="+Trac.getUUID());
    	try {
			Trac.httpsReq(tracUrl);
		} catch (IOException e) {
			Logger.log("Error in trac", "error");
			e.printStackTrace();
			System.exit(1);
		}
    }
    
    Progress.setupFrame();
    Progress.setProgress("Checking for updates...", 5);
    Updater.checkForUpdates();
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
      if (!protocol.equals("TCP") && !protocol.equals("UDP")) {
        Logger.log("Exception while getting properties.", "error");
        JOptionPane.showMessageDialog(null, 
            "PortMiner had an error reading the configuration.\nTry deleting it, and restarting PortMiner.", 
            "Failure reading configuration", 2);
        System.exit(1);
      } 
    } catch (NumberFormatException e) {
      Logger.log("Exception while getting properties.", "error");
      JOptionPane.showMessageDialog(null, 
          "PortMiner had an error reading the configuration.\nTry deleting it, and restarting PortMiner.", 
          "Failure reading configuration", 2);
      e.printStackTrace();
      System.exit(1);
    } 
    if (!safeClose) {
      Logger.log("PortMiner did not close correctly.", "warn");
      JOptionPane.showMessageDialog(null, 
          "PortMiner did not close correctly.\nIf this issue continues, please report it.", "Unsafe close", 
          2);
    } else {
      PropParse.setProperty("safe-close", "false");
    } 
    if (!runServer) {
      Progress.closeFrame();
      NoServer.run();
    } 
    Progress.setProgress("Checking for server jar...", 25);
    if (!serverJar.exists()) {
      Logger.log("Server jar does not exist.", "warn");
      int dialogButton = 0;
      int dialogResult = JOptionPane.showConfirmDialog(null, 
          "PortMiner could not find " + serverJar + ".\nDo you want to select the server jar?", "Server Jar", 
          dialogButton);
      if (dialogResult == 0) {
        Logger.log("Setting jar file.", "info");
        JFileChooser fChoose = new JFileChooser();
        fChoose.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fChoose.showOpenDialog(Progress.frame);
        if (result == 0) {
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
    Progress.setProgress("Checking for EULA...", 40);
    if (!eulaText.exists()) {
      Logger.log("Prompting EULA.", "info");
      int dialogButtonB = 0;
      int dialogResultB = JOptionPane.showConfirmDialog(null, 
          "Do you wish to agree to Mojangs EULA at\nhttps://account.mojang.com/documents/minecraft_eula", 
          "EULA", dialogButtonB);
      if (dialogResultB == 0) {
        PrintWriter writer = null;
        try {
          if (Multi.getOS() == "WINDOWS") {
            writer = new PrintWriter("eula.txt", "UTF-8");
          } else if (Multi.getOS() == "LINUX") {
            writer = new PrintWriter(Multi.workingDir() + "/eula.txt", "UTF-8");
          } else {
            writer = new PrintWriter("eula.txt", "UTF-8");
          } 
          writer.println("#Autogenerated by PortMiner at " + System.currentTimeMillis() + "ms.");
          writer.println("eula=true");
          writer.close();
        } catch (FileNotFoundException|java.io.UnsupportedEncodingException e) {
          Logger.log("Error creating eula.txt", "error");
          e.printStackTrace();
          System.exit(1);
        } 
      } else {
        Logger.log("User declined EULA.", "info");
        System.exit(0);
      } 
    } 
    Logger.log("Internal IP is " + Multi.internalIP(), "info");
    Logger.log("Opening port " + port, "info");
    Progress.setProgress("Attempting to open port " + port, 65);
    PortManager.openPort(Multi.internalIP(), port, "Minecraft Server", "TCP");
    if (pluginSupport) {
      Logger.log("Plugin support enabled in configuration, attempting to open port " + pluginPort, "info");
      Progress.setProgress("Attempting to open port " + pluginPort, 70);
      PortManager.openPortPlugin(Multi.internalIP(), pluginPort, "Minecraft Server Plugin", pluginProtocol);
    } 
    Progress.setProgress("Setting up process and runtime...", 75);
    Runtime runTime = Runtime.getRuntime();
    String[] toRun = null;
    if (Multi.getOS() == "WINDOWS") {
      toRun = new String[] { 
          "cmd", "/c", "start", "/WAIT", "java.exe", "-Xms" + xms + "M", 
          "-Xmx" + xmx + "M", "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, 
          "nogui" };
    } else if (Multi.getOS() == "LINUX") {
      toRun = new String[] { "xterm", "-e", "java", "-Xms" + xms + "M", "-Xmx" + xmx + "M", 
          "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, "nogui" };
    } else {
      toRun = new String[] { 
          "cmd", "/c", "start", "/WAIT", "java.exe", "-Xms" + xms + "M", 
          "-Xmx" + xmx + "M", "-XX:MaxPermSize=" + maxpermsize + "M", "-jar", sServerJar, 
          "nogui" };
    } 
    Process proc = null;
    Progress.setProgress("Starting process...", 80);
    try {
      if (Multi.getOS() == "WINDOWS") {
        proc = runTime.exec(toRun);
      } else if (Multi.getOS() == "LINUX") {
        File serverDir = Multi.workingDir();
        proc = runTime.exec(toRun, (String[])null, serverDir);
      } else {
        proc = runTime.exec(toRun);
      } 
    } catch (Exception e) {
      Logger.log("Exception while creating or running process.", "error");
      e.printStackTrace();
      Thread.sleep(1000L);
      Logger.log("Closing port " + port, "info");
      PortManager.closePort();
      JOptionPane.showMessageDialog(null, "Error while starting the main server process. Check console.\nPort " + 
          port + " has been closed as the server failed to start.", "Error", 0);
      System.exit(1);
    } 
    Progress.setProgress("Done!", 100);
    Thread.sleep(250L);
    Progress.closeFrame();
    while (proc.isAlive())
      Thread.sleep(2500L); 
    Progress.setupFrame();
    Progress.setProgress("Closing port " + port, 25);
    Logger.log("Closing port " + port, "info");
    Thread.sleep(4000L);
    PortManager.closePort();
    if (pluginSupport) {
      Progress.setProgress("Closing port " + pluginPort, 50);
      Logger.log("Closing port " + pluginPort, "info");
      PortManager.closePortPlugin();
    } 
    PropParse.setProperty("safe-close", "true");
    Progress.setProgress("Done!", 100);
    Thread.sleep(250L);
    Progress.closeFrame();
    System.exit(0);
  }
}
