package com.trevorwhitney.ioio.exception;

import android.util.Log;

public class InvalidPacketException extends Exception {
	String message;

	public InvalidPacketException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
}