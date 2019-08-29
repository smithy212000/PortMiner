package com.lnicf.PortMiner;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

    // Cache OSType
    private static OSType operatingSys = null;
    
    // Get operating system.
    public static OSType getOS() {
    	if (operatingSys == null) {
    		String tempOS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
            if (tempOS.contains("win"))
            	operatingSys = OSType.Windows;
            else if (tempOS.contains("linux"))
            	operatingSys = OSType.Linux;
            else if (tempOS.contains("unix"))
            	operatingSys = OSType.Linux;
            else if (tempOS.contains("mac"))
            	operatingSys = OSType.Mac;
            else if (tempOS.contains("OSX"))
            	operatingSys = OSType.Mac;
            else {
            	operatingSys = OSType.Unknown;
                Logger.log("Couldn't get OS.", "warn");
            }
    	}
        
        return operatingSys;
    }
    
	public static File workingDir() {
		
		File chkDir = null;
		
		if (getOS() == OSType.Windows) {
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
