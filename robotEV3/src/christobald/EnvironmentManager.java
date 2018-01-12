package christobald;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EnvironmentManager {
	public final int moustacheBreakPoint = 2;
	public final int tailBreakPoint = 2;
	
	public enum HeadDirection {
		  LEFT(-90),
		  RIGHT(90),
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
	
	EV3UltrasonicSensor distanceSensor;
	EV3TouchSensor pressedSensor;
	float[] distanceSample, pressedSample;
	NXTRegulatedMotor neckMotor;
	
 	public EnvironmentManager(String DistanceSensorPort, NXTRegulatedMotor a, String PressedSensorPort) {
		Port portDistance = LocalEV3.get().getPort(DistanceSensorPort);
		distanceSensor = new EV3UltrasonicSensor(portDistance);
		SampleProvider distanceProvider = distanceSensor.getDistanceMode();
		distanceSample = new float[distanceProvider.sampleSize()];
		
		headPosition = HeadDirection.FRONT;
		
		neckMotor = a;
		
		Port portPressed = LocalEV3.get().getPort(PressedSensorPort);
		pressedSensor = new EV3TouchSensor(portPressed);
		SampleProvider pressedProvider = distanceSensor.getDistanceMode();
		pressedSample = new float[pressedProvider.sampleSize()];
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
		return (float) 0.2;
	}
	public float getTailValue() {
		return (float) 0.2;
	}
	public boolean isMoustachePressed() {
		boolean isPressed = false;
		pressedSensor.fetchSample(pressedSample, 0);
		if(pressedSample[0] >= 1)
			isPressed = true;
		
		return isPressed;
	}
	public boolean isTailPressed() {
		return false;
	}
}
