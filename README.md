# RoombaJSSC

[![Build Status](https://travis-ci.org/maschel/RoombaJSSC.svg?branch=master)](https://travis-ci.org/maschel/RoombaJSSC) [![Coverage Status](https://coveralls.io/repos/github/maschel/RoombaJSSC/badge.svg?branch=master)](https://coveralls.io/github/maschel/RoombaJSSC?branch=master) [![Javadocs](http://www.javadoc.io/badge/com.maschel/roombajssc.svg)](http://www.javadoc.io/doc/com.maschel/roombajssc) [![Maven Central](https://img.shields.io/maven-central/v/com.maschel/roombajssc.svg)](https://maven-badges.herokuapp.com/maven-central/com.maschel/roombajssc) [![GitHub release](https://img.shields.io/github/release/maschel/roombajssc.svg)](https://github.com/maschel/RoombaJSSC/releases) [![license](https://img.shields.io/github/license/mashape/apistatus.svg)](https://github.com/maschel/RoombaJSSC/blob/master/LICENSE)


RoombaJSSC is a JAVA library to control and/or read sensor values of a Roomba (500 & 600 series).
This library implements (basically) all the commands and sensors specified in the [iRobot Create 2 Open Interace Specification](http://www.irobot.com/~/media/MainSite/PDFs/About/STEM/Create/create_2_Open_Interface_Spec.pdf) based on Roomba 600.

## Why this library?
* Fully implemented iRobot specifications (for roomba 500/600 series)
* Uses the JSSC library for serial communication, which contains native libraries for many platforms:
    * Windows (x86, x86-64)
    * Linux(x86, x86-64, ARM soft & hard float)
    * Solaris(x86, x86-64)
    * Mac OS X(x86, x86-64, PPC, PPC64)
* [JavaDoc](http://www.javadoc.io/doc/com.maschel/roombajssc) on all methods describing commands & values as given in iRobot specifications.

## How to install

### Maven (recommended)
```xml
<dependency>
    <groupId>com.maschel</groupId>
    <artifactId>roombajssc</artifactId>
    <version>1.1.0</version>
</dependency>
```

### Import jar in project
You can download the (latest) releases here: [Github releases](https://github.com/maschel/RoombaJSSC/releases)


## Basic usage
### Standard library lifecycle
```java
RoombaJSSC roomba = new RoombaJSSCSerial();

// Get available serial port(s) (not mandatory)
String[] ports = roomba.portList();

// Connect
roomba.connect("/dev/a/serial/port"); // Use portList() to get available ports.

// Make roomba ready for communication & control (safe mode)
roomba.startup();

// Send commands
roomba.clean(); // Roomba will start cleaning
roomba.digitLedsAscii('H', 'E', 'Y', '!'); // Shows message on digit leds

// Read sensors (until key is pressed)
while (System.in.available() == 0) {
    roomba.updateSensors(); // Read sensor values from roomba
    roomba.sleep(50); // Sleep for AT LEAST 50 ms
    
    // Read sensors
    if (roomba.wall())
        System.out.println("Wall detected!");
}

// Return to normal (human control) mode
roomba.stop();

// Close serial connection
roomba.disconnect();

```

## Table of contents
* [Roomba commands](#roomba-commands)
    * [Power commands](#power-commands)
    * [Mode commands](#mode-commands)
    * [Cleaning commands](#cleaning-commands)
    * [Actuator commands](#actuator-commands)
* [Roomba songs](#roomba-songs)
    * [Create song example](#create-song-example)
    * [Song commands](#song-commands)
* [Sensors](#sensors)
    * [Sensor commands](#sensor-commands)
    * [Sensor values](#sensor-values)
* [License](#license)

    

## Roomba commands

### Power commands

#### `public void start()`

This command starts the OI. You must always send the Start command before sending any other commands to the OI.

#### `public void startup()`

This command starts the OI of the roomba and puts it in Safe mode which enables user control. Safe mode turns off all LEDs. This will run the 'start' and 'safe' commands.

Note: Wait at least 500ms before sending any commands.

#### `public void stop()`

This command stops the OI. All streams will stop and the robot will no longer respond to commands. Use this command when you are finished working with the robot.

#### `public void powerOff()`

This command powers down Roomba. The OI can be in Passive, Safe, or Full mode to accept this command.

#### `public void reset()`

Reset the Roomba after error, this will also run the 'start' and 'safe' commands.

#### `public void hardReset()`

This command resets the Roomba, as if you had removed and reinserted the battery.

Note: Wait at least 5000ms before sending any commands.

### Mode commands

#### `public void safeMode()`

This command puts the OI into Safe mode, enabling user control of Roomba. It turns off all LEDs. The OI can be in Passive, Safe, or Full mode to accept this command. If a safety condition occurs Roomba reverts automatically to Passive mode.

#### `public void fullMode()`

This command gives you complete control over Roomba by putting the OI into Full mode, and turning off the cliff, wheel-drop and internal charger safety features. That is, in Full mode, Roomba executes any command that you send it, even if the internal charger is plugged in, or command triggers a cliff or wheel drop condition.

### Cleaning commands

#### `public void clean()`

This command starts the default cleaning mode. This is the same as pressing Roomba’s Clean button, and will pause a cleaning cycle if one is already in progress.

#### `public void cleanMax()`

This command starts the Max cleaning mode, which will clean until the battery is dead. This command will pause a cleaning cycle if one is already in progress.

#### `public void cleanSpot()`

This command starts the Spot cleaning mode. This is the same as pressing Roomba’s Spot button, and will pause a cleaning cycle if one is already in progress.

#### `public void seekDock()`

This command directs Roomba to drive onto the dock the next time it encounters the docking beams. This is the same as pressing Roomba’s Dock button, and will pause a cleaning cycle if one is already in progress.

#### `public void schedule(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, int sun_hour, int sun_min, int mon_hour, int mon_min, int tue_hour, int tue_min, int wed_hour, int wed_min, int thu_hour, int thu_min, int fri_hour, int fri_min, int sat_hour, int sat_min) throws IllegalArgumentException`

This command sends Roomba a new schedule. To disable scheduled cleaning, send all false and 0s.

 * **Parameters:**
   * `sun` — Enable Sunday scheduling
   * `mon` — Enable Monday scheduling
   * `tue` — Enable Tuesday scheduling
   * `wed` — Enable Wednesday scheduling
   * `thu` — Enable Thursday scheduling
   * `fri` — Enable Friday scheduling
   * `sat` — Enable Saturday scheduling
   * `sun_hour` — Sunday scheduled hour
   * `sun_min` — Sunday scheduled minute
   * `mon_hour` — Monday scheduled hour
   * `mon_min` — Monday scheduled minute
   * `tue_hour` — Tuesday scheduled hour
   * `tue_min` — Tuesday scheduled minute
   * `wed_hour` — Wednesday scheduled hour
   * `wed_min` — Wednesday scheduled minute
   * `thu_hour` — Thursday scheduled hour
   * `thu_min` — Thursday scheduled minute
   * `fri_hour` — Friday scheduled hour
   * `fri_min` — Friday scheduled minute
   * `sat_hour` — Saturday scheduled hour
   * `sat_min` — Saturday scheduled minute
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void setDayTime(int day, int hour, int minute) throws IllegalArgumentException`

This command sets Roomba’s clock.

 * **Parameters:**
   * `day` — Day number: 0 = Sunday, 1 = Monday ... 6 = Saturday
   * `hour` — Hour in 24hour format 0-23
   * `minute` — Minute 0-59
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.


### Actuator commands

#### `public void drive(int velocity, int radius) throws IllegalArgumentException`

This command controls Roomba’s drive wheels.

 * **Parameters:**
   * `velocity` — The average velocity of the drive wheels in millimeters per second (mm/s)

     A positive velocity makes the roomba drive forward, a negative velocity

     makes it drive backwards.

     min: -500mm/s max: 500mm/s
   * `radius` — The radius in millimeters at which Roomba will turn

     The longer radii make Roomba drive straighter, while the shorter radii make Roomba turn more.

     The radius is measured from the center of the turning circle to the center of Roomba.

     <p>min: -2000mm max: 2000mm</p>

     Special cases:

     <ul>

     <li>Straight = 32767 or 32768</li>

     <li>Turn in place clockwise = -1</li>

     <li>Turn in place counter-clockwise = 1</li>

     </ul>
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void driveDirect(int rightVelocity, int leftVelocity) throws IllegalArgumentException`

This command lets you control the forward and backward motion of Roomba’s drive wheels independently. A positive velocity makes that wheel drive forward, while a negative velocity makes it drive backward.

 * **Parameters:**
   * `rightVelocity` — Right wheel velocity min: -500 mm/s, max: 500 mm/s
   * `leftVelocity` — Left wheel velocity min: -500 mm/s, max: 500 mm/s
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void drivePWM(int rightPWM, int leftPWM) throws IllegalArgumentException`

This command lets you control the raw forward and backward motion of Roomba’s drive wheels independently. A positive PWM makes that wheel drive forward, while a negative PWM makes it drive backward. The PWM values are percentages: 100% is full power forward, -100% is full power reverse.

 * **Parameters:**
   * `rightPWM` — Right wheel PWM (min: -100%, max: 100%)
   * `leftPWM` — Left wheel PWM (min: -100%, max: 100%)
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void motors(boolean sideBrush, boolean vacuum, boolean mainBrush, boolean sideBrushClockwise, boolean mainBrushOutward)`

This command lets you control the forward and backward motion of Roomba’s main brush, side brush, and vacuum independently. Motor velocity cannot be controlled with this command, all motors will run at maximum speed when enabled. The main brush and side brush can be run in either direction. The vacuum only runs forward.

 * **Parameters:**
   * `sideBrush` — Turns on side brush
   * `vacuum` — Turns on vacuum
   * `mainBrush` — Turns on main brush
   * `sideBrushClockwise` — if true the side brush will turn clockwise (default: counterclockwise)
   * `mainBrushOutward` — if true the side brush will turn outward (default: inward)

#### `public void motorsPWM(int mainBrushPWM, int sideBrushPWM, int vacuumPWM) throws IllegalArgumentException`

This command lets you control the speed of Roomba’s main brush, side brush, and vacuum independently. The main brush and side brush can be run in either direction. The vacuum only runs forward. Positive speeds turn the motor in its default (cleaning) direction. Default direction for the side brush is counterclockwise. Default direction for the main brush/flapper is inward. The PWM values are percentages: 100% is full power forward, -100% is full power reverse.

 * **Parameters:**
   * `mainBrushPWM` — Main brush PWM (min: -100%, max: 100%)
   * `sideBrushPWM` — Side brush PWM (min: -100%, max: 100%)
   * `vacuumPWM` — Vacuum PWM (min: 0%, max: 100%)
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void relativeLeds(boolean debris, boolean spot, boolean dock, boolean check_robot, int powerColor, int powerIntensity) throws IllegalArgumentException`

This command controls the LEDs common to all models of Roomba 600.

 * **Parameters:**
   * `debris` — Turns on the debris LED
   * `spot` — Turns on the spot LED
   * `dock` — Turns on the dock LED
   * `check_robot` — Turns on the check robot LED
   * `powerColor` — Controls the power LED red color relative to green: 0% = green, 100% = red.

     Intermediate values are intermediate colors (orange, yellow, etc).
   * `powerIntensity` — Controls the intensity of the power led. 0% = off, 100% = full intensity.

     Intermediate values are intermediate intensities.
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void leds(boolean debris, boolean spot, boolean dock, boolean check_robot, int powerColor, int powerIntensity) throws IllegalArgumentException`

This command controls the LEDs common to all models of Roomba 600.

 * **Parameters:**
   * `debris` — Turns on the debris LED
   * `spot` — Turns on the spot LED
   * `dock` — Turns on the dock LED
   * `check_robot` — Turns on the check robot LED
   * `powerColor` — Controls the power LED color: 0 = green, 255 = red.

     Intermediate values are intermediate colors (orange, yellow, etc).
   * `powerIntensity` — Controls the intensity of the power led. 0 = off, 255 = full intensity.

     Intermediate values are intermediate intensities.
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void schedulingLeds(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean colon, boolean pm, boolean am, boolean clock, boolean schedule)`

This command controls the state of the scheduling LEDs present on the Roomba 560 and 570.

 * **Parameters:**
   * `sun` — Turn on scheduling Sunday LED
   * `mon` — Turn on scheduling Monday LED
   * `tue` — Turn on scheduling Tuesday LED
   * `wed` — Turn on scheduling Wednesday LED
   * `thu` — Turn on scheduling Thursday LED
   * `fri` — Turn on scheduling Friday LED
   * `sat` — Turn on scheduling Saturday LED
   * `colon` — Turn on scheduling Colon LED
   * `pm` — Turn on scheduling PM LED
   * `am` — Turn on scheduling AM LED
   * `clock` — Turn on scheduling clock LED
   * `schedule` — Turn on scheduling schedule LED

#### `public void digitLedsAscii(char char0, char char1, char char2, char char3) throws IllegalArgumentException`

This command controls the four 7 segment displays on the Roomba 560 and 570 using ASCII character codes. Because a 7 segment display is not sufficient to display alphabetic characters properly, all characters are an approximation, and not all ASCII codes are implemented. Note: Use a space for an empty character

 * **Parameters:**
   * `char0` — First character
   * `char1` — Second character
   * `char2` — Third character
   * `char3` — Fourth character
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void buttons(boolean clean, boolean spot, boolean dock, boolean minute, boolean hour, boolean day, boolean schedule, boolean clock)`

This command lets you push Roomba’s buttons. The buttons will automatically release after 1/6th of a second.

 * **Parameters:**
   * `clean` — Presses the clean button
   * `spot` — Presses the spot button
   * `dock` — Presses the dock button
   * `minute` — Presses the minute button
   * `hour` — Presses the hour button
   * `day` — Presses the day button
   * `schedule` — Presses the schedule button
   * `clock` — Presses the clock button

## Roomba songs

### Create song example
```java
// Fur Elise - Beethoven
RoombaSongNote[] notes = {
    new RoombaSongNote(RoombaNote.E2, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.D2Sharp, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.E2, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.D2Sharp, RoombaNoteDuration.EightNote),

    new RoombaSongNote(RoombaNote.E2, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.B1, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.D2, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.C2, RoombaNoteDuration.EightNote),

    new RoombaSongNote(RoombaNote.A1, RoombaNoteDuration.QuarterNote),
    new RoombaSongNote(RoombaNote.Pause, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.C1, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.E1, RoombaNoteDuration.
    new RoombaSongNote(RoombaNote.A1, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.B1, RoombaNoteDuration.QuarterNote),
    new RoombaSongNote(RoombaNote.Pause, RoombaNoteDuration.EightNote),
    new RoombaSongNote(RoombaNote.E1, RoombaNoteDuration.EightNote)
};
// Save to song number 0, tempo (in BPM) 125
roomba.song(0, notes, 125);
// Play song 0
roomba.play(0);
```

### Song commands

#### `public void song(int songNumber, RoombaSongNote[] notes, int tempo) throws IllegalArgumentException`

This command lets you specify up to four songs to the OI that you can play at a later time. Each song is associated with a song number. The Play command uses the song number to identify your song selection. Each song can contain up to sixteen notes.

 * **Parameters:**
   * `songNumber` — Song number (0-4)
   * `notes` — Array of RoombaSongNote (max. 16)
   * `tempo` — Tempo in BPM (min. 60 BPM, max. 800 BPM)
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

#### `public void play(int songNumber) throws IllegalArgumentException`

This command lets you select a song to play from the songs added to Roomba using the Song command. You must add one or more songs to Roomba using the Song command in order for the Play command to work.

 * **Parameters:** `songNumber` — Song number (0-4)
 * **Exceptions:** `IllegalArgumentException` — One of the arguments is out of bounds.

## Sensors

### Sensor commands

#### `public void updateSensors() throws RuntimeException`

This command requests new sensor data from the roomba.

Note: Don't invoke this method more than once per 50ms, this will possibly corrupt the sensor data received from the roomba.

 * **Exceptions:** `RuntimeException` — If sensor data updates are requested more than once per 50ms.

### Sensor values

#### `public boolean bumpRight()`

Get value of right bumper sensor.

 * **Returns:** True if bumped right

#### `public boolean bumpLeft()`

Get value of left bumper sensor.

 * **Returns:** True if bumped left

#### `public boolean wheelDropRight()`

Get value of right wheel drop sensor.

 * **Returns:** True if wheel drops right

#### `public boolean wheelDropLeft()`

Get value of left wheel drop sensor.

 * **Returns:** True if wheel drops left

#### `public boolean wall()`

Get value of wall sensor.

 * **Returns:** True if wall is seen

#### `public boolean cliffLeft()`

Get value of cliff left sensor.

 * **Returns:** True if cliff is seen on left side

#### `public boolean cliffFrontLeft()`

Get value of cliff front left sensor.

 * **Returns:** True if cliff is seen on front left

#### `public boolean cliffFrontRight()`

Get value of cliff front right sensor.

 * **Returns:** True if cliff is seen on front right

#### `public boolean cliffRight()`

Get value of cliff right sensor.

 * **Returns:** True if cliff is seen on right side

#### `public boolean virtualWall()`

Get value of virtual wall sensor.

 * **Returns:** True if a virtual wall is detected

#### `public boolean sideBrushOvercurrent()`

Get value of side brush overcurrent sensor.

 * **Returns:** True if side brush overcurrent

#### `public boolean mainBrushOvercurrent()`

Get value of main brush overcurrent sensor.

 * **Returns:** True if main brush overcurrent

#### `public boolean wheelOvercurrentRight()`

Get value of right wheel overcurrent sensor.

 * **Returns:** True if right wheel overcurrent

#### `public boolean wheelOvercurrentLeft()`

Get value of left wheel overcurrent sensor.

 * **Returns:** True if left wheel overcurrent

#### `public int dirtDetectLevel()`

Get the level of the dirt detect sensor.

 * **Returns:** Dirt level (0-255)

#### `public int infraredCharacterOmni()`

Get the character currently received by the omnidirectional receiver.

 * **Returns:** Received character (0-255)

#### `public int infraredCharacterLeft()`

Get the character currently received by the left receiver.

 * **Returns:** Received character (0-255)

#### `public int infraredCharacterRight()`

Get the character currently received by the right receiver.

 * **Returns:** Received character (0-225)

#### `public boolean buttonCleanPressed()`

Check if the clean button is pressed.

 * **Returns:** True if pressed

#### `public boolean buttonSpotPressed()`

Check if the spot button is pressed.

 * **Returns:** True if pressed

#### `public boolean buttonDockPressed()`

Check if the dock button is pressed.

 * **Returns:** True if pressed

#### `public boolean buttonMinutePressed()`

Check if the minute button is pressed.

 * **Returns:** True if pressed

#### `public boolean buttonHourPressed()`

Check if the hour button is pressed.

 * **Returns:** True if pressed

#### `public boolean buttonDayPressed()`

Check if the day button is pressed.

 * **Returns:** True if pressed

#### `public boolean buttonSchedulePressed()`

Check if the schedule button is pressed.

 * **Returns:** True if pressed

#### `public boolean buttonClockPressed()`

Check if the clock button is pressed.

 * **Returns:** True if pressed

#### `public int distanceTraveled()`

Get distance travelled in mm since the last sensor data request.

Note: if the sensor data is not polled frequently enough this value is capped at its minimum or maximum (-32768, 32767)

 * **Returns:** Distance travelled in mm since last sensor data request

#### `public int angleTurned()`

Get the angle turned in degrees since the last sensor data request. Counter-clockwise angles are positive, clockwise angles are negative.

Note: if the sonsor data is not polled requently enough this value is capped at its minimum or maximum (-32768, 32767)

 * **Returns:** Angle turned in degrees since last sensor data request

#### `public int chargingState()`

Get current charging state

States:

<ul> <li>0 = Not charging</li> <li>1 = Reconditioning charging</li> <li>2 = Full charging</li> <li>3 = Trickle Charging</li> <li>4 = Waiting</li> <li>5 = Charging Fault condition</li> </ul>

 * **Returns:** Charging state (0-5)

#### `public int batteryVoltage()`

Get the voltage of the battery in millivolt (mV)

 * **Returns:** battery voltage (0 - 65535 mV)

#### `public int batteryCurrent()`

Get the current in milliamps (mA) flowing into or out of roomba's battery. Negative currents indicate that the current is flowing out of the battery, as during normal running. Positive currents indicate that the current is flowing into the battery, as during charging.

 * **Returns:** Current in milliamps (-32768, 32768 mA)

#### `public int batteryTemperature()`

Get the temperature of Roomba's battery in degrees Celsius.

 * **Returns:** Battery temperature in degrees Celsius (-128, 127)

#### `public int batteryCharge()`

Get the estimated charge of the roomba's battery in milliamp-hours (mAh).

 * **Returns:** Estimated battery charge (0 - 65535 mAh)

#### `public int batteryCapacity()`

Get the estimated charge capacity of roomba's battery in milliamp-hours (mAh)

 * **Returns:** Estimated charge capacity (0 - 65535 mAh)

#### `public int wallSignal()`

Get the strength of the wall signal.

 * **Returns:** Strength of wall signal (0-1023)

#### `public int cliffSignalLeft()`

Get the strength of the cliff left signal.

 * **Returns:** Strength of cliff left signal(0-4095)

#### `public int cliffSignalFrontLeft()`

Get the strength of the cliff front left signal.

 * **Returns:** Strength of cliff front left signal(0-4095)

#### `public int cliffSignalFrontRight()`

Get the strength of the cliff front right signal.

 * **Returns:** Strength of cliff front left signal(0-4095)

#### `public int cliffSignalRight()`

Get the strength of the cliff right signal.

 * **Returns:** Strength of cliff right signal(0-4095)

#### `public boolean internalChargerAvailable()`

Check if the internal charger is present and powered.

 * **Returns:** True if present and powered.

#### `public boolean homebaseChargerAvailable()`

Check if the homebase charger is present and powered.

 * **Returns:** True if present and powered.

#### `public int mode()`

Get the current OI mode.

OI modes:

<ul> <li>0 = Off</li> <li>1 = Passive</li> <li>2 = Safe</li> <li>3 = Full</li> </ul>

 * **Returns:** Current OI mode (0-3)

#### `public int songNumber()`

Get the currently selected song.

 * **Returns:** Selected song number (0-15)

#### `public boolean songPlaying()`

Check if a song is playing.

 * **Returns:** True if a song is playing.

#### `public int requestedVelocity()`

Get the velocity most recently requested with a Drive command.

 * **Returns:** Requested velocity (-500 - 500mm/s)

#### `public int requestedRadius()`

Get the radius most recently requested with a Drive command.

 * **Returns:** Requested radius (-32768 - 32767mm)

#### `public int requestedVelocityRight()`

Get the right wheel velocity most recently requested with a Drive Direct command.

 * **Returns:** Requested right wheel velocity (-500 - 500mm/s)

#### `public int requestedVelocityLeft()`

Get the left wheel velocity most recently requested with a Drive Direct command.

 * **Returns:** Requested left wheel velocity (-500 - 500mm/s)

#### `public int encoderCountsLeft()`

Get the (cumulative) number of raw left encoder counts.

Note: This number will roll over to 0 after it passes 65535.

 * **Returns:** Cumulative left encoder counts (0-65535)

#### `public int encoderCountsRight()`

Get the (cumulative) number of raw right encoder counts.

Note: This number will roll over to 0 after it passes 65535.

 * **Returns:** Cumulative right encoder counts (0-65535)

#### `public boolean lightBumperLeft()`

Check if the left light bumper detects an obstacle.

 * **Returns:** True on obstacle

#### `public boolean lightBumperFrontLeft()`

Check if the front left light bumper detects an obstacle.

 * **Returns:** True on obstacle

#### `public boolean lightBumperCenterLeft()`

Check if the center left light bumper detects an obstacle.

 * **Returns:** True on obstacle

#### `public boolean lightBumperCenterRight()`

Check if the center right light bumper detects an obstacle.

 * **Returns:** True on obstacle

#### `public boolean lightBumperFrontRight()`

Check if the front right light bumper detects an obstacle.

 * **Returns:** True on obstacle

#### `public boolean lightBumperRight()`

Check if the right light bumper detects an obstacle.

 * **Returns:** True on obstacle

#### `public int lightBumperSignalLeft()`

Get the strength of the light bumper left signal.

 * **Returns:** Signal strength (0-4095)

#### `public int lightBumperSignalFrontLeft()`

Get the strength of the light bumper front left signal.

 * **Returns:** Signal strength (0-4095)

#### `public int lightBumperSignalCenterLeft()`

Get the strength of the light bumper center left signal.

 * **Returns:** Signal strength (0-4095)

#### `public int lightBumperSignalCenterRight()`

Get the strength of the light bumper center right signal.

 * **Returns:** Signal strength (0-4095)

#### `public int lightBumperSignalFrontRight()`

Get the strength of the light bumper front right signal.

 * **Returns:** Signal strength (0-4095)

#### `public int lightBumperSignalRight()`

Get the strength of the light bumper right signal.

 * **Returns:** Signal strength (0-4095)

#### `public int motorCurrentLeft()`

Get the current being drawn by the left wheel motor in milli Ampere (mA).

 * **Returns:** Motor current in mA (-32768 - 32767 mA)

#### `public int motorCurrentRight()`

Get the current being drawn by the right wheel motor in milli Ampere (mA).

 * **Returns:** Motor current in mA (-32768 - 32767 mA)

#### `public int motorCurrentMainBrush()`

Get the current being drawn by the main brush motor in milli Ampere (mA).

 * **Returns:** Motor current in mA (-32768 - 32767 mA)

#### `public int motorCurrentSideBrush()`

Get the current being drawn by the side brush motor in milli Ampere (mA).

 * **Returns:** Motor current in mA (-32768 - 32767 mA)

#### `public boolean stasis()`

Check if the roomba is making forward progress.

Note: this method returns false when the roomba is turning, driving backward, or is not driving.

 * **Returns:** True if making forward progress
 
## License
[MIT LICENSE](./LICENSE) - Copyright (c) 2016 Geoffrey Mastenbroek
