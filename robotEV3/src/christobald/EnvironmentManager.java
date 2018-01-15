package christobald;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EnvironmentManager {
	public final int moustacheBreakPoint = 1;
	public final int tailBreakPoint = 2;
	
	
	public enum HeadDirection {
		  LEFT(90),
		  RIGHT(-90),
		  FRONT(0);
		  private int angle = 0;
		  HeadDirection(int angle){
			  this.angle = angle;
		  }
		  public int getAngle(){
			  return this.angle;
		  }
	}
	
	private HeadDirection headPosition;
	
	NXTRegulatedMotor neckMotor;
	
	EV3UltrasonicSensor distanceSensor;
	EV3TouchSensor moustacheSensor;
	EV3ColorSensor colorSensor;
	float[] distanceSample, moustacheSample, colorSample;
	
 	public EnvironmentManager(String DistanceSensorPort, NXTRegulatedMotor a, String moustacheSensorPort, String colorSensorPort) {
		Port portDistance = LocalEV3.get().getPort(DistanceSensorPort);
		distanceSensor = new EV3UltrasonicSensor(portDistance);
		SampleProvider distanceProvider = distanceSensor.getDistanceMode();
		distanceSample = new float[distanceProvider.sampleSize()];
		
		headPosition = HeadDirection.FRONT;
		neckMotor = a;
		
		Port portMoustache = LocalEV3.get().getPort(moustacheSensorPort);
		moustacheSensor = new EV3TouchSensor(portMoustache);
		SampleProvider moustacheProvider = moustacheSensor.getTouchMode();
		moustacheSample = new float[moustacheProvider.sampleSize()];
		
		Port colorPort = LocalEV3.get().getPort(colorSensorPort);            
		colorSensor = new EV3ColorSensor(colorPort);
		SampleProvider colorIdSensor = colorSensor.getColorIDMode();
		int sampleSize = colorIdSensor.sampleSize();            
		colorSample = new float[sampleSize];
	}
	
	public float getSensorDistance() {
		distanceSensor.fetchSample(distanceSample, 0);
		return distanceSample[0];
	}
	
	public void look(HeadDirection direction) {
		int rotateAngle = direction.getAngle() - this.headPosition.getAngle();
		neckMotor.rotate(rotateAngle);
		this.headPosition = direction;
	}
	public float getDistanceOn(HeadDirection direction) {
		if(direction != this.headPosition) 
			look(direction);
		return getSensorDistance();
	}
	
	public float getMoustacheValue() {
		moustacheSensor.fetchSample(moustacheSample, 0);
		return moustacheSample[0];
	}
	public float getTailValue() {
		return (float) 0.2;
	}
	public boolean isMoustachePressed() {
		BlockIO.displayMessage("Moustache : " + getMoustacheValue());
		return getMoustacheValue() >= moustacheBreakPoint;
	}
	public boolean isTailPressed() {
		return false;
	}
	
	public float getColor() {
		colorSensor.fetchSample(colorSample, 0);
		return colorSample[0];
	}
	
	public boolean isRedColor() {
		return getColor() == 0.00;	
	}
}
