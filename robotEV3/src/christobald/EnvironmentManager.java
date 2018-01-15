package christobald;

import java.util.LinkedList;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class EnvironmentManager {
	private static EnvironmentManager instance = null;
	
	public final int moustacheBreakPoint = 1;
	public final int tailBreakPoint = 2;
	private final static String DISTANCE_SENSOR_PORT = "S2";
	private final static String MOUSTACHE_SENSOR_PORT = "S3";
	private final static String COLOR_SENSOR_PORT = "S1";
	
	private HeadDirection headPosition;
	private LinkedList<Float> distanceBuffer = new LinkedList<Float>();
	private final static float DISTANCE_TOLERANCE = 0.01f;
	
	private LinkedList<Float> gyroBuffer = new LinkedList<Float>();
	private final static float ANGLE_TOLERANCE = 5.0f;
	
	private final static float REPRESENTATIVE_SAMPLE = 95;
	private final static int MAX_BUFFER_SIZE = 100;
	
	NXTRegulatedMotor neckMotor = Motor.A;
	
	EV3UltrasonicSensor distanceSensor;
	EV3TouchSensor moustacheSensor;
	EV3ColorSensor colorSensor;
	float[] distanceSample, moustacheSample, colorSample;
	
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
	
	public static EnvironmentManager getInstance() {
		if(instance == null)
			instance = new EnvironmentManager();
		return instance;	
	}
	
 	private EnvironmentManager() {
 		Port portDistance = LocalEV3.get().getPort(DISTANCE_SENSOR_PORT);
		distanceSensor = new EV3UltrasonicSensor(portDistance);
		SampleProvider distanceProvider = distanceSensor.getDistanceMode();
		distanceSample = new float[distanceProvider.sampleSize()];
		
		headPosition = HeadDirection.FRONT;
		
		Port portMoustache = LocalEV3.get().getPort(MOUSTACHE_SENSOR_PORT);
		moustacheSensor = new EV3TouchSensor(portMoustache);
		SampleProvider moustacheProvider = moustacheSensor.getTouchMode();
		moustacheSample = new float[moustacheProvider.sampleSize()];
		
		Port colorPort = LocalEV3.get().getPort(COLOR_SENSOR_PORT);            
		colorSensor = new EV3ColorSensor(colorPort);
		SampleProvider colorIdSensor = colorSensor.getColorIDMode();
		int sampleSize = colorIdSensor.sampleSize();            
		colorSample = new float[sampleSize];
	}
 	
 	/*public void listenToEnv() {
 		float d = this.getSensorDistance();
 		if(d != 0.0f) {
 			distanceBuffer.add(new Float(d));
 			if(distanceBuffer.size() > MAX_BUFFER_SIZE)
 				distanceBuffer.removeFirst();
 		}
 		float a = this.getGyroAngle();
 		if(a != 0.0f) {
 			gyroBuffer.add(new Float(a));
 			if(gyroBuffer.size() > MAX_BUFFER_SIZE)
 				gyroBuffer.removeFirst();
 		}
 	}
 	
 	public boolean isMotionless() {
 		boolean motionless = true;
 		for(Float f : distanceBuffer) {
 			if()
 		}
 		return true;
 	}*/
 	
 	public boolean isOverRed() {
 		return isRedColor();
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
	public boolean isMoustachePressed() {
		BlockIO.displayMessage("Moustache : " + getMoustacheValue());
		return getMoustacheValue() >= moustacheBreakPoint;
	}
	
	public float getColor() {
		colorSensor.fetchSample(colorSample, 0);
		return colorSample[0];
	}
	
	public boolean isRedColor() {
		return getColor() == 0.00;	
	}
}
