package test;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class Display {

	public static void main(String[] args) {
		Port portDistance = LocalEV3.get().getPort("S2");
		EV3UltrasonicSensor distanceSensor = new EV3UltrasonicSensor(portDistance);
		SampleProvider distanceProvider = distanceSensor.getDistanceMode();
		float[] distanceSample = new float[distanceProvider.sampleSize()];
		
		while(Button.ESCAPE.isUp()) {
			distanceSensor.fetchSample(distanceSample, 0);
			//return distanceSample[0];
			System.out.println(distanceSample[0]);
			//Button.waitForAnyPress();
		}
	}
}
