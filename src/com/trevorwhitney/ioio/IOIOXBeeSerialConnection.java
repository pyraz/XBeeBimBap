package com.trevorwhitney.ioio;

import ioio.lib.api.IOIO;
import ioio.lib.api.IOIOFactory;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.api.exception.IncompatibilityException;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.rapplogic.xbee.XBeeConnection;

public class IOIOXBeeSerialConnection implements XBeeConnection {
	
	IOIO ioio;
	Uart uart;
	InputStream uart_in;
	BufferedOutputStream uart_out;
	private final int RX_PIN;
	private final int TX_PIN;
	private final int BAUD;
	
	
	public IOIOXBeeSerialConnection(int rx, int tx, int baud) 
			throws ConnectionLostException, IncompatibilityException {
		RX_PIN = rx;
		TX_PIN = tx;
		BAUD = baud;
		
		ioio = IOIOFactory.create();
		ioio.waitForConnect();
		uart = ioio.openUart(RX_PIN, TX_PIN, BAUD, 
				Uart.Parity.NONE, Uart.StopBits.ONE);
		uart_in = uart.getInputStream();
		uart_out = new BufferedOutputStream(uart.getOutputStream());
	}
	
	@Override
	public void close() {
		ioio.disconnect();
		try {
			ioio.waitForDisconnect();
		} catch (InterruptedException e) {
			Log.e("SenseCheck", "unable to disconnect from IOIO board");
			e.printStackTrace();
		}
	}

	@Override
	public InputStream getInputStream() {
		return uart_in;
	}

	@Override
	public OutputStream getOutputStream() {
		return uart_out;
	}
	
	
}