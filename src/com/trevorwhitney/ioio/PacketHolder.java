package com.trevorwhitney.ioio;

import com.trevorwhitney.ioio.domain.XBeePacket;
import com.trevorwhitney.ioio.persistence.XBeePacketHelper;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

class PacketHolder {
	private TextView message;
	
	PacketHolder(View row) {
		message = (TextView)row.findViewById(R.id.packet_message);
	}
	
	void populateList(Cursor c, XBeePacketHelper helper) {
		XBeePacket packet = new XBeePacket(helper.getPacket(c));
		message.setText(packet.getResponse().toString());
	}
}