package christobald;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
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
	float[] distanceSample;
	EV3MediumRegulatedMotor costMotor;
	
 	public EnvironmentManager(String DistanceSensorPort, String CostMotorPort) {
		Port portDistance = LocalEV3.get().getPort(DistanceSensorPort);
		distanceSensor = new EV3UltrasonicSensor(portDistance);
		SampleProvider distanceProvider = distanceSensor.getDistanceMode();
		distanceSample = new float[distanceProvider.sampleSize()];
		
		headPosition = HeadDirection.FRONT;
		
		Port portCost = LocalEV3.get().getPort(DistanceSensorPort);
		costMotor = new EV3MediumRegulatedMotor(portCost);
	}
	
	public float getSensorDistance() {
		distanceSensor.fetchSample(distanceSample, 0);
		return distanceSample[0];
	}
	
	public void look(HeadDirection direction) {
		int rotateAngle = direction.getAngle() - this.headPosition.getAngle();
		costMotor.rotate(rotateAngle);
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
		return false;
	}
	public boolean isTailPressed() {
		return false;
	}
}
