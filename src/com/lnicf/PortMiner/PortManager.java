package com.lnicf.PortMiner;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

public class PortManager {
    // Initialise PortMapping and UpnpService variables.
    private static PortMapping mapping = null;
    private static UpnpService upnpService = null;

    public static void openPort(String internalIP, int port, String name, String protocol) {
        // Create mapping, then use it.
        if (protocol == "TCP") {
            mapping = new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name);
        } else if (protocol == "UDP") {
            mapping = new PortMapping(port, internalIP, PortMapping.Protocol.UDP, name);
        } else {
            mapping = new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name);
        }
        upnpService = new UpnpServiceImpl(new PortMappingListener(mapping));
        upnpService.getControlPoint().search();
    }

    public static void closePort() {
        // Stop the UPnP service, closing the port.
        upnpService.shutdown();
    }
}
