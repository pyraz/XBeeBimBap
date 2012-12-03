package com.trevorwhitney.ioio;

import java.io.IOException;
import java.io.InputStream;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.ToggleButton;

public class XBeeBimBap extends IOIOActivity {
	TextView uartValue;
	ToggleButton toggleButton;
	String uartRecievedString = "Waiting...";
	Handler handler = new Handler();
	DigitalOutput led;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	
	    uartValue = (TextView)findViewById(R.id.uart);
	    toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
	
	    enableUi(false);
	}
	
	final Runnable updateResults = new Runnable() {
		public void run() {
			uartValue.append(uartRecievedString);
		}
	};
	
	class Looper extends BaseIOIOLooper {
		
		Uart uart;
		InputStream uartIn;
		int startDelimiter = 0;
		int size = 0;
		byte[] packetSize = new byte[2];
		byte[] packet = null;
		int checksum = 0;
		int[] fullPacket = null;
		final static int UART_RX_PIN = 4;
		final static int UART_TX_PIN = 5;
		final static int UART_BAUD = 57600;
		
		@Override
		public void setup() throws ConnectionLostException {
			uart = ioio_.openUart(UART_RX_PIN, UART_TX_PIN, UART_BAUD, 
					Uart.Parity.NONE, Uart.StopBits.ONE);
			led = ioio_.openDigitalOutput(IOIO.LED_PIN, true);
			uartIn = uart.getInputStream();
			enableUi(true);
		}
		
		@Override
		public void loop() {
			size = 0;
			try {
				Thread.sleep(1000);
				startDelimiter = uartIn.read();
				if (startDelimiter == 0x7e) {
					uartIn.read(packetSize);
					size = (int)packetSize[0]*256 + (int)packetSize[1];
					if (size > 0) {
						packet = new byte[size];
						uartIn.read(packet);
						checksum = uartIn.read();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (size > 0) {
				fullPacket = new int[4 + size];
				fullPacket[0] = startDelimiter;
				fullPacket[1] = (int) packetSize[0] & 0xFF;
				fullPacket[2] = (int) packetSize[1] & 0xFF;
				for (int i = 0; i < size; i++) {
					fullPacket[3 + i] = (int) packet[i] & 0xFF;
				}
				fullPacket[3 + size] = checksum; 
				uartRecievedString = "Packet size: " + size + "\n";
				uartRecievedString += "Packet: [";
				for (int i = 0; i < fullPacket.length; i++) {
					uartRecievedString += fullPacket[i] + ", "; 
				}
				uartRecievedString += "]\n";
				handler.post(updateResults);
			}
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				toggleButton.setEnabled(enable);
			}
		});
	}
	
}