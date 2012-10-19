package com.trevorwhitney.ioio;

import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.api.exception.IncompatibilityException;

public class IOIOXbeeSerialConnectionFactory {
	
	public static final int RX_PIN = 4;
	public static final int TX_PIN = 5;
	public static final int BAUD = 57600;
	
	public static IOIOXBeeSerialConnection getInstance() 
			throws ConnectionLostException, IncompatibilityException {
		return new IOIOXBeeSerialConnection(RX_PIN, TX_PIN, BAUD);
	}
	
	public static IOIOXBeeSerialConnection getInstance(int rx, int tx,
			int baud) throws ConnectionLostException, IncompatibilityException {
		return new IOIOXBeeSerialConnection(rx, tx, baud);
	}
}