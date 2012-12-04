package com.trevorwhitney.ioio.domain;

public class XBeeResponse {
	int apiId = 0;
	int[] payload;
	int checksum;
	
	public XBeeResponse(int[] payload, int checksum) {
		this.payload = payload;
		this.checksum = checksum;
	}
	
	public boolean isValid() {
		int value = 0;
		for (int i = 0; i < payload.length; i++) {
			value += payload[i];
		}
		value += apiId;
		value += checksum;
		
		if ((value & 255) == 255) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int length() {
		return payload.length;
	}
	
	//Getters and Setters
	public int getApiId() {
		return apiId;
	}
	public int[] getPayload() {
		return payload;
	}
	public int getChecksum() {
		return checksum;
	}

}