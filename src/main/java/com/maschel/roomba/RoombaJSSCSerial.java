/*
 *  roombajssc
 *
 *  MIT License
 *
 *  Copyright (c) 2016 Geoffrey Mastenbroek, geoffrey@maschel.com
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.maschel.roomba;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class RoombaJSSCSerial extends RoombaJSSC {

    public SerialPort serialPort;

    private String portName = null;


    public String[] portList() {
        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length == 0) {
            log.error("No serial ports found!");
        }
        return portNames;
    }

    public boolean connect(String portId) {
        portName = portId;
        log.info("Connecting to port: '" + portName + "'.");

        connected = open_port();

        return connected;
    }

    public void disconnect() {
        try {
            if (serialPort != null) serialPort.closePort();
            log.info("Closing serial port: '" + portName + "'");
        } catch (SerialPortException ex) {
            log.error("Failed to close serial port: '" + portName + "', error: " + ex.getMessage());
        }
        serialPort = null;
    }

    public boolean send(byte[] bytes) {
        try {
            log.debug("Sending byte array, of size: '" + bytes.length + "' to serial port.");
            serialPort.writeBytes(bytes);
            return true;
        } catch(SerialPortException ex) {
            log.error("Failed to send data to serial port, error: " + ex.getMessage());
            return false;
        }

    }

    public boolean send(int b) {
        try {
            log.debug("Sending data: '" + b + "' to serial port.");
            serialPort.writeInt(b);
            return true;
        } catch (SerialPortException ex) {
            log.error("Failed to send data to serial port, error: " + ex.getMessage());
            return false;
        }
    }

    private boolean portExists(String portId) {
        // Check if requested port exists
        for (String portName: portList()) {
            if (portName.equals(portId))
                return true;
        }
        return false;
    }

    private boolean open_port() {
        if (portExists(portName)) {
            boolean success;
            serialPort = new SerialPort(portName);
            try {
                success = serialPort.openPort();

                serialPort.setParams(
                        SerialPort.BAUDRATE_115200,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

                // TODO: Add some event listeners

                log.info("Successfully opened serial port.");
                return success;

            } catch (SerialPortException ex) {
                log.error("Error opening serial port, error: " + ex.getMessage());
                serialPort = null;
                return false;
            }

        } else {
            log.error("Port: '" + portName + "' does not exist.");
            return false;
        }
    }
}
