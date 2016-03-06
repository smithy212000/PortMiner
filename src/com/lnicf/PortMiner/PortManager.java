package com.lnicf.PortMiner;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

public class PortManager {
    // Initialise PortMapping and UpnpService variables.
    private static PortMapping mapping = null;
    private static UpnpService upnpService = null;

    public static void openPort(String internalIP, int port, String name) {
        // Create mapping, then use it.
        mapping = new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name);
        upnpService = new UpnpServiceImpl(new PortMappingListener(mapping));
        upnpService.getControlPoint().search();
    }

    public static void closePort() {
        // Stop the UPnP service, closing the port.
        upnpService.shutdown();
    }
}
