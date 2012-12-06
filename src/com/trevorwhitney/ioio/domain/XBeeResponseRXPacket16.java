package com.trevorwhitney.ioio.domain;

import android.util.Log;

public class XBeeResponseRXPacket16 extends XBeeResponse {
	int sourceAddress;
	int rssi;
	int options;
	int[] data;

	public XBeeResponseRXPacket16(int[] payload, int checksum) {
		super(payload, checksum);
		parsePayload();
		apiId = 0x81;
	}
	
	@Override
	public String toString() {
		//Debugging
		String packetString = "Response data: [";
		for (int i = 0; i < data.length; i++) {
			if (i == data.length - 1) {
				packetString += data[i]; 
			}
			else {
				packetString += data[i] + ", "; 
			}
		}
		packetString += "]";
		Log.d("XBeeBimBap", packetString);
		
		//Acutal Method
		packetString += "]\n";
		StringBuilder dataString = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			dataString.append((char)data[i]);
		}
		return dataString.toString();
	}
	
	private void parsePayload() {
		sourceAddress = payload[0]*256 + payload[1];
		rssi = payload[2];
		options = payload[3];
		data = new int[payload.length - 4];
		for (int i = 0; i < payload.length - 4; i++) {
			data[i] = payload[i+4];
		}
	}
	
	
	
}