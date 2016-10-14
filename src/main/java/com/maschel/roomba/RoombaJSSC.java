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
     */
    public void startup() {
        log.info("Sending 'startup' and 'safe' command to roomba.");
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
     */
    public void hardReset() {
        log.info("Sending 'hardreset' command to roomba.");
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
     */
    public void schedule(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat,
                         int sun_hour, int sun_min, int mon_hour, int mon_min, int tue_hour, int tue_min,
                         int wed_hour, int wed_min, int thu_hour, int thu_min, int fri_hour, int fri_min,
                         int sat_hour, int sat_min) {

        log.info("Sending new schedule to roomba.");

        // Create Days byte
        byte days = SCHEDULE_CLEAR_MASK;
        if(sun) days |= SCHEDULE_SUNDAY_MASK;
        if(mon) days |= SCHEDULE_MONDAY_MASK;
        if(tue) days |= SCHEDULE_TUESDAY_MASK;
        if(wed) days |= SCHEDULE_WEDNESDAY_MASK;
        if(thu) days |= SCHEDULE_THURSDAY_MASK;
        if(fri) days |= SCHEDULE_FRIDAY_MASK;
        if(sat) days |= SCHEUDLE_SATURDAY_MASK;

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
     */
    public void setDayTime(int day, int hour, int minute) {
        log.info("Setting time of roomba to: day='" + day + "', time='" + hour + ":" + minute + "'.");
        send(new byte[] { (byte)OPC_SET_DAYTIME, (byte)day, (byte)hour, (byte)minute });
    }

    //endregion

    //region Roomba Actuator commands



    //endregion

    // roomba Open interface commands Opcodes
    static final int OPC_RESET              =   7;
    static final int OPC_START              = 128;
    static final int OPC_BAUD               = 129;
    static final int OPC_CONTROL            = 130;
    static final int OPC_SAFE               = 131;
    static final int OPC_FULL               = 132;
    static final int OPC_POWER              = 133;
    static final int OPC_SPOT               = 134;
    static final int OPC_CLEAN              = 135;
    static final int OPC_MAX_CLEAN          = 136;
    static final int OPC_DRIVE              = 137;
    static final int OPC_MOTORS             = 138;
    static final int OPC_LEDS               = 139;
    static final int OPC_SONG               = 140;
    static final int OPC_PLAY               = 141;
    static final int OPC_QUERY              = 142;
    static final int OPC_FORCE_SEEKING_DOCK = 143;
    static final int OPC_PWM_MOTORS         = 144;
    static final int OPC_DRIVE_WHEELS       = 145;
    static final int OPC_DRIVE_PWM          = 146;
    static final int OPC_STREAM             = 148;
    static final int OPC_QUERY_LIST         = 149;
    static final int OPC_DO_STREAM          = 150;
    static final int OPC_SCHEDULING_LEDS    = 162;
    static final int OPC_SCHEDULE           = 167;
    static final int OPC_SET_DAYTIME        = 168;
    static final int OPC_STOP               = 173;

    // Scheduling bitmasks
    static final int SCHEDULE_CLEAR_MASK    = 0x0;
    static final int SCHEDULE_SUNDAY_MASK   = 0x1;
    static final int SCHEDULE_MONDAY_MASK   = 0x2;
    static final int SCHEDULE_TUESDAY_MASK  = 0x4;
    static final int SCHEDULE_WEDNESDAY_MASK= 0x8;
    static final int SCHEDULE_THURSDAY_MASK = 0x10;
    static final int SCHEDULE_FRIDAY_MASK   = 0x20;
    static final int SCHEUDLE_SATURDAY_MASK = 0x40;

}
