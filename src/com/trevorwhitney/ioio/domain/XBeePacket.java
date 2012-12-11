package com.trevorwhitney.ioio.domain;

import com.trevorwhitney.ioio.exception.InvalidPacketException;

public class XBeePacket {
	Integer[] packet;
	XBeeResponse response;
	XBeePacketType type;
	
	public XBeePacket(Integer[] packet) {
		this.packet = packet;
		try {
			response = XBeeResponseFactory.getInstance(packet);
			type = XBeePacketType.getTypeFromId(response.getApiId());
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
	
	public Integer[] getPacket() {
		return packet;
	}

	public XBeeResponse getResponse() {
		return response;
	}

	public XBeePacketType getType() {
		return type;
	}
}