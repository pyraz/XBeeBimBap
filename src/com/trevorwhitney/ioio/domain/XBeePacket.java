package com.trevorwhitney.ioio.domain;

public class XBeePacket {
	Integer[] packet;
	
	public XBeePacket(Integer[] packet) {
		this.packet = packet;
	}
	
	public String toString() {
		String packetString = "[";
		for (int i = 0; i < packet.length; i++) {
			if (i == packet.length - 1) {
				packetString += packet[i]; 
			}
			else {
				packetString += packet[i] + ", "; 
			}
		}
		packetString += "]\n";
		return packetString;
	}
}