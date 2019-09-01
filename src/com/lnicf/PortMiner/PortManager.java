package com.lnicf.PortMiner;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

public class PortManager {
	// Initialise PortMapping and UpnpService variables.
	private static PortMapping[] mapping = null;
	private static UpnpService upnpService = null;
	private static UpnpService upnpServicePlugin = null;

	public static void openPort(String internalIP, int port, String name, String protocol) {
		Logger.log("Creating port mapping", "info");
		// Create mapping, then use it.
		if (protocol.equals("TCP")) {
			mapping = new PortMapping[] {new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name)};
		} else if (protocol.equals("UDP")) {
			mapping = new PortMapping[] {new PortMapping(port, internalIP, PortMapping.Protocol.UDP, name)};
		} else if (protocol.equals("UDP_TCP")) {
			mapping = new PortMapping[] {new PortMapping(port, internalIP, PortMapping.Protocol.UDP, name),
										 new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name)};
		}
		else {
			mapping = new PortMapping[] {new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name)};
		}
		
		Logger.log("Creating UPnP service", "info");
		upnpService = new UpnpServiceImpl(new PortMappingListener(mapping));
		upnpService.getControlPoint().search();
	}
	
	public static void openPortPlugin(String internalIP, int port, String name, String protocol) {
		Logger.log("Creating plugin port mapping", "info");
		// Create mapping, then use it.
		if (protocol.equals("TCP")) {
			mapping = new PortMapping[] {new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name)};
		} else if (protocol.equals("UDP")) {
			mapping = new PortMapping[] {new PortMapping(port, internalIP, PortMapping.Protocol.UDP, name)};
		} else {
			mapping = new PortMapping[] {new PortMapping(port, internalIP, PortMapping.Protocol.TCP, name)};
		}
		
		Logger.log("Creating plugin UPnP service", "info");
		upnpServicePlugin = new UpnpServiceImpl(new PortMappingListener(mapping));
		upnpServicePlugin.getControlPoint().search();
	}

	public static void closePort() {
		// Stop the UPnP service, closing the port.
		upnpService.shutdown();
	}
	
	public static void closePortPlugin() {
		// Stop the UPnP service, closing the port.
		upnpServicePlugin.shutdown();
	}
}
