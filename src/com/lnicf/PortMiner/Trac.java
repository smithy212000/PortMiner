package com.lnicf.PortMiner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class Trac {
	public static int randomUUID() {
		Random randInt = new Random();
		int uL = 2137823490;
		return randInt.nextInt(uL);
	}
	
	public static int getUUID() {
		File uuidFile = new File(Multi.workingDir()+"/trac-uuid.txt");
		PrintWriter writer = null;
		int finalUUID = 0;
		
		if(!uuidFile.exists()) {
			Logger.log("UUID file does not exist. Creating one.", "info");
			try {
				writer = new PrintWriter(uuidFile.toString());
				int randID = randomUUID();
				writer.println(randID);
				finalUUID = randID;
				writer.close();
				Logger.log("UUID is "+finalUUID, "info");
			} catch (FileNotFoundException  e) {
				Logger.log("Exception while creating UUID.", "error");
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			Scanner uuidReader = null;;
			try {
				uuidReader = new Scanner(uuidFile);
			} catch (FileNotFoundException e) {
				Logger.log("Exception while reading UUID.", "error");
				e.printStackTrace();
				System.exit(1);
			}
			finalUUID = Integer.parseInt(uuidReader.nextLine());
			Logger.log("UUID already exists. UUID is "+finalUUID, "info");
			uuidReader.close();
		}
		return finalUUID;
	}
	
	public static boolean allowTrac() {
		File tracFile = new File(Multi.workingDir()+"/opt-out.txt");
		if(tracFile.exists()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void httpsReq(String inUrl) throws IOException {
		URL reqUrl = new URL(inUrl);
		HttpsURLConnection conn = (HttpsURLConnection)reqUrl.openConnection();
		Logger.log(conn.getInputStream().toString(), "info");
	}

}
