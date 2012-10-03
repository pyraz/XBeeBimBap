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

public class IOIOFinalProject extends IOIOActivity {
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
		byte[] uartRecievedBytes = new byte[10];
		
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
			try {
				Thread.sleep(1000);
				uartIn.read(uartRecievedBytes);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			uartRecievedString = new String(uartRecievedBytes);
			uartRecievedString = uartRecievedString.replace(',','\n');
			handler.post(updateResults);
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