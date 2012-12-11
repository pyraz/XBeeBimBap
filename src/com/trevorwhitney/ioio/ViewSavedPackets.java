package com.trevorwhitney.ioio;

import com.trevorwhitney.ioio.persistence.XBeePacketHelper;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class ViewSavedPackets extends ListActivity {
	XBeePacketHelper helper;
	Cursor packets;
	PacketAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		helper = new XBeePacketHelper(this);
		packets = helper.getAll();
		adapter = new PacketAdapter(packets);
		
		setListAdapter(adapter);
	}
	
	class PacketAdapter extends CursorAdapter {

		@SuppressWarnings("deprecation")
		PacketAdapter(Cursor c) {
			super(ViewSavedPackets.this, c);
		}
		
		@Override
		public void bindView(View row, Context context, Cursor c) {
			PacketHolder holder = (PacketHolder)row.getTag();
			holder.populateList(c, helper);
		}

		@Override
		public View newView(Context context, Cursor cursor, 
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.packet_row, parent, false);
			PacketHolder holder = new PacketHolder(row);
			
			row.setTag(holder);
			
			return row;
		}
		
	}
}