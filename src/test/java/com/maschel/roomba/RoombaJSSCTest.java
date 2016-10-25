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

import com.maschel.roomba.song.RoombaNote;
import com.maschel.roomba.song.RoombaNoteDuration;
import com.maschel.roomba.song.RoombaSongNote;
import jssc.SerialPortException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for RoombaJSSC.
 */
public class RoombaJSSCTest extends RoombaJSSCTestSuite
{
    /**
     * Ensure that we are connected before each test.
     */
    @Before
    public void connect() {
        roombaSerial.connect(SERIAL_PORT);
        // Clear currentSensorData and make sure it's initialized
        roombaSerial.currentSensorData = new byte[80];
    }

    //region Roomba basic power commands tests

    /**
     * Test start command for correct OPC (128).
     * @throws SerialPortException
     */
    @Test
    public void testStartOPC() throws SerialPortException {
        roombaSerial.start();
        final int OPC_START = 128;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_START));
    }

    /**
     * Test startup command for correct OPC's (128, 131).
     * @throws SerialPortException
     */
    @Test
    public void testStartupOPC() throws SerialPortException {
        roombaSerial.startup();
        final int OPC_START = 128; final int OPC_SAFE = 131;
        final byte[] startup = { (byte)OPC_START, (byte)OPC_SAFE };
        Mockito.verify(serialPort).writeBytes(Mockito.argThat(new ArgumentMatcher<byte[]>() {
            @Override
            public boolean matches(Object o) {
                return Arrays.equals((byte[])o, startup);
            }
        }));
    }

    /**
     * Test stop command for correct OPC (173).
     * @throws SerialPortException
     */
    @Test
    public void testStopOPC() throws SerialPortException {
        roombaSerial.stop();
        final int OPC_STOP = 173;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_STOP));
    }

    /**
     * Test powerOff command for correct OPC (133).
     * @throws SerialPortException
     */
    @Test
    public void testPowerOffOPC() throws SerialPortException {
        roombaSerial.powerOff();
        final int OPC_POWER = 133;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_POWER));
    }

    /**
     * Test hardReset command for correct OPC (7).
     * @throws SerialPortException
     */
    @Test
    public void testHardResetOPC() throws SerialPortException {
        roombaSerial.hardReset();
        final int OPC_RESET = 7;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_RESET));
    }

    // endregion

    //region Roomba mode commands tests

    /**
     * Test safeMode command for correct OPC (131).
     * @throws SerialPortException
     */
    @Test
    public void testSafeModeOPC() throws SerialPortException {
        roombaSerial.safeMode();
        final int OPC_SAFE = 131;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_SAFE));
    }

    /**
     * Test fullMode command for correct OPC (132).
     * @throws SerialPortException
     */
    @Test
    public void testFullModeOPC() throws SerialPortException {
        roombaSerial.fullMode();
        final int OPC_FULL = 132;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_FULL));
    }

    // endregion

    // region Roomba cleaning commands tests

    /**
     * Test clean command for correct OPC (135).
     * @throws SerialPortException
     */
    @Test
    public void testCleanOPC() throws SerialPortException {
        roombaSerial.clean();
        final int OPC_CLEAN = 135;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_CLEAN));
    }

    /**
     * Test cleanMax command for correct OPC (136).
     * @throws SerialPortException
     */
    @Test
    public void testCleanMaxOPC() throws SerialPortException {
        roombaSerial.cleanMax();
        final int OPC_MAX_CLEAN = 136;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_MAX_CLEAN));
    }

    /**
     * Test cleanSpot command for correct OPC (134).
     * @throws SerialPortException
     */
    @Test
    public void testCleanSpotOPC() throws SerialPortException {
        roombaSerial.cleanSpot();
        final int OPC_SPOT = 134;
        Mockito.verify(serialPort).writeInt(Mockito.eq(OPC_SPOT));
    }

    /**
     * Test seekDock command for correct OPC (143).
     * @throws SerialPortException
     */
    @Test
    public void testSeekDockOPC() throws SerialPortException {
        roombaSerial.seekDock();
        final int OPC_FORCE_SEEKING_DOCK = 143;
        Mockito.verify(serialPort).writeInt(OPC_FORCE_SEEKING_DOCK);
    }

    /**
     * Test schedule command throws exception on invalid arguments
     */
    @Test
    public void testScheduleArgumentValidation() {
        assertTrue(assertScheduleInvalidArgument(0,0,0,0,0,0,0,0,0,0,0,0,0,0));                 // Success
        assertTrue(assertScheduleInvalidArgument(23,59,23,59,23,59,23,59,23,59,23,59,23,59));   // Success
        assertFalse(assertScheduleInvalidArgument(-1,0,-1,0,-1,0,-1,0,-1,0,-1,0,-1,0));         // Fail
        assertFalse(assertScheduleInvalidArgument(0,-1,0,-1,0,-1,0,-1,0,-1,0,-1,0,-1));         // Fail
        assertFalse(assertScheduleInvalidArgument(24,0,24,0,24,0,24,0,24,0,24,0,24,0));         // Fail
        assertFalse(assertScheduleInvalidArgument(0,60,0,60,0,60,0,60,0,60,0,60,0,60));         // Fail
    }

    // Helper method for testScheduleArgumentValidation that checks input arguments
    private boolean assertScheduleInvalidArgument(int sun_hour, int sun_min, int mon_hour, int mon_min, int tue_hour,
                                               int tue_min, int wed_hour, int wed_min, int thu_hour, int thu_min,
                                               int fri_hour, int fri_min, int sat_hour, int sat_min) {
        try {
            roombaSerial.schedule(false, false, false, false, false, false, false, sun_hour, sun_min, mon_hour,
                    mon_min, tue_hour, tue_min, wed_hour, wed_min, thu_hour, thu_min, fri_hour, fri_min, sat_hour,
                    sat_min);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of schedule for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testScheduleByteOutput() throws SerialPortException {
        final int OPC_SCHEDULE = 167;
        // None
        roombaSerial.schedule(false, false, false, false, false, false, false, 0,0,0,0,0,0,0,0,0,0,0,0,0,0);
        byte[] expect_none = { (byte)OPC_SCHEDULE,(byte)(0x0),0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_none));
        // Sunday
        roombaSerial.schedule(true, false, false, false, false, false, false, 10,10,0,0,0,0,0,0,0,0,0,0,0,0);
        byte[] expect_sun = { (byte)OPC_SCHEDULE,(byte)(0x1),10,10,0,0,0,0,0,0,0,0,0,0,0,0};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_sun));
        // Monday
        roombaSerial.schedule(false, true, false, false, false, false, false, 0,0,10,10,0,0,0,0,0,0,0,0,0,0);
        byte[] expect_mon = { (byte)OPC_SCHEDULE,(byte)(0x2),0,0,10,10,0,0,0,0,0,0,0,0,0,0};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_mon));
        // Tuesday
        roombaSerial.schedule(false, false, true, false, false, false, false, 0,0,0,0,10,10,0,0,0,0,0,0,0,0);
        byte[] expect_tue = { (byte)OPC_SCHEDULE,(byte)(0x4),0,0,0,0,10,10,0,0,0,0,0,0,0,0};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_tue));
        // Wednesday
        roombaSerial.schedule(false, false, false, true, false, false, false, 0,0,0,0,0,0,10,10,0,0,0,0,0,0);
        byte[] expect_wed = { (byte)OPC_SCHEDULE,(byte)(0x8),0,0,0,0,0,0,10,10,0,0,0,0,0,0};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_wed));
        // Thursday
        roombaSerial.schedule(false, false, false, false, true, false, false, 0,0,0,0,0,0,0,0,10,10,0,0,0,0);
        byte[] expect_thu = { (byte)OPC_SCHEDULE,(byte)(0x10),0,0,0,0,0,0,0,0,10,10,0,0,0,0};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_thu));
        // Friday
        roombaSerial.schedule(false, false, false, false, false, true, false, 0,0,0,0,0,0,0,0,0,0,10,10,0,0);
        byte[] expect_fri = { (byte)OPC_SCHEDULE,(byte)(0x20),0,0,0,0,0,0,0,0,0,0,10,10,0,0};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_fri));
        // Saturday
        roombaSerial.schedule(false, false, false, false, false, false, true, 0,0,0,0,0,0,0,0,0,0,0,0,10,10);
        byte[] expect_sat = { (byte)OPC_SCHEDULE,(byte)(0x40),0,0,0,0,0,0,0,0,0,0,0,0,10,10};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_sat));
        // All
        roombaSerial.schedule(true, true, true, true, true, true, true, 10,10,10,10,10,10,10,10,10,10,10,10,10,10);
        byte[] expect_all = { (byte)OPC_SCHEDULE,(byte)(0x7F),10,10,10,10,10,10,10,10,10,10,10,10,10,10};
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_all));
    }

    /**
     * Test setDayTime command throws exception on invalid arguments
     */
    @Test
    public void testSetDayTimeArgumentValidation() throws SerialPortException {
        assertTrue(assertSetDayTimeArgumentValidation(0, 0, 0));    // Success
        assertTrue(assertSetDayTimeArgumentValidation(6, 23, 59));  // Success
        assertFalse(assertSetDayTimeArgumentValidation(-1, 0, 0));  // Fail
        assertFalse(assertSetDayTimeArgumentValidation(0, -1, 0));  // Fail
        assertFalse(assertSetDayTimeArgumentValidation(0, 0, -1));  // Fail
        assertFalse(assertSetDayTimeArgumentValidation(7, 0, 0));   // Fail
        assertFalse(assertSetDayTimeArgumentValidation(0, 24, 0));  // Fail
        assertFalse(assertSetDayTimeArgumentValidation(0, 0, 60));  // Fail
    }

    // Helper method for testSetDayTimeArgumentValidation that checks input arguments
    private boolean assertSetDayTimeArgumentValidation(int day, int hour, int minute) {
        try {
            roombaSerial.setDayTime(day, hour, minute);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of setDayTime for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testSetDayTimeByteOutput() throws SerialPortException {
        final int OPC_SET_DAY_TIME = 168;
        byte[] expect = { (byte)OPC_SET_DAY_TIME, 5, 10, 10 }; // Day 5 (Friday), 10am, 10min
        roombaSerial.setDayTime(5, 10, 10);
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    //endregion

    //region Roomba Actuator commands tests

    /**
     * Test drive command throws exception on invalid arguments
     */
    @Test
    public void testDriveArgumentValidation() {
        assertTrue(assertDriveArgumentValidation(-500, -2000)); // Success
        assertTrue(assertDriveArgumentValidation(500, 2000));   // Success
        assertTrue(assertDriveArgumentValidation(0, 32767));    // Success
        assertTrue(assertDriveArgumentValidation(0, 32768));    // Success
        assertFalse(assertDriveArgumentValidation(-501, 0));    // Fail
        assertFalse(assertDriveArgumentValidation(0, -2001));   // Fail
        assertFalse(assertDriveArgumentValidation(501, 0));     // Fail
        assertFalse(assertDriveArgumentValidation(0, 2001));    // Fail
    }

    // Helper method for testDriveArgumentValidation that checks input arguments
    private boolean assertDriveArgumentValidation(int velocity, int radius) {
        try {
            roombaSerial.drive(velocity, radius);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of drive for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testDriveByteOutput() throws SerialPortException {
        final int OPC_DRIVE = 137;
        final int velocity = 200; final int radius = 200;
        byte[] expect = { (byte)OPC_DRIVE, (byte)(velocity >>> 8), (byte)velocity, (byte)(radius >>> 8), (byte)radius };
        roombaSerial.drive(200, 200);
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    /**
     * Test driveDirect command throws exception on invalid arguments
     */
    @Test
    public void testDriveDirectArgumentValidation() {
        assertTrue(assertDriveDirectArgumentValidation(-500, -500));    // Success
        assertTrue(assertDriveDirectArgumentValidation(500, 500));      // Success
        assertFalse(assertDriveDirectArgumentValidation(-501, 0));      // Fail
        assertFalse(assertDriveDirectArgumentValidation(0, -501));      // Fail
        assertFalse(assertDriveDirectArgumentValidation(501, 0));       // Fail
        assertFalse(assertDriveDirectArgumentValidation(0, 501));       // Fail
    }

    // Helper method for testDriveDirectArgumentValidation that checks input arguments
    private boolean assertDriveDirectArgumentValidation(int rightVelocity, int leftVelocity) {
        try {
            roombaSerial.driveDirect(rightVelocity, leftVelocity);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of driveDirect for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testDriveDirectByteOutput() throws SerialPortException {
        final int OPC_DRIVE_WHEELS = 145;
        final int velocity_right = 200; final int velocity_left = 200;
        byte[] expect = { (byte)OPC_DRIVE_WHEELS, (byte)(velocity_right >>> 8), (byte)velocity_right,
                (byte)(velocity_left >>> 8), (byte)velocity_left };
        roombaSerial.driveDirect(velocity_right, velocity_left);
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    /**
     * Test drivePWM command throws exception on invalid arguments
     */
    @Test
    public void testDrivePWMArgumentValidation() {
        assertTrue(assertDrivePWMArgumentValidation(-100, -100));   // Success
        assertTrue(assertDrivePWMArgumentValidation(100, 100));     // Success
        assertFalse(assertDrivePWMArgumentValidation(-101, 0));     // Fail
        assertFalse(assertDrivePWMArgumentValidation(0, -101));     // Fail
        assertFalse(assertDrivePWMArgumentValidation(101, 0));      // Fail
        assertFalse(assertDrivePWMArgumentValidation(0, 101));      // Fail
    }

    // Helper method for testDrivePWMArgumentValidation that checks input arguments
    private boolean assertDrivePWMArgumentValidation(int rightPWM, int leftPWM) {
        try {
            roombaSerial.drivePWM(rightPWM, leftPWM);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of drivePWM for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testDrivePWMByteOutput() throws SerialPortException {
        final int OPC_DRIVE_PWM = 146;
        final int relRightPWM = 0xff * 80/100;  // 80% of max power (0xff)
        final int relLeftPWM = 0xff * 80/100;   // 80% of max power (0xff)
        byte[] expect = { (byte)OPC_DRIVE_PWM, (byte)(relRightPWM >>> 8), (byte)relRightPWM,
                (byte)(relLeftPWM >>> 8), (byte)relLeftPWM };
        roombaSerial.drivePWM(80, 80);
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    /**
     * Test the byte[] output of motors for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testMotorsByteOutput() throws SerialPortException {
        final int OPC_MOTORS = 138;
        // None
        roombaSerial.motors(false, false, false, false, false);
        byte[] expect_none = { (byte)OPC_MOTORS, (byte)(0x0) };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_none));
        // Side brush
        roombaSerial.motors(true, false, false, false, false);
        byte[] expect_side_brush = { (byte)OPC_MOTORS, (byte)(0x1) };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_side_brush));
        // Vacuum
        roombaSerial.motors(false, true, false, false, false);
        byte[] expect_vacuum = { (byte)OPC_MOTORS, (byte)(0x2) };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_vacuum));
        // Main brush
        roombaSerial.motors(false, false, true, false, false);
        byte[] expect_main_brush = { (byte)OPC_MOTORS, (byte)(0x4) };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_main_brush));
        // Side brush Clockwise
        roombaSerial.motors(false, false, false, true, false);
        byte[] expect_side_brush_cw = { (byte)OPC_MOTORS, (byte)(0x8) };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_side_brush_cw));
        // Main brush outward
        roombaSerial.motors(false, false, false, false, true);
        byte[] expect_main_brush_ow = { (byte)OPC_MOTORS, (byte)(0x10) };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_main_brush_ow));
        // Side all
        roombaSerial.motors(true, true, true, true, true);
        byte[] expect_all = { (byte)OPC_MOTORS, (byte)(0x1F) };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_all));
    }

    /**
     * Test motorsPWM command throws exception on invalid arguments
     */
    @Test
    public void testMotorsPWMArgumentValidation() {
        assertTrue(assertMotorsPWMArgumentValidation(-100, -100, 0));   // Success
        assertTrue(assertMotorsPWMArgumentValidation(100, 100, 100));   // Success
        assertFalse(assertMotorsPWMArgumentValidation(-101, 0, 0));     // Fail
        assertFalse(assertMotorsPWMArgumentValidation(0, -101, 0));     // Fail
        assertFalse(assertMotorsPWMArgumentValidation(0, 0, -101));     // Fail
        assertFalse(assertMotorsPWMArgumentValidation(101, 0, 0));      // Fail
        assertFalse(assertMotorsPWMArgumentValidation(0, 101, 0));      // Fail
        assertFalse(assertMotorsPWMArgumentValidation(0, 0, 101));      // Fail
    }

    // Helper method for testMotorsPWMArgumentValidation that checks input arguments
    private boolean assertMotorsPWMArgumentValidation(int mainBrushPWM, int sideBrushPWM, int vacuumPWM) {
        try {
            roombaSerial.motorsPWM(mainBrushPWM, sideBrushPWM, vacuumPWM);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of motorsPWM for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testMotorsPWMByteOutput() throws SerialPortException {
        final int OPC_PWM_MOTORS = 144;
        final int relMainBrushPWM = 0x7f * 80/100; // 80% of max power (0xff)
        final int relSideBrushPWM = 0x7f * 80/100; // 80% of max power (0xff)
        final int relVacuumPWM    = 0x7f * 80/100; // 80% of max power (0xff)
        byte[] expect = { (byte)OPC_PWM_MOTORS, (byte)relMainBrushPWM, (byte)relSideBrushPWM, (byte)relVacuumPWM };
        roombaSerial.motorsPWM(80, 80, 80);
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    /**
     * Test leds command throws exception on invalid arguments
     */
    @Test
    public void testLedsArgumentValidation() {
        assertTrue(assertLedsArgumentValidation(0, 0));     // Success
        assertTrue(assertLedsArgumentValidation(100, 100)); // Success
        assertFalse(assertLedsArgumentValidation(-1, 0));   // Fail
        assertFalse(assertLedsArgumentValidation(0, -1));   // Fail
        assertFalse(assertLedsArgumentValidation(101, 0));  // Fail
        assertFalse(assertLedsArgumentValidation(0, 101));  // Fail

    }

    // Helper method for testLedsArgumentValidation that checks input arguments
    private boolean assertLedsArgumentValidation(int powerColor, int powerIntensity) {
        try {
            roombaSerial.leds(true, true, true, true, powerColor, powerIntensity);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of leds for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testLedsByteOutput() throws SerialPortException {
        final int OPC_LEDS = 139;
        final int relPowerRedColor = (0xff * 80/100); // 80% red of max (0xff)
        final int relPowerIntensity = (0xff * 80/100); // 80% of max intensity (0xff)
        // None
        roombaSerial.leds(false, false, false, false, 0, 0);
        byte[] expect_none = { (byte)OPC_LEDS, (byte)0x0, (byte)0x0, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_none));
        // Debris
        roombaSerial.leds(true, false, false, false, 80, 80);
        byte[] expect_debris = { (byte)OPC_LEDS, (byte)0x1, (byte)relPowerRedColor, (byte)relPowerIntensity };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_debris));
        // Spot
        roombaSerial.leds(false, true, false, false, 80, 80);
        byte[] expect_spot = { (byte)OPC_LEDS, (byte)0x2, (byte)relPowerRedColor, (byte)relPowerIntensity };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_spot));
        // Dock
        roombaSerial.leds(false, false, true, false, 80, 80);
        byte[] expect_dock = { (byte)OPC_LEDS, (byte)0x4, (byte)relPowerRedColor, (byte)relPowerIntensity };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_dock));
        // Check robot
        roombaSerial.leds(false, false, false, true, 80, 80);
        byte[] expect_check_robot = { (byte)OPC_LEDS, (byte)0x8, (byte)relPowerRedColor, (byte)relPowerIntensity };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_check_robot));
        // All
        roombaSerial.leds(true, true, true, true, 100, 100);
        byte[] expect_all = { (byte)OPC_LEDS, (byte)0xF, (byte)0xff, (byte)0xff };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_all));
    }

    /**
     * Test the byte[] output of schedulingLeds for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testSchedulingLedsByteOutput() throws SerialPortException {
        final int OPC_SCHEDULING_LEDS = 162;
        // None
        roombaSerial.schedulingLeds(false, false, false, false, false, false, false, false, false, false, false, false);
        byte[] expect_none = { (byte)OPC_SCHEDULING_LEDS, (byte)0x0, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_none));
        // Sun
        roombaSerial.schedulingLeds(true, false, false, false, false, false, false, false, false, false, false, false);
        byte[] expect_sun = { (byte)OPC_SCHEDULING_LEDS, (byte)0x1, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_sun));
        // Mon
        roombaSerial.schedulingLeds(false, true, false, false, false, false, false, false, false, false, false, false);
        byte[] expect_mon = { (byte)OPC_SCHEDULING_LEDS, (byte)0x2, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_mon));
        // Tue
        roombaSerial.schedulingLeds(false, false, true, false, false, false, false, false, false, false, false, false);
        byte[] expect_tue = { (byte)OPC_SCHEDULING_LEDS, (byte)0x4, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_tue));
        // Wed
        roombaSerial.schedulingLeds(false, false, false, true, false, false, false, false, false, false, false, false);
        byte[] expect_wed = { (byte)OPC_SCHEDULING_LEDS, (byte)0x8, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_wed));
        // Thu
        roombaSerial.schedulingLeds(false, false, false, false, true, false, false, false, false, false, false, false);
        byte[] expect_thu = { (byte)OPC_SCHEDULING_LEDS, (byte)0x10, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_thu));
        // Fri
        roombaSerial.schedulingLeds(false, false, false, false, false, true, false, false, false, false, false, false);
        byte[] expect_fri = { (byte)OPC_SCHEDULING_LEDS, (byte)0x20, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_fri));
        // Sat
        roombaSerial.schedulingLeds(false, false, false, false, false, false, true, false, false, false, false, false);
        byte[] expect_sat = { (byte)OPC_SCHEDULING_LEDS, (byte)0x40, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_sat));
        // Colon
        roombaSerial.schedulingLeds(false, false, false, false, false, false, false, true, false, false, false, false);
        byte[] expect_colon = { (byte)OPC_SCHEDULING_LEDS, (byte)0x0, (byte)0x1 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_colon));
        // PM
        roombaSerial.schedulingLeds(false, false, false, false, false, false, false, false, true, false, false, false);
        byte[] expect_pm = { (byte)OPC_SCHEDULING_LEDS, (byte)0x0, (byte)0x2 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_pm));
        // AM
        roombaSerial.schedulingLeds(false, false, false, false, false, false, false, false, false, true, false, false);
        byte[] expect_am = { (byte)OPC_SCHEDULING_LEDS, (byte)0x0, (byte)0x4 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_am));
        // Clock
        roombaSerial.schedulingLeds(false, false, false, false, false, false, false, false, false, false, true, false);
        byte[] expect_clock = { (byte)OPC_SCHEDULING_LEDS, (byte)0x0, (byte)0x8 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_clock));
        // Schedule
        roombaSerial.schedulingLeds(false, false, false, false, false, false, false, false, false, false, false, true);
        byte[] expect_schedule = { (byte)OPC_SCHEDULING_LEDS, (byte)0x0, (byte)0x10 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_schedule));
        // All
        roombaSerial.schedulingLeds(true, true, true, true, true, true, true, true, true, true, true, true);
        byte[] expect_all = { (byte)OPC_SCHEDULING_LEDS, (byte)0x7f, (byte)0x1f };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_all));
    }

    /**
     * Test digitLedsAscii command throws exception on invalid arguments
     */
    @Test
    public void testDigitLedsAsciiArgumentValidation() {
        // SUCCESS: Test lowest printable ASCII character (32)Space
        assertTrue(assertDigitLedsAsciiArgumentValidation(' ', ' ', ' ', ' '));
        // SUCCESS: Test highest printable ASCII character (126)~
        assertTrue(assertDigitLedsAsciiArgumentValidation('~', '~', '~', '~'));
        // FAIL: Non printable (31)
        assertFalse(assertDigitLedsAsciiArgumentValidation((char)31, ' ', ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', (char)31, ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', (char)31, ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', ' ', (char)31));
        // FAIL: Exclude (42)* character
        assertFalse(assertDigitLedsAsciiArgumentValidation('*', ' ', ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', '*', ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', '*', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', ' ', '*'));
        // FAIL: Exclude (43)+ character
        assertFalse(assertDigitLedsAsciiArgumentValidation('+', ' ', ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', '+', ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', '+', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', ' ', '+'));
        // FAIL: Exclude (64)@ character
        assertFalse(assertDigitLedsAsciiArgumentValidation('@', ' ', ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', '@', ' ', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', '@', ' '));
        assertFalse(assertDigitLedsAsciiArgumentValidation(' ', ' ', ' ', '@'));

    }

    // Helper method for testLedsArgumentValidation that checks input arguments
    private boolean assertDigitLedsAsciiArgumentValidation(char char0, char char1, char char2, char char3) {
        try {
            roombaSerial.digitLedsAscii(char0, char1, char2, char3);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of buttons for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testButtonsByteOutput() throws SerialPortException {
        final int OPC_BUTTONS = 165;
        // None
        roombaSerial.buttons(false, false, false, false, false, false, false, false);
        byte[] expect_none = { (byte)OPC_BUTTONS, (byte)0x0 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_none));
        // Clean
        roombaSerial.buttons(true, false, false, false, false, false, false, false);
        byte[] expect_clean = { (byte)OPC_BUTTONS, (byte)0x1 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_clean));
        // Spot
        roombaSerial.buttons(false, true, false, false, false, false, false, false);
        byte[] expect_spot = { (byte)OPC_BUTTONS, (byte)0x2 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_spot));
        // Dock
        roombaSerial.buttons(false, false, true, false, false, false, false, false);
        byte[] expect_dock = { (byte)OPC_BUTTONS, (byte)0x4 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_dock));
        // Minute
        roombaSerial.buttons(false, false, false, true, false, false, false, false);
        byte[] expect_minute = { (byte)OPC_BUTTONS, (byte)0x8 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_minute));
        // Hour
        roombaSerial.buttons(false, false, false, false, true, false, false, false);
        byte[] expect_hour = { (byte)OPC_BUTTONS, (byte)0x10 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_hour));
        // Day
        roombaSerial.buttons(false, false, false, false, false, true, false, false);
        byte[] expect_day = { (byte)OPC_BUTTONS, (byte)0x20 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_day));
        // Schedule
        roombaSerial.buttons(false, false, false, false, false, false, true, false);
        byte[] expect_schedule = { (byte)OPC_BUTTONS, (byte)0x40 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_schedule));
        // Clock
        roombaSerial.buttons(false, false, false, false, false, false, false, true);
        byte[] expect_clock = { (byte)OPC_BUTTONS, (byte)0x80 };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_clock));
        // All
        roombaSerial.buttons(true, true, true, true, true, true, true, true);
        byte[] expect_all = { (byte)OPC_BUTTONS, (byte)0xff };
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect_all));
    }

    /**
     * Test song command throws exception on invalid arguments
     */
    @Test
    public void testSongArgumentValidation() {
        RoombaSongNote[] maxNotes = new RoombaSongNote[16];
        for (int i = 0; i < 16; i++) {
            maxNotes[i] = new RoombaSongNote(RoombaNote.A0, RoombaNoteDuration.EightNote);
        }
        RoombaSongNote[] tooManyNotes = new RoombaSongNote[17];
        for (int i = 0; i < 17; i++) {
            tooManyNotes[i] = new RoombaSongNote(RoombaNote.A0, RoombaNoteDuration.EightNote);
        }
        assertTrue(assertSongArgumentValidation(0, new RoombaSongNote[]{}, 60));    // Success
        assertTrue(assertSongArgumentValidation(4, maxNotes, 800));                 // Success
        assertFalse(assertSongArgumentValidation(-1, new RoombaSongNote[]{}, 60));  // Fail
        assertFalse(assertSongArgumentValidation(0, new RoombaSongNote[]{}, 59));   // Fail
        assertFalse(assertSongArgumentValidation(5, new RoombaSongNote[]{}, 60));   // Fail
        assertFalse(assertSongArgumentValidation(0, tooManyNotes, 60));             // Fail
        assertFalse(assertSongArgumentValidation(0, new RoombaSongNote[]{}, 801));  // Fail
    }

    // Helper method for testLedsArgumentValidation that checks input arguments
    private boolean assertSongArgumentValidation(int songNumber, RoombaSongNote[] notes, int tempo) {
        try {
            roombaSerial.song(songNumber, notes, tempo);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of song for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testSongByteOutput() throws SerialPortException {
        final int OPC_SONG = 140;
        final int tempo = 120;
        final int songNumber = 1;
        final RoombaSongNote[] notes = {
                new RoombaSongNote(RoombaNote.A0, RoombaNoteDuration.EightNote),
                new RoombaSongNote(RoombaNote.A1, RoombaNoteDuration.QuarterNote)
        };
        final byte[] notes_bytes = RoombaSongNote.songNotesToBytes(notes, tempo);
        final byte[] expect = { (byte)OPC_SONG, (byte)songNumber, (byte)notes.length,
                notes_bytes[0], notes_bytes[1], notes_bytes[2], notes_bytes[3]
        };
        roombaSerial.song(songNumber, notes, tempo);
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    /**
     * Test play command throws exception on invalid arguments
     */
    @Test
    public void testPlayArgumentValidation() {
        assertTrue(assertPlayArgumentValidation(0));    // Success
        assertTrue(assertPlayArgumentValidation(4));    // Success
        assertFalse(assertPlayArgumentValidation(-1));  // Fail
        assertFalse(assertPlayArgumentValidation(5));   // Fail
    }

    // Helper method for testLedsArgumentValidation that checks input arguments
    private boolean assertPlayArgumentValidation(int songNumber) {
        try {
            roombaSerial.play(songNumber);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Test the byte[] output of play for correct OPC and byte value's for given arguments
     * @throws SerialPortException
     */
    @Test
    public void testPlayByteOutput() throws SerialPortException {
        final int OPC_PLAY = 141;
        final int songNumber = 2;
        final byte[] expect = { (byte)OPC_PLAY, songNumber };
        roombaSerial.play(songNumber);
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    //endregion

    //region Roomba sensor commands tests

    /**
     * Test if the updateSensors method throws an exception if invoked too fast.
     */
    @Test(expected=RuntimeException.class)
    public void testUpdateSensorsInterval() {
        roombaSerial.updateSensors();
        roombaSerial.updateSensors(); // Should throw RuntimeException
    }

    /**
     * Test byte[] output of updateSensors for correct OPC and SENSOR_PACKET_ID
     * @throws SerialPortException
     */
    @Test
    public void testUpdateSensors() throws SerialPortException {
        final int OPC_QUERY = 142;
        final int SENSOR_PACKET_ALL = 100;
        byte[] expect = { (byte)OPC_QUERY, (byte)SENSOR_PACKET_ALL };
        roombaSerial.updateSensors();
        Mockito.verify(serialPort).writeBytes(Mockito.eq(expect));
    }

    /**
     * Test if the updateSensors method resets sensorDataBufferIndex
     */
    @Test
    public void testUpdateSensorsResetsDataBufferIndex() {
        roombaSerial.sensorDataBufferIndex = 40;
        roombaSerial.updateSensors();
        assertEquals(roombaSerial.sensorDataBufferIndex, 0);
    }

    //endregion

    //region Roomba sensor value getters tests

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testBumpRight() {
        assertFalse("bumpRight() should return false on 0 sensor bit", roombaSerial.bumpRight());
        roombaSerial.currentSensorData[0] = 0x1;
        assertTrue("bumpRight() should return true on 1 sensor bit", roombaSerial.bumpRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testBumpLeft() {
        assertFalse("bumpLeft() should return false on 0 sensor bit", roombaSerial.bumpLeft());
        roombaSerial.currentSensorData[0] = 0x2;
        assertTrue("bumpLeft() should return true on 1 sensor bit", roombaSerial.bumpLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testWheelDropRight() {
        assertFalse("wheelDropRight() should return false on 0 sensor bit", roombaSerial.wheelDropRight());
        roombaSerial.currentSensorData[0] = 0x4;
        assertTrue("wheelDropRight() should return true on 1 sensor bit", roombaSerial.wheelDropRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testWheelDropLeft() {
        assertFalse("wheelDropLeft() should return false on 0 sensor bit", roombaSerial.wheelDropLeft());
        roombaSerial.currentSensorData[0] = 0x8;
        assertTrue("wheelDropLeft() should return true on 1 sensor bit", roombaSerial.wheelDropLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testWall() {
        assertFalse("wall() should return false on 0 sensor bit", roombaSerial.wall());
        roombaSerial.currentSensorData[1] = 0x1;
        assertTrue("wall() should return true on 1 sensor bit", roombaSerial.wall());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffLeft() {
        assertFalse("cliffLeft() should return false on 0 sensor bit", roombaSerial.cliffLeft());
        roombaSerial.currentSensorData[2] = 0x1;
        assertTrue("cliffLeft() should return true on 1 sensor bit", roombaSerial.cliffLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffFrontLeft() {
        assertFalse("cliffFrontLeft() should return false on 0 sensor bit", roombaSerial.cliffFrontLeft());
        roombaSerial.currentSensorData[3] = 0x1;
        assertTrue("cliffFrontLeft() should return true on 1 sensor bit", roombaSerial.cliffFrontLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffFrontRight() {
        assertFalse("cliffFrontRight() should return false on 0 sensor bit", roombaSerial.cliffFrontRight());
        roombaSerial.currentSensorData[4] = 0x1;
        assertTrue("cliffFrontRight() should return true on 1 sensor bit", roombaSerial.cliffFrontRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffRight() {
        assertFalse("cliffRight() should return false on 0 sensor bit", roombaSerial.cliffRight());
        roombaSerial.currentSensorData[5] = 0x1;
        assertTrue("cliffRight() should return true on 1 sensor bit", roombaSerial.cliffRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testVirtualWall() {
        assertFalse("virtualWall() should return false on 0 sensor bit", roombaSerial.virtualWall());
        roombaSerial.currentSensorData[6] = 0x1;
        assertTrue("virtualWall() should return true on 1 sensor bit", roombaSerial.virtualWall());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testSideBrushOvercurrent() {
        assertFalse("sideBrushOvercurrent() should return false on 0 sensor bit", roombaSerial.sideBrushOvercurrent());
        roombaSerial.currentSensorData[7] = 0x1;
        assertTrue("sideBrushOvercurrent() should return true on 1 sensor bit", roombaSerial.sideBrushOvercurrent());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testMainBrushOvercurrent() {
        assertFalse("mainBrushOvercurrent() should return false on 0 sensor bit", roombaSerial.mainBrushOvercurrent());
        roombaSerial.currentSensorData[7] = 0x4;
        assertTrue("mainBrushOvercurrent() should return true on 1 sensor bit", roombaSerial.mainBrushOvercurrent());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testWheelOvercurrentRight() {
        assertFalse("wheelOvercurrentRight() should return false on 0 sensor bit",
                roombaSerial.wheelOvercurrentRight());
        roombaSerial.currentSensorData[7] = 0x8;
        assertTrue("wheelOvercurrentRight() should return true on 1 sensor bit",
                roombaSerial.wheelOvercurrentRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testWheelOvercurrentLeft() {
        assertFalse("wheelOvercurrentLeft() should return false on 0 sensor bit",
                roombaSerial.wheelOvercurrentLeft());
        roombaSerial.currentSensorData[7] = 0x10;
        assertTrue("wheelOvercurrentLeft() should return true on 1 sensor bit",
                roombaSerial.wheelOvercurrentLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testDirtDetectLevel() {
        assertEquals("dirtDetectLevel() should return 0 on 0x0", 0, roombaSerial.dirtDetectLevel());
        roombaSerial.currentSensorData[8] = (byte)0x80; // 128
        assertEquals("dirtDetectLevel() should return 128 on 0x80", 128, roombaSerial.dirtDetectLevel());
        roombaSerial.currentSensorData[8] = (byte)0xff; // 255
        assertEquals("dirtDetectLevel() should return 255 on 0xff", 255, roombaSerial.dirtDetectLevel());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testInfraredCharacterOmni() {
        assertEquals("infraredCharacterOmni() should return 0 on 0x0", 0, roombaSerial.infraredCharacterOmni());
        roombaSerial.currentSensorData[9] = (byte)0x80; // 128
        assertEquals("infraredCharacterOmni() should return 128 on 0x80", 128, roombaSerial.infraredCharacterOmni());
        roombaSerial.currentSensorData[9] = (byte)0xff; // 255
        assertEquals("infraredCharacterOmni() should return 255 on 0xff", 255, roombaSerial.infraredCharacterOmni());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testInfraredCharacterLeft() {
        assertEquals("infraredCharacterLeft() should return 0 on 0x0", 0, roombaSerial.infraredCharacterLeft());
        roombaSerial.currentSensorData[69] = (byte)0x80; // 128
        assertEquals("infraredCharacterLeft() should return 128 on 0x80", 128, roombaSerial.infraredCharacterLeft());
        roombaSerial.currentSensorData[69] = (byte)0xff; // 255
        assertEquals("infraredCharacterLeft() should return 255 on 0xff", 255, roombaSerial.infraredCharacterLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testInfraredCharacterRight() {
        assertEquals("infraredCharacterRight() should return 0 on 0x0", 0, roombaSerial.infraredCharacterRight());
        roombaSerial.currentSensorData[70] = (byte)0x80; // 128
        assertEquals("infraredCharacterRight() should return 128 on 0x80", 128, roombaSerial.infraredCharacterRight());
        roombaSerial.currentSensorData[70] = (byte)0xff; // 255
        assertEquals("infraredCharacterRight() should return 255 on 0xff", 255, roombaSerial.infraredCharacterRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonCleanPressed() {
        assertFalse("buttonCleanPressed() should return false on 0 sensor bit", roombaSerial.buttonCleanPressed());
        roombaSerial.currentSensorData[11] = 0x1;
        assertTrue("buttonCleanPressed() should return true on 1 sensor bit", roombaSerial.buttonCleanPressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonSpotPressed() {
        assertFalse("buttonSpotPressed() should return false on 0 sensor bit", roombaSerial.buttonSpotPressed());
        roombaSerial.currentSensorData[11] = 0x2;
        assertTrue("buttonSpotPressed() should return true on 1 sensor bit", roombaSerial.buttonSpotPressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonDockPressed() {
        assertFalse("buttonDockPressed() should return false on 0 sensor bit", roombaSerial.buttonDockPressed());
        roombaSerial.currentSensorData[11] = 0x4;
        assertTrue("buttonDockPressed() should return true on 1 sensor bit", roombaSerial.buttonDockPressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonMinutePressed() {
        assertFalse("buttonMinutePressed() should return false on 0 sensor bit", roombaSerial.buttonMinutePressed());
        roombaSerial.currentSensorData[11] = 0x8;
        assertTrue("buttonMinutePressed() should return true on 1 sensor bit", roombaSerial.buttonMinutePressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonHourPressed() {
        assertFalse("buttonHourPressed() should return false on 0 sensor bit", roombaSerial.buttonHourPressed());
        roombaSerial.currentSensorData[11] = 0x10;
        assertTrue("buttonHourPressed() should return true on 1 sensor bit", roombaSerial.buttonHourPressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonDayPressed() {
        assertFalse("buttonDayPressed() should return false on 0 sensor bit", roombaSerial.buttonDayPressed());
        roombaSerial.currentSensorData[11] = 0x20;
        assertTrue("buttonDayPressed() should return true on 1 sensor bit", roombaSerial.buttonDayPressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonSchedulePressed() {
        assertFalse("buttonSchedulePressed() should return false on 0 sensor bit",
                roombaSerial.buttonSchedulePressed());
        roombaSerial.currentSensorData[11] = 0x40;
        assertTrue("buttonSchedulePressed() should return true on 1 sensor bit", roombaSerial.buttonSchedulePressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testButtonClockPressed() {
        assertFalse("buttonClockPressed() should return false on 0 sensor bit", roombaSerial.buttonClockPressed());
        roombaSerial.currentSensorData[11] = (byte)0x80;
        assertTrue("buttonClockPressed() should return true on 1 sensor bit", roombaSerial.buttonClockPressed());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testDistanceTraveled() {
        assertEquals("distanceTraveled() should return 0 on 0x0", 0, roombaSerial.distanceTraveled());
        roombaSerial.currentSensorData[12] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[13] = (byte)0xff; // Low byte
        assertEquals("distanceTraveled() should return 32767 on 0x7fff", 32767, roombaSerial.distanceTraveled());
        roombaSerial.currentSensorData[12] = (byte)0x80;
        roombaSerial.currentSensorData[13] = (byte)0x00;
        assertEquals("distanceTraveled() should return -32768 on 0x8000", -32768, roombaSerial.distanceTraveled());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testAngleTurned() {
        assertEquals("angleTurned() should return 0 on 0x0", 0, roombaSerial.angleTurned());
        roombaSerial.currentSensorData[14] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[15] = (byte)0xff; // Low byte
        assertEquals("angleTurned() should return 32767 on 0x7fff", 32767, roombaSerial.angleTurned());
        roombaSerial.currentSensorData[14] = (byte)0x80;
        roombaSerial.currentSensorData[15] = (byte)0x00;
        assertEquals("angleTurned() should return -32768 on 0x8000", -32768, roombaSerial.angleTurned());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testChargingState() {
        assertEquals("chargingState() should return 0 0n 0x0", 0, roombaSerial.chargingState());
        roombaSerial.currentSensorData[16] = 0x5;
        assertEquals("chargingState() should return 5 0n 0x5", 5, roombaSerial.chargingState());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testBatteryVoltage() {
        assertEquals("batteryVoltage() should return 0 on 0x0", 0, roombaSerial.batteryVoltage());
        roombaSerial.currentSensorData[17] = (byte)0x00; // High byte
        roombaSerial.currentSensorData[18] = (byte)0xff; // Low byte
        assertEquals("batteryVoltage() should return 255 on 0x00ff", 255, roombaSerial.batteryVoltage());
        roombaSerial.currentSensorData[17] = (byte)0xff; // High byte
        roombaSerial.currentSensorData[18] = (byte)0xff; // Low byte
        assertEquals("batteryVoltage() should return 65535 on 0xffff", 65535, roombaSerial.batteryVoltage());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testBatteryCurrent() {
        assertEquals("batteryCurrent() should return 0 on 0x0", 0, roombaSerial.batteryCurrent());
        roombaSerial.currentSensorData[19] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[20] = (byte)0xff; // Low byte
        assertEquals("batteryCurrent() should return 32767 on 0x7fff", 32767, roombaSerial.batteryCurrent());
        roombaSerial.currentSensorData[19] = (byte)0x80;
        roombaSerial.currentSensorData[20] = (byte)0x00;
        assertEquals("batteryCurrent() should return -32768 on 0x8000", -32768, roombaSerial.batteryCurrent());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testBatteryTemperature() {
        assertEquals("batteryTemperature() should return 0 on 0x0", 0, roombaSerial.batteryTemperature());
        roombaSerial.currentSensorData[21] = (byte)0x7f;
        assertEquals("batteryTemperature() should return 127 on 0x7f", 127, roombaSerial.batteryTemperature());
        roombaSerial.currentSensorData[21] = (byte)0x80;
        assertEquals("batteryTemperature() should return -128 on 0x80", -128, roombaSerial.batteryTemperature());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testBatteryCharge() {
        assertEquals("batteryCharge() should return 0 on 0x0", 0, roombaSerial.batteryCharge());
        roombaSerial.currentSensorData[22] = (byte)0x00; // High byte
        roombaSerial.currentSensorData[23] = (byte)0xff; // Low byte
        assertEquals("batteryCharge() should return 255 on 0x00ff", 255, roombaSerial.batteryCharge());
        roombaSerial.currentSensorData[22] = (byte)0xff; // High byte
        roombaSerial.currentSensorData[23] = (byte)0xff; // Low byte
        assertEquals("batteryCharge() should return 65535 on 0xffff", 65535, roombaSerial.batteryCharge());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testBatteryCapacity() {
        assertEquals("batteryCapacity() should return 0 on 0x0", 0, roombaSerial.batteryCapacity());
        roombaSerial.currentSensorData[24] = (byte)0x00; // High byte
        roombaSerial.currentSensorData[25] = (byte)0xff; // Low byte
        assertEquals("batteryCapacity() should return 255 on 0x00ff", 255, roombaSerial.batteryCapacity());
        roombaSerial.currentSensorData[24] = (byte)0xff; // High byte
        roombaSerial.currentSensorData[25] = (byte)0xff; // Low byte
        assertEquals("batteryCapacity() should return 65535 on 0xffff", 65535, roombaSerial.batteryCapacity());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testWallSignal() {
        assertEquals("wallSignal() should return 0 on 0x0", 0, roombaSerial.wallSignal());
        // Max wall signal is 1023
        roombaSerial.currentSensorData[26] = (byte)0x03; // High byte
        roombaSerial.currentSensorData[27] = (byte)0xff; // Low byte
        assertEquals("wallSignal() should return 1023 on 0x03ff", 1023, roombaSerial.wallSignal());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffSignalLeft() {
        assertEquals("cliffSignalLeft() should return 0 on 0x0", 0, roombaSerial.cliffSignalLeft());
        // Max cliff signal is 4095
        roombaSerial.currentSensorData[28] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[29] = (byte)0xff; // Low byte
        assertEquals("cliffSignalLeft() should return 4095 on 0x0fff", 4095, roombaSerial.cliffSignalLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffSignalFrontLeft() {
        assertEquals("cliffSignalFrontLeft() should return 0 on 0x0", 0, roombaSerial.cliffSignalFrontLeft());
        // Max cliff signal is 4095
        roombaSerial.currentSensorData[30] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[31] = (byte)0xff; // Low byte
        assertEquals("cliffSignalFrontLeft() should return 4095 on 0x0fff", 4095, roombaSerial.cliffSignalFrontLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffSignalFrontRight() {
        assertEquals("cliffSignalFrontRight() should return 0 on 0x0", 0, roombaSerial.cliffSignalFrontRight());
        // Max cliff signal is 4095
        roombaSerial.currentSensorData[32] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[33] = (byte)0xff; // Low byte
        assertEquals("cliffSignalFrontRight() should return 4095 on 0x0fff", 4095,
                roombaSerial.cliffSignalFrontRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testCliffSignalRight() {
        assertEquals("cliffSignalRight() should return 0 on 0x0", 0, roombaSerial.cliffSignalRight());
        // Max cliff signal is 4095
        roombaSerial.currentSensorData[34] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[35] = (byte)0xff; // Low byte
        assertEquals("cliffSignalRight() should return 4095 on 0x0fff", 4095, roombaSerial.cliffSignalRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testInternalChargerAvailable() {
        assertFalse("internalChargerAvailable() should return false on 0 sensor bit",
                roombaSerial.internalChargerAvailable());
        roombaSerial.currentSensorData[39] = 0x1;
        assertTrue("internalChargerAvailable() should return true on 1 sensor bit",
                roombaSerial.internalChargerAvailable());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testHomebaseChargerAvailable() {
        assertFalse("homebaseChargerAvailable() should return false on 0 sensor bit",
                roombaSerial.homebaseChargerAvailable());
        roombaSerial.currentSensorData[39] = 0x2;
        assertTrue("homebaseChargerAvailable() should return true on 1 sensor bit",
                roombaSerial.homebaseChargerAvailable());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testMode() {
        assertEquals("mode() should return 0 0n 0x0", 0, roombaSerial.mode());
        roombaSerial.currentSensorData[40] = 0x5;
        assertEquals("mode() should return 5 0n 0x5", 5, roombaSerial.mode());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testSongNumber() {
        assertEquals("songNumber() should return 0 on 0x0", 0, roombaSerial.songNumber());
        roombaSerial.currentSensorData[41] = (byte)0x7; // 7
        assertEquals("songNumber() should return 128 on 0x80", 7, roombaSerial.songNumber());
        roombaSerial.currentSensorData[41] = (byte)0xf; // 15 (max song number)
        assertEquals("songNumber() should return 255 on 0xff", 15, roombaSerial.songNumber());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testSongPlaying() {
        assertFalse("songPlaying() should return false on 0 sensor bit", roombaSerial.songPlaying());
        roombaSerial.currentSensorData[42] = 0x1;
        assertTrue("songPlaying() should return true on 1 sensor bit", roombaSerial.songPlaying());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testRequestedVelocity() {
        assertEquals("requestedVelocity() should return 0 on 0x0", 0, roombaSerial.requestedVelocity());
        // Max positive velocity (500 mm/s)
        roombaSerial.currentSensorData[44] = (byte)0x01; // High byte
        roombaSerial.currentSensorData[45] = (byte)0xf4; // Low byte
        assertEquals("requestedVelocity() should return 500 on 0x01f4", 500, roombaSerial.requestedVelocity());
        // Max negative velocity (-550 mm/s)
        roombaSerial.currentSensorData[44] = (byte)0xfe;
        roombaSerial.currentSensorData[45] = (byte)0x0c;
        assertEquals("requestedVelocity() should return -32768 on 0x8000", -500, roombaSerial.requestedVelocity());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testRequestedRadius() {
        assertEquals("requestedRadius() should return 0 on 0x0", 0, roombaSerial.requestedRadius());
        roombaSerial.currentSensorData[46] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[47] = (byte)0xff; // Low byte
        assertEquals("requestedRadius() should return 32767 on 0x7fff", 32767, roombaSerial.requestedRadius());
        roombaSerial.currentSensorData[46] = (byte)0x80;
        roombaSerial.currentSensorData[47] = (byte)0x00;
        assertEquals("requestedRadius() should return -32768 on 0x8000", -32768, roombaSerial.requestedRadius());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testRequestedVelocityRight() {
        assertEquals("requestedVelocityRight() should return 0 on 0x0", 0, roombaSerial.requestedVelocityRight());
        // Max positive velocity (500 mm/s)
        roombaSerial.currentSensorData[48] = (byte)0x01; // High byte
        roombaSerial.currentSensorData[49] = (byte)0xf4; // Low byte
        assertEquals("requestedVelocityRight() should return 500 on 0x01f4", 500,
                roombaSerial.requestedVelocityRight());
        // Max negative velocity (-550 mm/s)
        roombaSerial.currentSensorData[48] = (byte)0xfe;
        roombaSerial.currentSensorData[49] = (byte)0x0c;
        assertEquals("requestedVelocityRight() should return -32768 on 0x8000", -500,
                roombaSerial.requestedVelocityRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testRequestedVelocityLeft() {
        assertEquals("requestedVelocityLeft() should return 0 on 0x0", 0, roombaSerial.requestedVelocityLeft());
        // Max positive velocity (500 mm/s)
        roombaSerial.currentSensorData[50] = (byte)0x01; // High byte
        roombaSerial.currentSensorData[51] = (byte)0xf4; // Low byte
        assertEquals("requestedVelocityLeft() should return 500 on 0x01f4", 500,
                roombaSerial.requestedVelocityLeft());
        // Max negative velocity (-550 mm/s)
        roombaSerial.currentSensorData[50] = (byte)0xfe;
        roombaSerial.currentSensorData[51] = (byte)0x0c;
        assertEquals("requestedVelocityLeft() should return -32768 on 0x8000", -500,
                roombaSerial.requestedVelocityLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testEncoderCountsLeft() {
        assertEquals("encoderCountsLeft() should return 0 on 0x0", 0, roombaSerial.encoderCountsLeft());
        roombaSerial.currentSensorData[52] = (byte)0x00; // High byte
        roombaSerial.currentSensorData[53] = (byte)0xff; // Low byte
        assertEquals("encoderCountsLeft() should return 255 on 0x00ff", 255, roombaSerial.encoderCountsLeft());
        roombaSerial.currentSensorData[52] = (byte)0xff; // High byte
        roombaSerial.currentSensorData[53] = (byte)0xff; // Low byte
        assertEquals("encoderCountsLeft() should return 65535 on 0xffff", 65535, roombaSerial.encoderCountsLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testEncoderCountsRight() {
        assertEquals("encoderCountsRight() should return 0 on 0x0", 0, roombaSerial.encoderCountsRight());
        roombaSerial.currentSensorData[54] = (byte)0x00; // High byte
        roombaSerial.currentSensorData[55] = (byte)0xff; // Low byte
        assertEquals("encoderCountsRight() should return 255 on 0x00ff", 255, roombaSerial.encoderCountsRight());
        roombaSerial.currentSensorData[54] = (byte)0xff; // High byte
        roombaSerial.currentSensorData[55] = (byte)0xff; // Low byte
        assertEquals("encoderCountsRight() should return 65535 on 0xffff", 65535, roombaSerial.encoderCountsRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperLeft() {
        assertFalse("lightBumperLeft() should return false on 0 sensor bit", roombaSerial.lightBumperLeft());
        roombaSerial.currentSensorData[56] = 0x1;
        assertTrue("lightBumperLeft() should return true on 1 sensor bit", roombaSerial.lightBumperLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperFrontLeft() {
        assertFalse("lightBumperFrontLeft() should return false on 0 sensor bit", roombaSerial.lightBumperFrontLeft());
        roombaSerial.currentSensorData[56] = 0x2;
        assertTrue("lightBumperFrontLeft() should return true on 1 sensor bit", roombaSerial.lightBumperFrontLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperCenterLeft() {
        assertFalse("lightBumperCenterLeft() should return false on 0 sensor bit",
                roombaSerial.lightBumperCenterLeft());
        roombaSerial.currentSensorData[56] = 0x4;
        assertTrue("lightBumperCenterLeft() should return true on 1 sensor bit", roombaSerial.lightBumperCenterLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperCenterRight() {
        assertFalse("lightBumperCenterRight() should return false on 0 sensor bit",
                roombaSerial.lightBumperCenterRight());
        roombaSerial.currentSensorData[56] = 0x8;
        assertTrue("lightBumperCenterRight() should return true on 1 sensor bit",
                roombaSerial.lightBumperCenterRight());
    }


    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperFrontRight() {
        assertFalse("lightBumperFrontRight() should return false on 0 sensor bit",
                roombaSerial.lightBumperFrontRight());
        roombaSerial.currentSensorData[56] = 0x10;
        assertTrue("lightBumperFrontRight() should return true on 1 sensor bit", roombaSerial.lightBumperFrontRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperRight() {
        assertFalse("lightBumperRight() should return false on 0 sensor bit", roombaSerial.lightBumperRight());
        roombaSerial.currentSensorData[56] = 0x20;
        assertTrue("lightBumperRight() should return true on 1 sensor bit", roombaSerial.lightBumperRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperSignalLeft() {
        assertEquals("lightBumperSignalLeft() should return 0 on 0x0", 0, roombaSerial.lightBumperSignalLeft());
        // Max light bumper signal is 4095
        roombaSerial.currentSensorData[57] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[58] = (byte)0xff; // Low byte
        assertEquals("lightBumperSignalLeft() should return 4095 on 0x0fff", 4095,
                roombaSerial.lightBumperSignalLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperSignalFrontLeft() {
        assertEquals("lightBumperSignalFrontLeft() should return 0 on 0x0", 0,
                roombaSerial.lightBumperSignalFrontLeft());
        // Max light bumper signal is 4095
        roombaSerial.currentSensorData[59] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[60] = (byte)0xff; // Low byte
        assertEquals("lightBumperSignalFrontLeft() should return 4095 on 0x0fff", 4095,
                roombaSerial.lightBumperSignalFrontLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperSignalCenterLeft() {
        assertEquals("lightBumperSignalCenterLeft() should return 0 on 0x0", 0,
                roombaSerial.lightBumperSignalCenterLeft());
        // Max light bumper signal is 4095
        roombaSerial.currentSensorData[61] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[62] = (byte)0xff; // Low byte
        assertEquals("lightBumperSignalCenterLeft() should return 4095 on 0x0fff", 4095,
                roombaSerial.lightBumperSignalCenterLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperSignalCenterRight() {
        assertEquals("lightBumperSignalCenterRight() should return 0 on 0x0", 0,
                roombaSerial.lightBumperSignalCenterRight());
        // Max light bumper signal is 4095
        roombaSerial.currentSensorData[63] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[64] = (byte)0xff; // Low byte
        assertEquals("lightBumperSignalCenterRight() should return 4095 on 0x0fff", 4095,
                roombaSerial.lightBumperSignalCenterRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperSignalFrontRight() {
        assertEquals("lightBumperSignalFrontRight() should return 0 on 0x0", 0,
                roombaSerial.lightBumperSignalFrontRight());
        // Max light bumper signal is 4095
        roombaSerial.currentSensorData[65] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[66] = (byte)0xff; // Low byte
        assertEquals("lightBumperSignalFrontRight() should return 4095 on 0x0fff", 4095,
                roombaSerial.lightBumperSignalFrontRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testLightBumperSignalRight() {
        assertEquals("lightBumperSignalRight() should return 0 on 0x0", 0, roombaSerial.lightBumperSignalRight());
        // Max light bumper signal is 4095
        roombaSerial.currentSensorData[67] = (byte)0x0f; // High byte
        roombaSerial.currentSensorData[68] = (byte)0xff; // Low byte
        assertEquals("lightBumperSignalRight() should return 4095 on 0x0fff", 4095,
                roombaSerial.lightBumperSignalRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testMotorCurrentLeft() {
        assertEquals("motorCurrentLeft() should return 0 on 0x0", 0, roombaSerial.motorCurrentLeft());
        roombaSerial.currentSensorData[71] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[72] = (byte)0xff; // Low byte
        assertEquals("motorCurrentLeft() should return 32767 on 0x7fff", 32767,
                roombaSerial.motorCurrentLeft());
        roombaSerial.currentSensorData[71] = (byte)0x80;
        roombaSerial.currentSensorData[72] = (byte)0x00;
        assertEquals("motorCurrentLeft() should return -32768 on 0x8000", -32768,
                roombaSerial.motorCurrentLeft());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testMotorCurrentRight() {
        assertEquals("motorCurrentRight() should return 0 on 0x0", 0, roombaSerial.motorCurrentRight());
        roombaSerial.currentSensorData[73] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[74] = (byte)0xff; // Low byte
        assertEquals("motorCurrentRight() should return 32767 on 0x7fff", 32767,
                roombaSerial.motorCurrentRight());
        roombaSerial.currentSensorData[73] = (byte)0x80;
        roombaSerial.currentSensorData[74] = (byte)0x00;
        assertEquals("motorCurrentRight() should return -32768 on 0x8000", -32768,
                roombaSerial.motorCurrentRight());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testMotorCurrentMainBrush() {
        assertEquals("motorCurrentMainBrush() should return 0 on 0x0", 0, roombaSerial.motorCurrentMainBrush());
        roombaSerial.currentSensorData[75] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[76] = (byte)0xff; // Low byte
        assertEquals("motorCurrentMainBrush() should return 32767 on 0x7fff", 32767,
                roombaSerial.motorCurrentMainBrush());
        roombaSerial.currentSensorData[75] = (byte)0x80;
        roombaSerial.currentSensorData[76] = (byte)0x00;
        assertEquals("motorCurrentMainBrush() should return -32768 on 0x8000", -32768,
                roombaSerial.motorCurrentMainBrush());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testMotorCurrentSideBrush() {
        assertEquals("motorCurrentSideBrush() should return 0 on 0x0", 0, roombaSerial.motorCurrentSideBrush());
        roombaSerial.currentSensorData[77] = (byte)0x7f; // High byte
        roombaSerial.currentSensorData[78] = (byte)0xff; // Low byte
        assertEquals("motorCurrentSideBrush() should return 32767 on 0x7fff", 32767,
                roombaSerial.motorCurrentSideBrush());
        roombaSerial.currentSensorData[77] = (byte)0x80;
        roombaSerial.currentSensorData[78] = (byte)0x00;
        assertEquals("motorCurrentSideBrush() should return -32768 on 0x8000", -32768,
                roombaSerial.motorCurrentSideBrush());
    }

    /**
     * Test for correct sensor byte read and method return
     */
    @Test
    public void testStasis() {
        assertFalse("stasis() should return false on 0 sensor bit", roombaSerial.stasis());
        roombaSerial.currentSensorData[79] = 0x1;
        assertTrue("stasis() should return true on 1 sensor bit", roombaSerial.stasis());
    }

    //endregion
}
