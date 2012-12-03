package com.trevorwhitney.xbee;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialReader {

  public void main(String[] args) {
    try {
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier("/dev/ttyUSB0");
			SerialPort serialPort = 
					(SerialPort) portId.open("Demo application", 5000);
			
			int baudRate = 57600;
			serialPort.setSerialPortParams(
			    baudRate,
			    SerialPort.DATABITS_8,
			    SerialPort.STOPBITS_1,
			    SerialPort.PARITY_NONE);
			
			 serialPort.setFlowControlMode(
		        SerialPort.FLOWCONTROL_NONE);
			 OutputStream outStream = serialPort.getOutputStream();
			 InputStream inStream = serialPort.getInputStream();
			 
			 while (inStream.available() > 0) {
				 System.out.println(inStream.read());
			 }
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}