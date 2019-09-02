package com.lnicf.PortMiner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.lnicf.PortMiner.Logger.LogType;

public class Updater {
    public static void checkForUpdates() {
        // Initialise URL variable.
        URL serverURI = null;

        // Try set serverURI.
        try {
            serverURI = new URL("http://smithy212000.github.io/PortMinerVersion");
        } catch (MalformedURLException e) {
            Logger.log("Exception in Updater.", LogType.ERROR);
            e.printStackTrace();
            System.exit(1);
        }

        // Initialise scanner.
        Scanner versionText = null;

        try {
            versionText = new Scanner(serverURI.openStream());
        } catch (IOException e) {
            Logger.log("Error while trying to check for updates.", LogType.ERROR);
            e.printStackTrace();
            return;
        }

        // Initialise latestVersion variable.
        double latestVersion = 0.0;

        // Parse the latestVersion variable.
        try {
            latestVersion = Double.parseDouble(versionText.nextLine());
        } catch (Exception e) {
            Logger.log("Exception while trying to check for updates.", LogType.ERROR);
            versionText.close();
            return;
        }

        // If the latest version is not the same as the current version, display a message.
        if (latestVersion != PortMiner.softVersion) {
            Logger.log("PortMiner is outdated.", LogType.WARN);
            JOptionPane.showMessageDialog(null, "An update is available.\nIt is recommended you download it.",
                    "Update available", JOptionPane.INFORMATION_MESSAGE);
            versionText.close();
            return;
        } else {
            Logger.log("PortMiner is up to date.", LogType.INFO);
            versionText.close();
            return;
        }
    }
}
