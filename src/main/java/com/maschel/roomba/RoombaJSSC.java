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

import com.maschel.roomba.song.RoombaSongNote;
import org.apache.log4j.Logger;

public abstract class RoombaJSSC {

    final static Logger log = Logger.getLogger(RoombaJSSC.class);

    boolean connected = false;

    public RoombaJSSC() {}

    public abstract String[] portList();

    public abstract boolean connect(String portId);

    public abstract void disconnect();

    public abstract boolean send(byte[] bytes);

    public abstract boolean send(int b);


    //region Roomba basic power commands

    /**
     * This command starts the OI. You must always send the Start command
     * before sending any other commands to the OI.
     */
    public void start() {
        log.info("Sending 'start' command to roomba.");
        send(OPC_START);
    }

    /**
     * This command starts the OI of the roomba and puts it in Safe mode
     * which enables user control. Safe mode turns off all LEDs.
     * This will run the 'start' and 'safe' commands.
     * <p>Note: Wait at least 500ms before sending any commands.</p>
     */
    public void startup() {
        log.info("Sending 'startup' and 'safeMode' command to roomba.");
        byte cmd[] = { (byte)OPC_START, (byte)OPC_SAFE };
        send(cmd);
    }

    /**
     * This command stops the OI. All streams will stop and the robot will no longer
     * respond to commands. Use this command when you are finished working with the robot.
     */
    public void stop() {
        log.info("Sending 'stop' command to roomba.");
        send(OPC_STOP);
    }

    /**
     * This command powers down Roomba. The OI can be in Passive,
     * Safe, or Full mode to accept this command.
     */
    public void powerOff() {
        log.info("Sending 'powerOff' command to roomba.");
        send(OPC_POWER);
    }

    /**
     * Reset the Roomba after error, this will also run the 'start' and 'safe' commands.
     */
    public void reset() {
        log.info("Sending 'reset' command to roomba.");
        startup();
    }

    /**
     * This command resets the Roomba, as if you had removed and reinserted the battery.
     * <p>Note: Wait at least 5000ms before sending any commands.</p>
     */
    public void hardReset() {
        log.info("Sending 'hardReset' command to roomba.");
        send(OPC_RESET);
    }

    //endregion

    //region Roomba mode commands

    /**
     * This command puts the OI into Safe mode, enabling user control of Roomba.
     * It turns off all LEDs. The OI can be in Passive, Safe, or Full mode to accept this command.
     * If a safety condition occurs Roomba reverts automatically to Passive mode.
     */
    public void safeMode() {
        log.info("Sending 'safe' command to roomba.");
        send(OPC_SAFE);
    }

    /**
     * This command gives you complete control over Roomba by putting the OI into Full mode,
     * and turning off the cliff, wheel-drop and internal charger safety features. That is, in Full mode,
     * Roomba executes any command that you send it, even if the internal charger is plugged in, or command
     * triggers a cliff or wheel drop condition.
     */
    public void fullMode() {
        log.info("Sending 'full' command to roomba.");
        send(OPC_FULL);
    }

    //endregion

    //region Roomba cleaning commands

    /**
     * This command starts the default cleaning mode. This is the same as pressing Roomba’s Clean button,
     * and will pause a cleaning cycle if one is already in progress.
     */
    public void clean() {
        log.info("Sending 'clean' command to roomba.");
        send(OPC_CLEAN);
    }

    /**
     * This command starts the Max cleaning mode, which will clean until the battery is dead.
     * This command will pause a cleaning cycle if one is already in progress.
     */
    public void cleanMax() {
        log.info("Sending 'cleanMax' command to roomba.");
        send(OPC_MAX_CLEAN);
    }

    /**
     * This command starts the Spot cleaning mode. This is the same as pressing Roomba’s Spot button,
     * and will pause a cleaning cycle if one is already in progress.
     */
    public void cleanSpot() {
        log.info("Sending 'cleanSpot' command to roomba.");
        send(OPC_SPOT);
    }

    /**
     * This command directs Roomba to drive onto the dock the next time it encounters the docking beams.
     * This is the same as pressing Roomba’s Dock button, and will pause a cleaning cycle if one is already
     * in progress.
     */
    public void seekDock() {
        log.info("Sending 'seekDock' command to roomba.");
        send(OPC_FORCE_SEEKING_DOCK);
    }

    /**
     * This command sends Roomba a new schedule. To disable scheduled cleaning, send all false and 0s.
     * @param sun Enable Sunday scheduling
     * @param mon Enable Monday scheduling
     * @param tue Enable Tuesday scheduling
     * @param wed Enable Wednesday scheduling
     * @param thu Enable Thursday scheduling
     * @param fri Enable Friday scheduling
     * @param sat Enable Saturday scheduling
     * @param sun_hour Sunday scheduled hour
     * @param sun_min Sunday scheduled minute
     * @param mon_hour Monday scheduled hour
     * @param mon_min Monday scheduled minute
     * @param tue_hour Tuesday scheduled hour
     * @param tue_min Tuesday scheduled minute
     * @param wed_hour Wednesday scheduled hour
     * @param wed_min Wednesday scheduled minute
     * @param thu_hour Thursday scheduled hour
     * @param thu_min Thursday scheduled minute
     * @param fri_hour Friday scheduled hour
     * @param fri_min Friday scheduled minute
     * @param sat_hour Saturday scheduled hour
     * @param sat_min Saturday scheduled minute
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void schedule(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat,
                         int sun_hour, int sun_min, int mon_hour, int mon_min, int tue_hour, int tue_min,
                         int wed_hour, int wed_min, int thu_hour, int thu_min, int fri_hour, int fri_min,
                         int sat_hour, int sat_min) throws IllegalArgumentException {

        // Validate argument values
        if ((sun_hour < 0 || sun_hour > 23) || (mon_hour < 0 || mon_hour > 23) || (tue_hour < 0 || tue_hour > 23)
                || (wed_hour < 0 || wed_hour > 23) || (thu_hour < 0 || thu_hour > 23)  || (fri_hour < 0 || fri_hour > 23)
                || (sat_hour < 0 || sat_hour > 23)) {
            throw new IllegalArgumentException("Scheduled hours should be between 0 and 23");
        }
        if ((sun_min < 0 || sun_min > 59) || (mon_min < 0 || mon_min > 59) || (tue_min < 0 || tue_min > 59)
                || (wed_min < 0 || wed_min > 59) || (thu_min < 0 || thu_min > 59) || (fri_min < 0 || fri_min > 59)
                || (sat_min < 0 || sat_min > 59)) {
            throw new IllegalArgumentException("Scheduled minutes should be between 0 and 59");
        }

        log.info("Sending new schedule to roomba.");

        // Create Days byte
        byte days = (byte)((sun?SCHEDULE_SUNDAY_MASK:0) | (mon?SCHEDULE_MONDAY_MASK:0) |
                            (tue?SCHEDULE_TUESDAY_MASK:0) | (wed?SCHEDULE_WEDNESDAY_MASK:0) |
                            (thu?SCHEDULE_THURSDAY_MASK:0) |(fri?SCHEDULE_FRIDAY_MASK:0) |
                            (sat?SCHEDULE_SATURDAY_MASK:0));

        send(new byte[] {
                (byte)OPC_SCHEDULE, days,
                (byte)sun_hour, (byte)sun_min,
                (byte)mon_hour, (byte)mon_min,
                (byte)tue_hour, (byte)tue_min,
                (byte)wed_hour, (byte)wed_min,
                (byte)thu_hour, (byte)thu_min,
                (byte)fri_hour, (byte)fri_min,
                (byte)sat_hour, (byte)sat_min
        });
    }

    /**
     * This command sets Roomba’s clock.
     * @param day Day number: 0 = Sunday, 1 = Monday ... 6 = Saturday
     * @param hour Hour in 24hour format 0-23
     * @param minute Minute 0-59
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void setDayTime(int day, int hour, int minute) throws IllegalArgumentException {

        // Validate argument values
        if (day < 0 || day > 6)
            throw new IllegalArgumentException("Day should be between 0 (sun) and 6 (sat)");
        if (hour < 0 || hour > 23)
            throw new IllegalArgumentException("Hour should be between 0 and 23");
        if (minute < 0 || minute > 59)
            throw new IllegalArgumentException("Minute should be between 0 and 59");

        log.info("Setting time of roomba to: day='" + day + "', time='" + hour + ":" + minute + "'.");
        send(new byte[] { (byte)OPC_SET_DAYTIME, (byte)day, (byte)hour, (byte)minute });
    }

    //endregion

    //region Roomba Actuator commands

    /**
     * This command controls Roomba’s drive wheels.
     * @param velocity The average velocity of the drive wheels in millimeters per second (mm/s)
     *                 A positive velocity makes the roomba drive forward, a negative velocity
     *                 makes it drive backwards.
     *                 min: -500mm/s max: 500mm/s
     * @param radius The radius in millimeters at which Roomba will turn
     *               The longer radii make Roomba drive straighter, while the shorter radii make Roomba turn more.
     *               The radius is measured from the center of the turning circle to the center of Roomba.
     *               <p>min: -2000mm max: 2000mm</p>
     *               Special cases:
     *               <ul>
     *                  <li>Straight = 32767 or 32768</li>
     *                  <li>Turn in place clockwise = -1</li>
     *                  <li>Turn in place counter-clockwise = 1</li>
     *               </ul>
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void drive(int velocity, int radius) throws IllegalArgumentException {

        // Validate argument values
        if (velocity < -500 || velocity > 500)
            throw new IllegalArgumentException("Velocity should be between -500 and 500");
        if ((radius < -2000 || radius > 2000) && (radius != 32768 && radius != 32767))
            throw new IllegalArgumentException("Radius should be between -2000 and 2000 or 32767-32768");

        log.info("Sending 'drive' command (velocity:" + velocity + ", radius:" + radius + ") to roomba.");
        byte[] cmd = { (byte)OPC_DRIVE, (byte)(velocity >>> 8), (byte)velocity,
                        (byte)(radius >>> 8), (byte)radius
        };
        send(cmd);
    }

    /**
     * This command lets you control the forward and backward motion of Roomba’s drive wheels independently.
     * A positive velocity makes that wheel drive forward, while a negative velocity makes it drive backward.
     * @param rightVelocity Right wheel velocity min: -500 mm/s, max: 500 mm/s
     * @param leftVelocity Left wheel velocity min: -500 mm/s, max: 500 mm/s
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void driveDirect(int rightVelocity, int leftVelocity) throws IllegalArgumentException {

        // Validate argument values
        if (rightVelocity < -500 || rightVelocity > 500 || leftVelocity < -500 || leftVelocity > 500)
            throw new IllegalArgumentException("Velocity should be between -500 and 500");

        log.info("Sending 'driveDirect' command (velocity right: " + rightVelocity + ", " +
                "velocity left: " + leftVelocity + ") to roomba.");
        byte[] cmd = { (byte)OPC_DRIVE_WHEELS, (byte)(rightVelocity >>> 8), (byte)rightVelocity,
                        (byte)(leftVelocity >>> 8), (byte)leftVelocity
        };
        send(cmd);
    }

    /**
     * This command lets you control the raw forward and backward motion of Roomba’s drive wheels independently.
     * A positive PWM makes that wheel drive forward, while a negative PWM makes it drive backward.
     * The PWM values are percentages: 100% is full power forward, -100% is full power reverse.
     * @param rightPWM Right wheel PWM (min: -100%, max: 100%)
     * @param leftPWM Left wheel PWM (min: -100%, max: 100%)
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void drivePWM(int rightPWM, int leftPWM) throws IllegalArgumentException {

        // Validate argument values
        if (rightPWM < -100 || rightPWM > 100 || leftPWM < -100 || leftPWM > 100)
            throw new IllegalArgumentException("PWM should be between -100% and 100%");

        log.info("Sending 'drivePWM' command (right PWM: " + rightPWM + "%, left PWM: " + leftPWM + "%) to roomba.");
        int relRightPWM = (DRIVE_WHEEL_MAX_POWER * rightPWM/100);
        int relLeftPWM = (DRIVE_WHEEL_MAX_POWER * leftPWM/100);
        byte[] cmd = { (byte)OPC_DRIVE_PWM, (byte)(relRightPWM >>> 8), (byte)relRightPWM,
                        (byte)(relLeftPWM >>> 8), (byte)relLeftPWM
        };
        send(cmd);
    }

    /**
     * This command lets you control the forward and backward motion of Roomba’s main brush, side brush,
     * and vacuum independently. Motor velocity cannot be controlled with this command, all motors will run at
     * maximum speed when enabled. The main brush and side brush can be run in either direction.
     * The vacuum only runs forward.
     * @param sideBrush Turns on side brush
     * @param vacuum Turns on vacuum
     * @param mainBrush Turns on main brush
     * @param sideBrushClockwise if true the side brush will turn clockwise (default: counterclockwise)
     * @param mainBrushOutward if true the side brush will turn outward (default: inward)
     */
    public void motors(boolean sideBrush, boolean vacuum, boolean mainBrush,
                       boolean sideBrushClockwise, boolean mainBrushOutward) {
        log.info("Sending 'motors' command (sideBrush: " + sideBrush + "(clockwise: " + sideBrushClockwise + "), " +
                "vacuum: " + vacuum + ", mainBrush: " + mainBrush + "(outward: " + mainBrushOutward + ")) to roomba.");

        // Create motor byte
        byte motors = (byte)((sideBrush?MOTORS_SIDE_BRUSH_MASK:0) | (vacuum?MOTORS_VACUUM_MASK:0) |
                            (mainBrush?MOTORS_MAIN_BRUSH_MASK:0) | (sideBrushClockwise?MOTORS_SIDE_BRUSH_CW_MASK:0) |
                            (mainBrushOutward?MOTORS_MAIN_BRUSH_OW_MASK:0));
        byte[] cmd = { (byte)OPC_MOTORS, motors };
        send(cmd);
    }

    /**
     * This command lets you control the speed of Roomba’s main brush, side brush, and vacuum independently.
     * The main brush and side brush can be run in either direction. The vacuum only runs forward.
     * Positive speeds turn the motor in its default (cleaning) direction. Default direction for the side brush is
     * counterclockwise. Default direction for the main brush/flapper is inward.
     * The PWM values are percentages: 100% is full power forward, -100% is full power reverse.
     * @param mainBrushPWM Main brush PWM (min: -100%, max: 100%)
     * @param sideBrushPWM Side brush PWM (min: -100%, max: 100%)
     * @param vacuumPWM Vacuum PWM (min: 0%, max: 100%)
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void motorsPWM(int mainBrushPWM, int sideBrushPWM, int vacuumPWM) throws IllegalArgumentException {

        // Validate argument values
        if (mainBrushPWM < -100 || mainBrushPWM > 100 || sideBrushPWM < -100 || sideBrushPWM > 100)
            throw new IllegalArgumentException("Main- and side- brush PWM should be between -100% and 100%");
        if (vacuumPWM < 0 || vacuumPWM > 100)
            throw new IllegalArgumentException("Vacuum PWM should be between 0% and 100%");

        log.info("Sending 'motorsPWM' command (mainBrushPWM: " + mainBrushPWM + "%, sideBrushPWM: " + sideBrushPWM +
                "%, vacuumPWM: " + vacuumPWM + "%) to roomba.");
        int relMainBrushPWM = (MOTORS_MAX_POWER * mainBrushPWM/100);
        int relSideBrushPWM = (MOTORS_MAX_POWER * sideBrushPWM/100);
        int relVacuumPWM    = (MOTORS_MAX_POWER * vacuumPWM/100);
        byte[] cmd = { (byte)OPC_PWM_MOTORS, (byte)relMainBrushPWM, (byte)relSideBrushPWM, (byte)relVacuumPWM };
        send(cmd);
    }

    /**
     * This command controls the LEDs common to all models of Roomba 600.
     * @param debris Turns on the debris LED
     * @param spot Turns on the spot LED
     * @param dock Turns on the dock LED
     * @param check_robot Turns on the check robot LED
     * @param powerColor Controls the power LED red color relative to green: 0% = green, 100% = red.
     *                   Intermediate values are intermediate colors (orange, yellow, etc).
     * @param powerIntensity Controls the intensity of the power led. 0% = off, 100% = full intensity.
     *                       Intermediate values are intermediate intensities.
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void leds(boolean debris, boolean spot, boolean dock, boolean check_robot, int powerColor,
                     int powerIntensity) throws IllegalArgumentException {

        // Validate argument values
        if (powerColor < 0 || powerColor > 100 || powerIntensity < 0 || powerIntensity > 100)
            throw new IllegalArgumentException("Color and/or Intensity should be between 0% and 100%");

        log.info("Sending 'LEDs' command (debris: " + debris + ", spot: " + spot + ", dock: " + dock +
                ", checkRobot: " + check_robot + ", powerRedColor: " + powerColor + ", powerIntensity: "
                + powerIntensity + ") to roomba.");
        // Create LEDs byte
        byte LEDs = (byte)((debris?LEDS_DEBRIS_MASK:0) | (spot?LEDS_SPOT_MASK:0) | (dock?LEDS_DOCK_MASK:0) |
                            (check_robot?LEDS_CHECK_ROBOT_MASK:0));
        int relPowerRedColor = (LEDS_POWER_RED_COLOR * powerColor/100);
        int relPowerIntensity = (LEDS_POWER_MAX_INTENSITY * powerIntensity/100);
        byte[] cmd = { (byte)OPC_LEDS, LEDs, (byte)relPowerRedColor, (byte)relPowerIntensity };
        send(cmd);
    }

    /**
     * This command controls the state of the scheduling LEDs present on the Roomba 560 and 570.
     * @param sun Turn on scheduling Sunday LED
     * @param mon Turn on scheduling Monday LED
     * @param tue Turn on scheduling Tuesday LED
     * @param wed Turn on scheduling Wednesday LED
     * @param thu Turn on scheduling Thursday LED
     * @param fri Turn on scheduling Friday LED
     * @param sat Turn on scheduling Saturday LED
     * @param colon Turn on scheduling Colon LED
     * @param pm Turn on scheduling PM LED
     * @param am Turn on scheduling AM LED
     * @param clock Turn on scheduling clock LED
     * @param schedule Turn on scheduling schedule LED
     */
    public void schedulingLeds(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri,
                               boolean sat, boolean colon, boolean pm, boolean am, boolean clock, boolean schedule) {
        log.info("Sending 'schedulingLEDs' command (sun:" + sun + ", mon:" + mon + ", tue:" + tue + ", wed:" +
                wed + ", thu:" + thu + ", fri:" + fri + ", sat:" + sat + ", colon:" + colon + ", pm:" +
                pm + ", am:" + am + ", clock:" + clock + ", schedule:" + schedule + ") to roomba.");
        // Create weekday LEDs byte
        byte weekdayLEDs = (byte)((sun?SCHEDULE_SUNDAY_MASK:0) | (mon?SCHEDULE_MONDAY_MASK:0) |
                                (tue?SCHEDULE_TUESDAY_MASK:0) | (wed?SCHEDULE_WEDNESDAY_MASK:0) |
                                (thu?SCHEDULE_THURSDAY_MASK:0) | (fri?SCHEDULE_FRIDAY_MASK:0) |
                                (sat?SCHEDULE_SATURDAY_MASK:0));
        // Create Scheduling LEDs byte
        byte schedulingLEDs = (byte)((colon?LEDS_SCHEDULE_COLON_MASK:0) | (pm?LEDS_SCHEDULE_PM_MASK:0) |
                                    (am?LEDS_SCHEDULE_AM_MASK:0) | (clock?LEDS_SCHEDULE_CLOCK_MASK:0) |
                                    (schedule?LEDS_SCHEDULE_SCHEDULE_MASK:0));
        byte[] cmd = { (byte)OPC_SCHEDULING_LEDS, weekdayLEDs, schedulingLEDs };
        send(cmd);
    }

    /**
     * This command controls the four 7 segment displays on the Roomba 560 and 570 using ASCII character codes.
     * Because a 7 segment display is not sufficient to display alphabetic characters properly, all characters are
     * an approximation, and not all ASCII codes are implemented.
     * Note: Use a space for an empty character
     * @param char0 First character
     * @param char1 Second character
     * @param char2 Third character
     * @param char3 Fourth character
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void digitLedsAscii(char char0, char char1, char char2, char char3) throws IllegalArgumentException {

        // Validate argument values
        if (!isAllowedASCIIChar(char0))
            throw new IllegalArgumentException("Character '" + char0 + "' is not allowed");
        if (!isAllowedASCIIChar(char1))
            throw new IllegalArgumentException("Character '" + char1 + "' is not allowed");
        if (!isAllowedASCIIChar(char2))
            throw new IllegalArgumentException("Character '" + char2 + "' is not allowed");
        if (!isAllowedASCIIChar(char3))
            throw new IllegalArgumentException("Character '" + char3 + "' is not allowed");

        log.info("Sending 'digitLedsAscii' command with chars: " + char0 + ", " + char1 + ", " + char2 + ", "
                + char3 + " to roomba.");
        byte[] cmd = { (byte)OPC_DIGIT_LEDS_ASCII, (byte)char0, (byte)char1, (byte)char2, (byte)char3 };
        send(cmd);
    }

    /**
     * This command lets you push Roomba’s buttons. The buttons will automatically release after 1/6th of a second.
     * @param clean Presses the clean button
     * @param spot Presses the spot button
     * @param dock Presses the dock button
     * @param minute Presses the minute button
     * @param hour Presses the hour button
     * @param day Presses the day button
     * @param schedule Presses the schedule button
     * @param clock Presses the clock button
     */
    public void buttons(boolean clean, boolean spot, boolean dock, boolean minute, boolean hour,
                        boolean day, boolean schedule, boolean clock) {
        log.info("Sending 'buttons' command with pushed clean:" + clean + ", spot:" + spot + ", dock:" + dock +
                ", minute:" + minute + ", hour:" + hour + ", day:" + day + ", schedule:" + schedule +
                ", clock:" + clock + " to roomba.");
        // Create buttons byte
        byte buttons = (byte)((clean?BUTTONS_CLEAN_MASK:0) | (spot?BUTTONS_SPOT_MASK:0) | (dock?BUTTONS_DOCK_MASK:0) |
                            (minute?BUTTONS_MINUTE_MASK:0) | (hour?BUTTONS_HOUR_MASK:0) | (day?BUTTONS_DAY_MASK:0) |
                            (schedule?BUTTONS_SCHEDULE_MASK:0) | (clock?BUTTONS_CLOCK_MASK:0));
        byte[] cmd = { (byte)OPC_BUTTONS, buttons };
        send(cmd);
    }

    /**
     * This command lets you specify up to four songs to the OI that you can play at a later time.
     * Each song is associated with a song number. The Play command uses the song number to identify your
     * song selection. Each song can contain up to sixteen notes.
     * @param songNumber Song number (0-4)
     * @param notes Array of RoombaSongNote (max. 16)
     * @param tempo Tempo in BPM (min. 60 BPM, max. 800 BPM)
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void song(int songNumber, RoombaSongNote[] notes, int tempo) throws IllegalArgumentException {

        // Validate argument values
        if (songNumber < 0 || songNumber > 4)
            throw new IllegalArgumentException("Song number should be between 0 and 4");
        if (notes.length > 16)
            throw new IllegalArgumentException("Songs have a maximum of 16 notes");
        if (tempo < 60 || tempo > 800)
            throw new IllegalArgumentException("Song Tempo should be between 60 and 800 BPM");

        log.info("Sending 'song' command, saving song number: " + songNumber + " to roomba.");
        final int notes_offset = 3;
        byte[] cmd = new byte[notes.length*2 + notes_offset];
        cmd[0] = (byte)OPC_SONG;
        cmd[1] = (byte)songNumber;
        cmd[2] = (byte)notes.length;
        System.arraycopy(RoombaSongNote.songNotesToBytes(notes, tempo), 0, cmd, notes_offset, notes.length*2);
        send(cmd);
    }

    /**
     * This command lets you select a song to play from the songs added to Roomba using the Song command.
     * You must add one or more songs to Roomba using the Song command in order for the Play command to work.
     * @param songNumber Song number (0-4)
     * @throws IllegalArgumentException One of the arguments is out of bounds.
     */
    public void play(int songNumber) throws IllegalArgumentException {

        // Validate argument values
        if (songNumber < 0 || songNumber > 4)
            throw new IllegalArgumentException("Song number should be between 0 and 4");

        log.info("Sending 'play' command, song number: " + songNumber + " to roomba.");
        byte[] cmd = { (byte)OPC_PLAY, (byte)songNumber };
        send(cmd);
    }

    //endregion

    //region Class helpers

    /**
     * General sleep function that gives commands that use this function some time to instantiate.
     * @param millis Time in milliseconds that the current Thread should sleep.
     */
    public void sleep(int millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ex) {}
    }

    /**
     * Checks if the given char is a charter that can be shown on the roomba.
     * @param c The char to check.
     * @return True if the character is printable by the roomba.
     */
    private boolean isAllowedASCIIChar(char c) {
        return c >= 32 && c <= 126  // Printable ASCII characters
                && c != 42          // Not *
                && c != 43          // Not +
                && c != 64;         // Not @
    }

    //endregion

    //region static class variables

    // roomba Open interface commands Opcodes
    private static final int OPC_RESET              =   7;
    private static final int OPC_START              = 128;
    private static final int OPC_SAFE               = 131;
    private static final int OPC_FULL               = 132;
    private static final int OPC_POWER              = 133;
    private static final int OPC_SPOT               = 134;
    private static final int OPC_CLEAN              = 135;
    private static final int OPC_MAX_CLEAN          = 136;
    private static final int OPC_DRIVE              = 137;
    private static final int OPC_MOTORS             = 138;
    private static final int OPC_LEDS               = 139;
    private static final int OPC_SONG               = 140;
    private static final int OPC_PLAY               = 141;
    private static final int OPC_QUERY              = 142;
    private static final int OPC_FORCE_SEEKING_DOCK = 143;
    private static final int OPC_PWM_MOTORS         = 144;
    private static final int OPC_DRIVE_WHEELS       = 145;
    private static final int OPC_DRIVE_PWM          = 146;
    private static final int OPC_STREAM             = 148;
    private static final int OPC_QUERY_LIST         = 149;
    private static final int OPC_DO_STREAM          = 150;
    private static final int OPC_SCHEDULING_LEDS    = 162;
    private static final int OPC_DIGIT_LEDS_ASCII   = 164;
    private static final int OPC_BUTTONS            = 165;
    private static final int OPC_SCHEDULE           = 167;
    private static final int OPC_SET_DAYTIME        = 168;
    private static final int OPC_STOP               = 173;

    // Scheduling bitmask
    private static final int SCHEDULE_SUNDAY_MASK   = 0x1;
    private static final int SCHEDULE_MONDAY_MASK   = 0x2;
    private static final int SCHEDULE_TUESDAY_MASK  = 0x4;
    private static final int SCHEDULE_WEDNESDAY_MASK= 0x8;
    private static final int SCHEDULE_THURSDAY_MASK = 0x10;
    private static final int SCHEDULE_FRIDAY_MASK   = 0x20;
    private static final int SCHEDULE_SATURDAY_MASK = 0x40;

    // Motors bitmask
    private static final int MOTORS_SIDE_BRUSH_MASK     = 0x1;
    private static final int MOTORS_VACUUM_MASK         = 0x2;
    private static final int MOTORS_MAIN_BRUSH_MASK     = 0x4;
    private static final int MOTORS_SIDE_BRUSH_CW_MASK  = 0x8;
    private static final int MOTORS_MAIN_BRUSH_OW_MASK  = 0x10;

    // LEDs bitmask
    private static final int LEDS_DEBRIS_MASK       = 0x1;
    private static final int LEDS_SPOT_MASK         = 0x2;
    private static final int LEDS_DOCK_MASK         = 0x4;
    private static final int LEDS_CHECK_ROBOT_MASK  = 0x8;

    // Schedule LEDs bitmask
    private static final int LEDS_SCHEDULE_COLON_MASK       = 0x1;
    private static final int LEDS_SCHEDULE_PM_MASK          = 0x2;
    private static final int LEDS_SCHEDULE_AM_MASK          = 0x4;
    private static final int LEDS_SCHEDULE_CLOCK_MASK       = 0x8;
    private static final int LEDS_SCHEDULE_SCHEDULE_MASK    = 0x10;

    // Buttons bitmask
    private static final int BUTTONS_CLEAN_MASK     = 0x1;
    private static final int BUTTONS_SPOT_MASK      = 0x2;
    private static final int BUTTONS_DOCK_MASK      = 0x4;
    private static final int BUTTONS_MINUTE_MASK    = 0x8;
    private static final int BUTTONS_HOUR_MASK      = 0x10;
    private static final int BUTTONS_DAY_MASK       = 0x20;
    private static final int BUTTONS_SCHEDULE_MASK  = 0x40;
    private static final int BUTTONS_CLOCK_MASK     = 0x80;

    // Drive constants
    private static final int DRIVE_WHEEL_MAX_POWER  = 0xFF;

    // Motors constants
    private static final int MOTORS_MAX_POWER       = 0x7F;

    // LEDS constants
    private static final int LEDS_POWER_MAX_INTENSITY   = 0xFF;
    private static final int LEDS_POWER_RED_COLOR       = 0xFF;

    //endregion
}
