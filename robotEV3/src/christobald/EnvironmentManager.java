package christobald;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EnvironmentManager {
	public final int moustacheBreakPoint = 1;
	public final int tailBreakPoint = 2;
	private float[] wallDistance;
	
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
	
 	public EnvironmentManager(String DistanceSensorPort, NXTRegulatedMotor a, String moustacheSensorPort) {
		Port portDistance = LocalEV3.get().getPort(DistanceSensorPort);
		distanceSensor = new EV3UltrasonicSensor(portDistance);
		SampleProvider distanceProvider = distanceSensor.getDistanceMode();
		distanceSample = new float[distanceProvider.sampleSize()];
		
		headPosition = HeadDirection.FRONT;
		wallDistance = new float[100];
		this.resetWallDistance();
		
		neckMotor = a;
		
		Port portMoustache = LocalEV3.get().getPort(moustacheSensorPort);
		moustacheSensor = new EV3TouchSensor(portMoustache);
		SampleProvider moustacheProvider = moustacheSensor.getTouchMode();
		moustacheSample = new float[moustacheProvider.sampleSize()];
	}
	public void resetWallDistance() {
		for(int i = 0; i < wallDistance.length; i++) {
			wallDistance[i] = 0;
		}
	}
 	public int getWallAngleAndReset() {
 		float sum = 0;
 		int n = 0;
 		for(int i = 0; i < wallDistance.length - 1; i++) {
 			if(wallDistance[i] !=  0 && wallDistance[i+1] != 0) {
 				sum += wallDistance[i] - wallDistance[i+1];
 				n++;
 			}
 		}
 		float coefAverage = sum / n;
 		resetWallDistance();
 		return Math.round( Math.atan((double)coefAverage));
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
}
