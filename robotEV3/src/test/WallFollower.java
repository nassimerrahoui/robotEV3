package test;


import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Stopwatch;

public class WallFollower // Make sure the classname matches the filename (case-sensitive)
{
	private MovePilot  bot; // Field declaration for a Pilot object
	private boolean isRunning = true;

	//the code in the main method will not change (except for the classname)
	public static void main(String[] args)
	{

		WallFollower program = new WallFollower(); // Matches classname (case-sensitive)
		Button.ESCAPE.waitForPressAndRelease(); // Register this object as a listener
		program.run();

	} // main()

	/*

    The constructor generally instantiates field objects and
    sets other initial state information

	 */
	public WallFollower() 
	{
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 56).offset(-72);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.D, 56).offset(72);
		Chassis chassis = new WheeledChassis(new Wheel[]{wheel1, wheel2}, WheeledChassis.TYPE_DIFFERENTIAL); 
		bot = new MovePilot(chassis); // Instantiate a Pilot object named "bot"
		bot.rotate(250);
		bot.setLinearSpeed(250);
	} //constructor

	/*

    Your code goes in this method

	 */
	public void run()
	{

		// Create a TouchSensor that monitors sensor port S1
		EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S1);

		// Create a Stopwatch that monitors elapsed time in milliseconds
		Stopwatch sw = new Stopwatch();

		long time_in_milliseconds = 10000L;
		int distIncrement = getTravelDistanceFromInches(5);
		double angleIncrement = getCalibratedAngle(10);

		// Since we're going backwards, technically:
		distIncrement *= -1;

		pause(500);

		// Reset the stop watch to 0, before entering the loop
		sw.reset();

		while ( sw.elapsed() < time_in_milliseconds && isRunning )
		{
			bot.backward();

			if (touch.getTouchMode().sampleSize()==1)
			{
				bot.stop();

				// Reverse and slightly rotate
				bot.travel(0-distIncrement);
				bot.rotate(angleIncrement);

				// If we tapped the wall, reset our timer:
				sw.reset();
			}
		}

		touch.close();
	}

	/*

    The following method allows the bot to do what it was doing,
    but suspends execution of the next statement.

    When this method returns, the program continues executing
    where it left off.

	 */
	public void pause(int milli)
	{

		try
		{
			Thread.sleep(milli);
		}

		catch(InterruptedException e)
		{

		}

	}


	public double getCalibratedAngle(double angle)
	{
		// Adjust according to calibration
		return angle * 1.25;
	}

	public int getTravelDistanceFromInches(int inches)
	{
		// 300 = 11.5 inches
		// ~26 = 1 inch
		return inches * 26;
	}
}