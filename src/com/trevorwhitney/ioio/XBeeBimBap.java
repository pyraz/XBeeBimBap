package com.trevorwhitney.ioio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.trevorwhitney.ioio.domain.XBeeResponse;
import com.trevorwhitney.ioio.domain.XBeeResponseFactory;
import com.trevorwhitney.ioio.domain.XBeePacket;
import com.trevorwhitney.ioio.exception.InvalidPacketException;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class XBeeBimBap extends IOIOActivity {
	TextView ioioMessage;
	ToggleButton logData;
	ListView data;
	String packetString = "";
	Handler handler = new Handler();
	List<XBeePacket> packets = new ArrayList<XBeePacket>();
	ArrayAdapter<XBeePacket> packetsAdapter = null;
	XBeePacket currentPacket = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    //ioioMessage = (TextView)findViewById(R.id.ioio_message);
	    logData = (ToggleButton)findViewById(R.id.log_data);
	    data = (ListView)findViewById(R.id.data);
	    
	    packetsAdapter = new ArrayAdapter<XBeePacket>(this,
	    		android.R.layout.simple_list_item_1, packets);
	    data.setAdapter(packetsAdapter);
	    
			Integer[] samplePacket = {126, 0, 19, 129, 0, 2, 23, 0, 72, 101, 108, 
			    108, 111, 32, 102, 114, 111, 109, 32, 50, 13, 10, 52};
			XBeePacket sampleXBeePacket = new XBeePacket(samplePacket);
			packets.add(sampleXBeePacket);
	
	    enableUi(false);
	}
	
	final Runnable updatePacketsList = new Runnable() {
		public void run() {
			if (currentPacket != null) {
				packets.add(currentPacket);
				packetsAdapter.notifyDataSetChanged();
				currentPacket = null;
			}
		}
	};
	
	class Looper implements IOIOLooper {
		
		Uart uart;
		InputStream uartIn;
		int startDelimiter = 0;
		int size = 0;
		byte[] packetSize = new byte[2];
		byte[] packet = null;
		int checksum = 0;
		Integer[] fullPacket = null;
		final static int UART_RX_PIN = 4;
		final static int UART_TX_PIN = 5;
		final static int UART_BAUD = 57600;
		
		@Override
		public void setup(IOIO ioio) throws ConnectionLostException {
			uart = ioio.openUart(UART_RX_PIN, UART_TX_PIN, UART_BAUD, 
					Uart.Parity.NONE, Uart.StopBits.ONE);
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
				fullPacket = new Integer[4 + size];
				fullPacket[0] = startDelimiter;
				fullPacket[1] = (int) packetSize[0] & 0xFF;
				fullPacket[2] = (int) packetSize[1] & 0xFF;
				for (int i = 0; i < size; i++) {
					fullPacket[3 + i] = (int) packet[i] & 0xFF;
				}
				fullPacket[3 + size] = checksum;
				
				/*
				 * Next I would like to actually parse out the information from
				 * the packet. Unfortunately, this currently isn't working, and 
				 * there's currently no way to debug it without either 
				 * A) implementing bluetooth on the IOIO or
				 * B) rooting my device, since I have no access to Logcat while
				 * the IOIO is using up the Android's USB port. Therefore, I'm
				 * stuck with just writing raw packets to the list for now.
				
				try {
					XBeeResponse response = 
							XBeeResponseFactory.getInstance(fullPacket);
					uartRecievedString += "API Id: " + response.getApiId();
				} catch (InvalidPacketException e) {
					e.printStackTrace();
					uartRecievedString = "Exception occured";
				}
				*
				* In the meantime, I've created a transitional XBeePacket
				* class that will have to handle the magic for now
				*
				*/
				
				currentPacket = new XBeePacket(fullPacket);
				handler.post(updatePacketsList);
			}
		}

		@Override
		public void disconnected() {
			Toast.makeText(getApplicationContext(), 
					"Please reconnect IOIO device",
					Toast.LENGTH_LONG).show();
			enableUi(false);
		}

		@Override
		public void incompatible() {
			Toast.makeText(getApplicationContext(), 
					"Incompatible IOIO device detected",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}

	private void enableUi(final boolean enable) {
		if (enable) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					logData.setEnabled(true);
				}
			});
		}
		else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					logData.setEnabled(false);
				}
			});
		}
	}
	
}