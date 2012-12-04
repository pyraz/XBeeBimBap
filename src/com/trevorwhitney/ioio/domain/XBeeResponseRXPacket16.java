package com.trevorwhitney.ioio.domain;

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
		for (int i = 0; i < payload.length; i++) {
			data[i] = payload[i+4];
		}
	}
	
	
	
}