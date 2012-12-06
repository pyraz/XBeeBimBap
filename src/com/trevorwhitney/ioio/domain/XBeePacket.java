package com.trevorwhitney.ioio.domain;

import com.trevorwhitney.ioio.exception.InvalidPacketException;

public class XBeePacket {
	Integer[] packet;
	XBeeResponse response;
	
	public XBeePacket(Integer[] packet) {
		this.packet = packet;
		try {
			this.response = XBeeResponseFactory.getInstance(packet);
		} catch (InvalidPacketException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String packetString = "";
		
		if (response != null) {
			packetString = response.toString();
		}
		else {
			packetString = "[";
			for (int i = 0; i < packet.length; i++) {
				if (i == packet.length - 1) {
					packetString += packet[i]; 
				}
				else {
					packetString += packet[i] + ", "; 
				}
			}
			packetString += "]\n";
		}
		return packetString;
	}
}