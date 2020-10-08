package com.lnicf.PortMiner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropParse {
  static File propFile = new File(Multi.workingDir() + "/portminer.properties");
  
  public static void checkPropFile() {
    OutputStream output = null;
    Properties prop = new Properties();
    if (!propFile.exists()) {
      Logger.log("HELLO WORLD", "info");
      Logger.log("Properties file does not exist. Attempting to create.", "info");
      PortMiner.firstRun = true;
      try {
        output = new FileOutputStream(propFile.toString());
        prop.setProperty("config-version", Double.toString(1.2D));
        prop.setProperty("find-ip", "true");
        prop.setProperty("internal-ip", "0.0.0.0");
        prop.setProperty("port", "25565");
        prop.setProperty("plugin-support", "false");
        prop.setProperty("plugin-port", "8080");
        prop.setProperty("plugin-protocol", "TCP");
        prop.setProperty("server-jar", "minecraft_server.jar");
        prop.setProperty("xms", "256");
        prop.setProperty("xmx", "1024");
        prop.setProperty("maxpermsize", "64");
        prop.setProperty("safe-close", "true");
        prop.setProperty("run-server", "true");
        prop.setProperty("run-server-false-protocol", "TCP");
        prop.store(output, (String)null);
      } catch (IOException e) {
        Logger.log("Exception while creating properties file", "error");
        e.printStackTrace();
        System.exit(1);
      } 
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
    checkPropFile();
    Logger.log("Getting property: " + property, "info");
    Properties prop = new Properties();
    InputStream input = null;
    try {
      input = new FileInputStream(propFile.toString());
      prop.load(input);
      Logger.log("Found value: " + prop.getProperty(property), "info");
      return prop.getProperty(property);
    } catch (IOException e) {
      Logger.log("Exception while trying to get property!", "error");
      e.printStackTrace();
      System.exit(1);
      return "";
    } 
  }
  
  public static void setProperty(String property, String value) {
    checkPropFile();
    Logger.log("Setting property: " + property + " to " + value, "info");
    Properties prop = new Properties();
    InputStream input = null;
    OutputStream output = null;
    try {
      input = new FileInputStream(propFile.toString());
      prop.load(input);
      input.close();
      output = new FileOutputStream(propFile.toString());
      prop.setProperty(property, value);
      prop.store(output, (String)null);
      output.close();
    } catch (IOException e) {
      Logger.log("Exception while trying to get property!", "error");
      e.printStackTrace();
      System.exit(1);
    } 
  }
}
