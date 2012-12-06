package com.trevorwhitney.ioio.domain;

import android.util.Log;

import com.trevorwhitney.ioio.exception.InvalidPacketException;

public class XBeeResponseFactory {
	private static int apiId;
	private static int checksum;
	public static XBeeResponse response;
	
	public static XBeeResponse getInstance(Integer[] packet) 
			throws InvalidPacketException {
		int[] payload = parsePacket(packet);
		if (apiId == (int)0x81) {
			response = new XBeeResponseRXPacket16(payload, checksum);
		}
		else {
			response = new XBeeResponse(payload, checksum);
		}
		
		if (!response.isValid()) {
			throw new InvalidPacketException("Checksum is not valid");
		}
		
		return response;
	}
	
	public static int[] parsePacket(Integer[] packet) throws InvalidPacketException {
		if (!packet[0].equals((int)0x7e)) {
			throw new InvalidPacketException(
					"Supplied packet has an invalid start delimeter");
		}
		
		int length = packet[1]*256 + packet[2];
		apiId = packet[3];
		checksum = packet[packet.length - 1];
		
		String debug1 = "Length: " + length + ", API ID: "  + 
				apiId + ", Checksum: " + checksum;
		Log.d("XBeeBimBap", debug1);
		
		int[] payload = new int[length - 1];
		
		for (int i = 0; i < length - 1; i++) {
			if (packet[i+4] != null) {
				payload[i] = packet[i+4];
			}
			else {
				throw new InvalidPacketException(
						"Actual packet length does not match decalred packet " +
						"length");
			}
		}
		
		//Debug
		String payloadString = "Payload: [";
		for (int i = 0; i < payload.length; i++) {
			if (i == payload.length - 1) {
				payloadString += payload[i]; 
			}
			else {
				payloadString += payload[i] + ", "; 
			}
		}
		payloadString += "]";
		Log.d("XBeeBimBap", payloadString);
		
		return payload;
	}
}