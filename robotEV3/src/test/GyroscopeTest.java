package test;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class GyroscopeTest {
	
	public static void main(String[] args) {
		Port port = LocalEV3.get().getPort("S4");
 
		EV3GyroSensor gyro = new EV3GyroSensor(port);
		
		gyro.reset();
		SampleProvider sample_provider = gyro.getAngleMode();
		
		// initialize an array of floats for fetching samples. 
		// Ask the SampleProvider how long the array should be
		float[] sample = new float[sample_provider.sampleSize()];
		
		Port sensorPort = LocalEV3.get().getPort("S1");           
		EV3ColorSensor colorSensor = new EV3ColorSensor(sensorPort);
		SampleProvider colorIdSensor = colorSensor.getColorIDMode();
		int sampleSize = colorIdSensor.sampleSize();            
		float[] sampleColor = new float[sampleSize];

		gyro.reset();
		// fetch a sample
		while(Button.ESCAPE.isUp()){
			sample_provider.fetchSample(sample, 0);
			colorSensor.fetchSample(sampleColor, 0);
			LCD.drawString("ID : " + sampleColor[0], 0, 3);
			Delay.msDelay(10);
		}
		//close the sensor
		gyro.close();
		colorSensor.close();	
	}
}
