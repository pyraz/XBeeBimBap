package com.trevorwhitney.ioio.domain;

import com.trevorwhitney.ioio.exception.InvalidPacketException;

public class XBeeResponseFactory {
	private static int apiId;
	private static int checksum;
	
	public static XBeeResponse getInstance(Integer[] packet) 
			throws InvalidPacketException {
		int[] payload = parsePacket(packet);
		if (apiId == 0x81) {
			return new XBeeResponseRXPacket16(payload, checksum);
		}
		else {
			return new XBeeResponse(payload, checksum);
		}
	}
	
	public static int[] parsePacket(Integer[] packet) throws InvalidPacketException {
		if (packet[0] != 0x7e) {
			throw new InvalidPacketException(
					"Supplied packet has an invalid start delimeter");
		}
		
		int length = packet[1]*256 + packet[2];
		apiId = packet[3];
		checksum = packet[-1];
		
		int[] payload = new int[length - 1];
		
		for (int i = 0; i < length; i++) {
			if (packet[i+4] != null) {
				payload[i] = packet[i+4];
			}
			else {
				throw new InvalidPacketException(
						"Actual packet length does not match decalred packet " +
						"length");
			}
		}
		
		return payload;
	}
}