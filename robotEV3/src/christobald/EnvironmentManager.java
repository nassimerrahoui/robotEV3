package christobald;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EnvironmentManager {
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
}
