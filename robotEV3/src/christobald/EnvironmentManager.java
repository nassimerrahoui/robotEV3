package christobald;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EnvironmentManager {
	public final int moustacheBreakPoint = 2;
	public final int tailBreakPoint = 2;
	
	public enum HeadDirection {
		  LEFT,
		  RIGHT,
		  FRONT
	}
	
	EV3UltrasonicSensor distanceSensor;
	float[] distanceSample;
 	public EnvironmentManager(String DistanceSensorPort) {
		Port port = LocalEV3.get().getPort(DistanceSensorPort);
		distanceSensor = new EV3UltrasonicSensor(port);
		SampleProvider distanceProvider = distanceSensor.getDistanceMode();
		distanceSample = new float[distanceProvider.sampleSize()];
	}
	
	public float getSensorDistance() {
		distanceSensor.fetchSample(distanceSample, 0);
		return distanceSample[0];
	}
	
	public float look(HeadDirection direction) {
		return (float) 0.1;
	}
	public float getDistanceOn(HeadDirection direction) {
		return (float) 0.1;
	}
	
	public float getMoustacheValue() {
		return (float) 0.2;
	}
	public float getTailValue() {
		return (float) 0.2;
	}
	public boolean isMoustachePressed() {
		return false;
	}
	public boolean isTailPressed() {
		return false;
	}
}
