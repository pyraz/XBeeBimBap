package com.trevorwhitney.ioio.persistence;

import com.trevorwhitney.ioio.domain.XBeePacketType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class XBeePacketHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "XBeeBimBap.db";
	private static final int SCHEMA_VERSION = 1;
	
	public XBeePacketHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE xbee_packets " +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"packet TEXT, packet_type INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, 
			int newVersion) {
		// In first schema, nothing to do on upgrade
	}
	
	public void insert(Integer[] packet, XBeePacketType type) {
		ContentValues cv = new ContentValues();
		
		cv.put("packet", convertPacketToString(packet));
		cv.put("packet_type", type.apiId());
		
		getWritableDatabase().insert("xbee_packets", "packet", cv);
	}
	
	public void update(String id, Integer[] packet, 
			XBeePacketType type) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		
		cv.put("packet", convertPacketToString(packet));
		cv.put("api_id", type.apiId());
		
		getWritableDatabase().update("xbee_packets", cv, "_id=?", args);
	}
	
	public Cursor getAll() {
		return getReadableDatabase().rawQuery("SELECT _id, packet, " +
				"api_id FROM xbee_packets", null);
	}
	
	public int getId(Cursor c) {
		return c.getInt(0);
	}
	
	public Integer[] getPacket(Cursor c) {
		return convertPacketToInteger(c.getString(1));
	}
	
	public XBeePacketType getType(Cursor c) {
		return XBeePacketType.getTypeFromId(c.getInt(2));
	}
	
	private String convertPacketToString(Integer[] packet) {
		String packetString = "";
		
		for (int i = 0; i < packet.length; i++) {
			if (i == packet.length - 1) {
				packetString += packet[i].toString();
			}
			else {
				packetString += packet[i].toString() + ",";
			}
		}
		
		return packetString;
	}
	
	private Integer[] convertPacketToInteger(String packetString) {
		String[] packetStringArray = packetString.split(",");
		Integer[] packet = new Integer[packetStringArray.length];
		
		for (int i = 0; i < packetStringArray.length; i++) {
			packet[i] = Integer.parseInt(packetStringArray[i]);
		}
		
		return packet;
	}
	
}