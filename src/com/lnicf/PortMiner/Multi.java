package com.lnicf.PortMiner;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

public class Multi {
    // Class for obtaining small bits of info.
    
    // Get current time in 24 hour format.
    public static String currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(System.currentTimeMillis());
    }

    // Get internal IP address.
    public static String internalIP() {
        boolean manualIP = false;

        try {
            manualIP = Boolean.parseBoolean(PropParse.getProperty("find-ip"));
        } catch (Exception e) {
            Logger.log("Exception in Multi.internalIP(), failed to parse boolean. Possible invalid config?", "error");
            e.printStackTrace();
            System.exit(1);
        }
        String finalIP = null;

        if (!manualIP == true) {
            finalIP = PropParse.getProperty("internal-ip");
        } else {
            try {
                finalIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                Logger.log("Exception while trying to find internal IP address.", "error");
                e.printStackTrace();
                System.exit(1);
            }
        }

        return finalIP;
    }

    // Get operating system.
    public static String getOS() {
        String operatingSys = System.getProperty("os.name").toLowerCase();
        if (operatingSys.contains("win"))
            return "WINDOWS";
        if (operatingSys.contains("linux"))
            return "LINUX";
        if (operatingSys.contains("unix"))
            return "LINUX";
        if (operatingSys.contains("mac"))
            return "MAC";
        if (operatingSys.contains("OSX"))
            return "MAC";
        Logger.log("Couldn't get OS.", "warn");
        return "UNKNOWN";
    }
    
	public static File workingDir() {
		
		File chkDir = null;
		
		if (getOS() == "WINDOWS") {
			chkDir = new File(System.getenv("APPDATA") + "\\PortMiner\\");
		} else {
			chkDir = new File(System.getProperty("user.home") + "/PortMiner/");
		}
		
		if(!chkDir.exists()) {
			chkDir.mkdir();
		}
		
		return chkDir;
		
	}
	
}
