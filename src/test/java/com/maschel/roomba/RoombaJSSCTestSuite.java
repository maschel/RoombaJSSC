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
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RoombaJSSCSerial.class, SerialPortList.class})
public abstract class RoombaJSSCTestSuite {
    @Mock
    SerialPort serialPort;

    @InjectMocks
    public RoombaJSSCSerial roombaSerial;

    final String SERIAL_PORT = "/dev/tty.mock";

    final SerialPortException mockSerialPortException = new SerialPortException("mock", "mock", "mock");

    private boolean setUpIsDone = false;

    public void setUp() throws Exception {
        if (setUpIsDone) {
            return;
        }
        setUpIsDone = true;

        // Insert single run setup here
    }

    @Before
    public void setUpBefore() throws Exception {
        // Mock SerialPortList class
        PowerMockito.mockStatic(SerialPortList.class);
        // By default return a serial port
        Mockito.when(SerialPortList.getPortNames()).thenReturn(new String[] {SERIAL_PORT});
        // Replace new SerialPort instances with Mock
        PowerMockito.whenNew(SerialPort.class).withAnyArguments().thenReturn(serialPort);
        // Always allow to open the serial port
        Mockito.when(serialPort.openPort()).thenReturn(true);
        // Always return true on writeInt to serialPort
        Mockito.when(serialPort.writeInt(Matchers.anyInt())).thenReturn(true);
        // Always return true on writeBytes to serialPort
        Mockito.when(serialPort.writeBytes(Matchers.any(byte[].class))).thenReturn(true);
    }
}
