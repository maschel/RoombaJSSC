# RoombaJSSC
RoombaJSSC is a JAVA library to control and/or read sensor values of a Roomba (500 & 600 series).
This library implements (basically) all the commands and sensors specified in the [iRobot Create 2 Open Interace Specification](http://www.irobot.com/~/media/MainSite/PDFs/About/STEM/Create/create_2_Open_Interface_Spec.pdf) based on Roomba 600.

## Why this library?
* Fully implemented iRobot specifications (for roomba 500/600 series)
* Uses the JSSC library for serial communication, which contains native libraries for many platforms:
    * Windows (x86, x86-64)
    * Linux(x86, x86-64, ARM soft & hard float)
    * Solaris(x86, x86-64)
    * Mac OS X(x86, x86-64, PPC, PPC64)
* JavaDoc on all methods describing commands & values as given in iRobot specifications.

## Basic usage
### Standard library lifecycle
```java
RoombaJSSC roomba = new RoombaJSSCSerial();

// Get available serial port(s) (not mandatory)
String[] ports = roomba.portList();

// Connect
roomba.connect("/dev/a/serial/port"); // Use portList() to get available ports.

// Make roomba ready for communication & control (safe mode)
roomba.startUp();

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

### Roomba commands

#### Power commands

##### Start
This command starts the OI. You must always send the Start command before sending any other commands to the OI.
```java
roomba.start();
```

##### Startup
This command starts the OI of the roomba and puts it in Safe mode which enables user control. Safe mode turns off all LEDs. This will run the 'start' and 'safe' commands.
```java
roomba.startup();
```

##### Stop
This command stops the OI. All streams will stop and the robot will no longer respond to commands. Use this command when you are finished working with the robot.
```java
roomba.stop();
```

##### PowerOff
This command powers down Roomba. The OI can be in Passive, Safe, or Full mode to accept this command.
```java
roomba.powerOff();
```

##### Reset
Reset the Roomba after error, this will also run the 'start' and 'safe' commands.
```java
roomba.reset();
```

##### HardReset
This command resets the Roomba, as if you had removed and reinserted the battery.
Note: Wait at least 5000ms before sending any commands.
```java
roomba.hardReset();
```

#### Mode commands

##### SafeMode
This command puts the OI into Safe mode, enabling user control of Roomba. It turns off all LEDs. The OI can be in Passive, Safe, or Full mode to accept this command.
If a safety condition occurs Roomba reverts automatically to Passive mode.
```java
roomba.safeMode();
```

##### FullMode
This command gives you complete control over Roomba by putting the OI into Full mode, and turning off the cliff, wheel-drop and internal charger safety features.
That is, in Full mode, Roomba executes any command that you send it, even if the internal charger is plugged in, or command triggers a cliff or wheel drop condition.
```java
roomba.fullMode();
```

#### Cleaning commands

##### Clean
This command starts the default cleaning mode. This is the same as pressing Roomba’s Clean button, and will pause a cleaning cycle if one is already in progress.
```java
roomba.clean();
```

##### CleanMax
This command starts the Max cleaning mode, which will clean until the battery is dead. This command will pause a cleaning cycle if one is already in progress.
```java
roomba.cleanMax();
```

##### CleanSpot
This command starts the Spot cleaning mode. This is the same as pressing Roomba’s Spot button, and will pause a cleaning cycle if one is already in progress.
```java
roomba.cleanSpot();
```

##### SeekDock
This command directs Roomba to drive onto the dock the next time it encounters the docking beams.
This is the same as pressing Roomba’s Dock button, and will pause a cleaning cycle if one is already in progress.
```java
roomba.seekDock();
```

##### Schedule
This command sends Roomba a new schedule. To disable scheduled cleaning, send all false and 0s.
- sun-mon Enable {day} scheduling
- {day}_hour - {day} scheduled hour
- {day}_min - {day} scheduled minute
```java
schedule(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat,
                                int sun_hour, int sun_min, int mon_hour, int mon_min, int tue_hour, int tue_min,
                                int wed_hour, int wed_min, int thu_hour, int thu_min, int fri_hour, int fri_min,
                                int sat_hour, int sat_min)
```

##### SetDayTime
This command sets Roomba’s clock.
- day: Day number: 0 = Sunday, 1 = Monday ... 6 = Saturday
- hour: Hour in 24hour format 0-23
- minute: Minute 0-59
```java
setDayTime(int day, int hour, int minute)
```

#### Roomba actuator commands

##### Drive
This command controls Roomba’s drive wheels.
- velocity: The average velocity of the drive wheels in millimeters per second (mm/s) A positive velocity makes the roomba drive forward, a negative velocity makes it drive backwards. min: -500mm/s max: 500mm/s
- radius: The radius in millimeters at which Roomba will turn The longer radii make Roomba drive straighter, while the shorter radii make Roomba turn more. The radius is measured from the center of the turning circle to the center of Roomba. min: -2000mm max: 2000mm
Special cases:
- Straight = 32767 or 32768
- Turn in place clockwise = -1
- Turn in place counter-clockwise = 1
```java
drive(int velocity, int radius)
```

##### DriveDirect
This command lets you control the forward and backward motion of Roomba’s drive wheels independently. A positive velocity makes that wheel drive forward, while a negative velocity makes it drive backward.
- rightVelocity: Right wheel velocity min: -500 mm/s, max: 500 mm/s
- leftVelocity: Left wheel velocity min: -500 mm/s, max: 500 mm/s
```java
driveDirect(int rightVelocity, int leftVelocity)
```

##### DrivePWM
This command lets you control the raw forward and backward motion of Roomba’s drive wheels independently. A positive PWM makes that wheel drive forward, while a negative PWM makes it drive backward. The PWM values are percentages: 100% is full power forward, -100% is full power reverse.
- rightPWM: Right wheel PWM (min: -100%, max: 100%)
- leftPWM: Left wheel PWM (min: -100%, max: 100%)
```java
drivePWM(int rightPWM, int leftPWM)
```

##### Motors
This command lets you control the forward and backward motion of Roomba’s main brush, side brush, and vacuum independently. Motor velocity cannot be controlled with this command, all motors will run at maximum speed when enabled. The main brush and side brush can be run in either direction. The vacuum only runs forward.
- sideBrush: Turns on side brush
- vacuum: Turns on vacuum
- mainBrush: Turns on main brush
- sideBrushClockwise: if true the side brush will turn clockwise (default: counterclockwise)
- mainBrushOutward: if true the side brush will turn outward (default: inward)
```java
motors(boolean sideBrush, boolean vacuum, boolean mainBrush, boolean sideBrushClockwise, boolean mainBrushOutward)
```

##### MotorsPWM
This command lets you control the speed of Roomba’s main brush, side brush, and vacuum independently. The main brush and side brush can be run in either direction. The vacuum only runs forward. Positive speeds turn the motor in its default (cleaning) direction. Default direction for the side brush is counterclockwise. Default direction for the main brush/flapper is inward. The PWM values are percentages: 100% is full power forward, -100% is full power reverse.
- mainBrushPWM: Main brush PWM (min: -100%, max: 100%)
- sideBrushPWM: Side brush PWM (min: -100%, max: 100%)
- vacuumPWM: Vacuum PWM (min: 0%, max: 100%)
```java
motorsPWM(int mainBrushPWM, int sideBrushPWM, int vacuumPWM)
```

##### Leds
This command controls the LEDs common to all models of Roomba 600.
- debris: Turns on the debris LED
- spot: Turns on the spot LED
- dock: Turns on the dock LED
- check_robot: Turns on the check robot LED
- powerColor: Controls the power LED red color relative to green: 0% = green, 100% = red. Intermediate values are intermediate colors (orange, yellow, etc).
- powerIntensity: Controls the intensity of the power led. 0% = off, 100% = full intensity. Intermediate values are intermediate intensities.
```java
leds(boolean debris, boolean spot, boolean dock, boolean check_robot, int powerColor, int powerIntensity)
```

##### SchedulingLeds
This command controls the state of the scheduling LEDs present on the Roomba 560 and 570.
- sun-sat Turn on scheduling {day} LED
- colon: Turn on scheduling Colon LED
- pm: Turn on scheduling PM LED
- am: Turn on scheduling AM LED
- clock: Turn on scheduling clock LED
- schedule: Turn on scheduling schedule LED
```java
schedulingLeds(boolean sun, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean colon, boolean pm, boolean am, boolean clock, boolean schedule)
```

##### DigitLedsASCII
This command controls the four 7 segment displays on the Roomba 560 and 570 using ASCII character codes. Because a 7 segment display is not sufficient to display alphabetic characters properly, all characters are an approximation, and not all ASCII codes are implemented. Note: Use a space for an empty character
- char0: First character
- char1: Second character
- char2: Third character
- char3: Fourth character
```java
digitLedsAscii(char char0, char char1, char char2, char char3)
```

##### Buttons
This command lets you push Roomba’s buttons. The buttons will automatically release after 1/6th of a second.
- clean-clock: Presses the {button} button
```java
buttons(boolean clean, boolean spot, boolean dock, boolean minute, boolean hour, boolean day, boolean schedule, boolean clock)
```

#### Roomba songs

##### Create/save/play a song
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

#### Sensor commands

#### UpdateSensors
This command requests new sensor data from the roomba.
Note: Don't invoke this method more than once per 50ms, this will possibly corrupt the sensor data received from the roomba.
```java
roomba.updateSensors();
```

#### Get sensor values
For a full list of available sensors see the JavaDoc.