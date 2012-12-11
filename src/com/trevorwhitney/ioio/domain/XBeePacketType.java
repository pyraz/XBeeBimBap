package com.trevorwhitney.ioio.domain;

public enum XBeePacketType {
	UNKNOWN(-1),
	MODEM_STATUS(0x8A),
	AT_COMMAND(0x08),
	AT_COMMAND_QUEUE_PARAMETER_VALUE(0x09),
	AT_COMMAND_RESPONSE(0x88),
	REMOTE_AT_COMMAND_REQUEST(0x17),
	REMOTE_COMMAND_RESPONSE(0x97),
	TX_REQUEST_64(0x00),
	TX_REQUEST_16(0x01),
	TX_STATUS(0x89),
	RX_PACKET_64(0x80),
	RX_PACKET_16(0x81),
	RX_PACKET_64_IO(0x82),
	RX_PACKET_16_IO(0x83);
	
	private final int apiId;
	
	XBeePacketType(int id) { this.apiId = id; }
	public int apiId() { return apiId; }
	
	public static XBeePacketType getTypeFromId(int apiId) {
		switch (apiId) {
		case 0x81:
			return XBeePacketType.RX_PACKET_16;
		default:
			return XBeePacketType.UNKNOWN;
		}
	}
}