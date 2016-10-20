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
}
