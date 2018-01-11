package test;



import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Ultrasound {
	
	public static void main(String[] args) {
		
		System.out.println("Hello");
		Button.waitForAnyPress();
		
		Port port = LocalEV3.get().getPort("S2");

		// Get an instance of the Ultrasonic EV3 sensor
		EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(port);

		// get an instance of this sensor in measurement mode
		SampleProvider distance = sensor.getDistanceMode();

		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[distance.sampleSize()];

		
		// fetch a sample
		while(Button.ESCAPE.isUp()) {
			//Button.waitForAnyPress();
			Delay.msDelay(500);
			sensor.fetchSample(sample, 0);
			for (float f : sample) {
				LCD.drawString("Distance : " + f +  " m", 0, 3);
			}
		}
	}
	
	public void forwardBecauseAWall()
	{
		Motor.D.forward();
		Motor.B.forward();
	}
	
	public void stopBecauseNotWall()
	{
		Motor.D.stop();
	 	Motor.B.stop();
	}
}
