package christobald;

import java.util.ArrayList;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EnvironmentManager {
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
	float[] distanceSample, moustacheSample;
	ArrayList<Float> wallSample;
	SampleProvider distanceProvider, wallProvider;
	
 	public EnvironmentManager(String DistanceSensorPort, NXTRegulatedMotor a, String moustacheSensorPort) {
		Port portDistance = LocalEV3.get().getPort(DistanceSensorPort);
		distanceSensor = new EV3UltrasonicSensor(portDistance);
		distanceProvider = distanceSensor.getDistanceMode();
		
		distanceSample = new float[distanceProvider.sampleSize()];
		wallSample = new ArrayList<Float>();
		
		headPosition = HeadDirection.FRONT;
		neckMotor = a;
		
		Port portMoustache = LocalEV3.get().getPort(moustacheSensorPort);
		moustacheSensor = new EV3TouchSensor(portMoustache);
		SampleProvider moustacheProvider = moustacheSensor.getTouchMode();
		moustacheSample = new float[moustacheProvider.sampleSize()];
	}

 	public double getWallAngleAndReset() {
 		float sum = 0;
 		int n = wallSample.size() - 1;
 		
 		for(int i = 0; i < n ; i++) {
 			sum += wallSample.get(i) - wallSample.get(i+1);			
 		}
 		
 		wallSample.clear();
 		
 		double num = sum * 1000;
 		double t = num / n;
 		BlockIO.displayMessage(t + " " + n); ///////////////////PROBLEM
 		double angle = Math.atan(t);
 		return - Math.toDegrees(angle);
 	}
 	
 	public void addSample() {
 		float t = getSensorDistance();
 		if(t != 0.0f)
 			wallSample.add(t);
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
		return getMoustacheValue() == 1.0;
	}
	public boolean isTailPressed() {
		return false;
	}
}
