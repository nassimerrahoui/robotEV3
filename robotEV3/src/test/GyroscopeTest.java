package test;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class GyroscopeTest {
	
	public static void main(String[] args) {
		Port port = LocalEV3.get().getPort("S1");

		EV3GyroSensor gyro = new EV3GyroSensor(port);
		
		gyro.reset();
		SampleProvider sample_provider = gyro.getAngleMode();
		
		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[sample_provider.sampleSize()];

		// fetch a sample
		while(Button.ESCAPE.isUp()) {
			gyro.fetchSample(sample, 0);
			LCD.drawString("Angle : " + sample[0] +  " degres", 0, 3);
			


		}
		//close the sensor
		gyro.close();

		
	}

}
