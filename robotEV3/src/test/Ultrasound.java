package test;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;

public class Ultrasound {
	
	public static void main(String[] args) {
		
		System.out.println("Hello");
		Button.waitForAnyPress();
		
		Port port = LocalEV3.get().getPort("S2");

		// Get an instance of the Ultrasonic EV3 sensor
		SensorModes sensor = new EV3UltrasonicSensor(port);

		// get an instance of this sensor in measurement mode
		SampleProvider distance= sensor.getMode("Distance");

		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[distance.sampleSize()];

		
		
		// fetch a sample
		while(true) {
		  distance.fetchSample(sample, 0);
		  System.out.println(sample);
		}
	}
}
