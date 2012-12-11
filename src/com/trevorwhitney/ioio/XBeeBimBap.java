package com.trevorwhitney.ioio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.trevorwhitney.ioio.domain.XBeePacket;
import com.trevorwhitney.ioio.persistence.XBeePacketHelper;

import ioio.lib.api.IOIO;
import ioio.lib.api.Uart;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class XBeeBimBap extends IOIOActivity {
	Boolean logging = false;
	ToggleButton logData;
	ListView data;
	String packetString = "";
	Handler handler = new Handler();
	List<XBeePacket> packets = new ArrayList<XBeePacket>();
	ArrayAdapter<XBeePacket> packetsAdapter = null;
	XBeePacket currentPacket = null;
	XBeePacketHelper helper = new XBeePacketHelper(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    logData = (ToggleButton)findViewById(R.id.log_data);
	    logData.setOnCheckedChangeListener(logDataListener);
	    data = (ListView)findViewById(R.id.data);
	    
	    packetsAdapter = new ArrayAdapter<XBeePacket>(this,
	    		android.R.layout.simple_list_item_1, packets);
	    data.setAdapter(packetsAdapter);
	    
			Integer[] samplePacket = {126, 0, 19, 129, 0, 2, 23, 0, 72, 101, 108, 
			    108, 111, 32, 102, 114, 111, 109, 32, 50, 13, 10, 52};
			XBeePacket sampleXBeePacket = new XBeePacket(samplePacket);
			packetsAdapter.add(sampleXBeePacket);
	
	    enableUi(false);
	    
	    if (!logData.isEnabled()) {
	    	Toast.makeText(getApplicationContext(), "Please connect IOIO device",
						Toast.LENGTH_LONG).show();
	    }
	}
	
	final Runnable updatePacketsList = new Runnable() {
		public void run() {
			if (currentPacket != null) {
				packetsAdapter.add(currentPacket);
				
				if (logging == true) {
					helper.insert(currentPacket.getPacket(), 
							currentPacket.getType());
				}
				
				currentPacket = null;
			}
		}
	};
	
	private OnCheckedChangeListener logDataListener = new
			OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					logging = isChecked;
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