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
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit test for RoombaJSSCSerial.
 */
public class RoombaJSSCSerialTest extends RoombaJSSCTestSuite
{
    /**
     * Test if portList method returns a list (instead of null)
     */
    @Test
    public void testEmptyPortListNotNull()
    {
        Mockito.when(SerialPortList.getPortNames()).thenReturn(new String[] {});
        String[] portList = roombaSerial.portList();
        assertNotNull("portList method should never return null (use empty list)", portList);
    }

    /**
     * Test if the connected variable is true and the serialPort
     * variable is set after connecting
     */
    @Test
    public void testConnectedAndSerialPortSet() throws SerialPortException {
        roombaSerial.connect(SERIAL_PORT);
        assertTrue("connected should be true after successful connect", roombaSerial.connected);
        assertNotNull("serialPort should not be null after successful connect", roombaSerial.serialPort);
    }

    /**
     * Trying to connect using a non existing serial port the should
     * connect method should return false
     */
    @Test
    public void testConnectingNonExistingPort() {
        final String non_existing_port = "/dev/i.do.not.exist";
        assertFalse("Connection() with non existing port should not be possible", roombaSerial.connect(non_existing_port));
        assertFalse("connected should be false if using non existing port", roombaSerial.connected);
        assertNull("serialPort should be null if using not existing port", roombaSerial.serialPort);
    }

    /**
     * Test if the connect method returns False if a Exception is thrown
     * by the openPort method.
     * @throws SerialPortException
     */
    @Test
    public void testConnectReturnsFalseOnException() throws SerialPortException {
        // Throw exception on openPort
        Mockito.when(serialPort.openPort()).thenThrow(mockSerialPortException);
        assertFalse("connect() should return False on exception in openPort()", roombaSerial.connect(SERIAL_PORT));
    }

    /**
     * Test if the disconnect method closes the serialPort and
     * sets the variable to null
     */
    @Test
    public void testDisconnected() {
        roombaSerial.connect(SERIAL_PORT);
        roombaSerial.disconnect();
        assertNull("serialPort should be set to null after disconnect", roombaSerial.serialPort);
    }

    /**
     * Test if the sendInt method returns True on a successful serial send, and false on failure.
     * @throws SerialPortException
     */
    @Test
    public void testSendIntReturn() throws SerialPortException {
        roombaSerial.connect(SERIAL_PORT);
        final int byteVal = 128;
        assertTrue("send(int) should return True on successful send", roombaSerial.send(byteVal));

        // Throw Exception
        Mockito.when(serialPort.writeInt(Matchers.anyInt())).thenThrow(mockSerialPortException);
        assertFalse("send(int) should return False on failed send", roombaSerial.send(byteVal));
    }

    /**
     * Test if the sendBytes method returns True on a successful send, and false on failure.
     * @throws SerialPortException
     */
    @Test
    public void testSendBytesReturn() throws SerialPortException {
        roombaSerial.connect(SERIAL_PORT);
        final byte[] bytes = {(byte)127, (byte)128 };
        assertTrue("send(byte[]) should return True on successful send", roombaSerial.send(bytes));

        // Throw Exception
        Mockito.when(serialPort.writeBytes(Matchers.any(byte[].class))).thenThrow(mockSerialPortException);
        assertFalse("send(byte[]) should return False on failed send", roombaSerial.send(bytes));
    }

    /**
     * Test if the serialEvent method processes the incoming serial (sensor) data as expected.
     * The event handler should write all incoming data to the sensorDataBuffer, until a all the
     * packets are received (80 bytes) then the buffer should be copied to the currentSensorData array.
     * @throws SerialPortException
     */
    @Test
    public void testSerialEventProcessing() throws SerialPortException {
        roombaSerial.connect(SERIAL_PORT);
        // Devide the input in multiple arrays to simulate serial input delay
        final int byte_array_length = 20; // SENSOR_PACKET_ALL_SIZE (80) / 4 data packets = 20 bytes per array
        byte[] bytes0 = new byte[byte_array_length];
        byte[] bytes1 = new byte[byte_array_length];
        byte[] bytes2 = new byte[byte_array_length];
        byte[] bytes3 = new byte[byte_array_length];
        for (int i=0; i < byte_array_length; i++) {
            // Set to some distinguishable values
            bytes0[i] = (byte)0x1f;
            bytes1[i] = (byte)0x3f;
            bytes2[i] = (byte)0x7f;
            bytes3[i] = (byte)0xff;
        }
        // Initialize the buffer
        roombaSerial.sensorDataBuffer = new byte[80];
        // Create expected buffer
        byte[] expect_buffer = new byte[80];
        // Create a mock RXCHAR serialPortEvent of byte_array_length bytes
        SerialPortEvent serialPortEvent = new SerialPortEvent(SERIAL_PORT, SerialPort.MASK_RXCHAR, byte_array_length);

        // Check if 0-20 bytes are written to the buffer
        System.arraycopy(bytes0, 0, expect_buffer, 0, byte_array_length);
        Mockito.when(serialPort.readBytes()).thenReturn(bytes0);
        roombaSerial.serialEvent(serialPortEvent);
        assertArrayEquals(roombaSerial.sensorDataBuffer, expect_buffer);
        assertEquals(roombaSerial.sensorDataBufferIndex, 20);

        // Check if 20-40 bytes are written to the buffer
        System.arraycopy(bytes1, 0, expect_buffer, byte_array_length, byte_array_length);
        Mockito.when(serialPort.readBytes()).thenReturn(bytes1);
        roombaSerial.serialEvent(serialPortEvent);
        assertArrayEquals(roombaSerial.sensorDataBuffer, expect_buffer);
        assertEquals(roombaSerial.sensorDataBufferIndex, 40);

        // Check if 40-60 bytes are written to the buffer
        System.arraycopy(bytes2, 0, expect_buffer, byte_array_length*2, byte_array_length);
        Mockito.when(serialPort.readBytes()).thenReturn(bytes2);
        roombaSerial.serialEvent(serialPortEvent);
        assertArrayEquals(roombaSerial.sensorDataBuffer, expect_buffer);
        assertEquals(roombaSerial.sensorDataBufferIndex, 60);

        // Check if 60-80 bytes are written to the buffer, the buffer is copied to
        // the currentSensorData array and the sensorDataBufferIndex is reset to 0.
        System.arraycopy(bytes3, 0, expect_buffer, byte_array_length*3, byte_array_length);
        Mockito.when(serialPort.readBytes()).thenReturn(bytes3);
        roombaSerial.serialEvent(serialPortEvent);
        assertArrayEquals(roombaSerial.sensorDataBuffer, expect_buffer);
        assertArrayEquals(roombaSerial.currentSensorData, expect_buffer);
        assertEquals(roombaSerial.sensorDataBufferIndex, 0);
    }
}
