package christobald;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class MovementManager {
	private static MovementManager instance = null;
	
	private final static NXTRegulatedMotor MotorLeft = Motor.B;
	private final static NXTRegulatedMotor MotorRight =  Motor.D;
	private final static int WHEEL_DEGREE_90 = 210;
	private final static String GYRO_SENSOR_PORT = "S4";
	private SampleProvider gyroProvider;
	private float[] gyroSamples;
	private float gyroCount = 0;
	public final static int ROTATING_TOLERANCE = 900;
	
	public enum Direction {
		  LEFT(-60),
		  RIGHT(60);
		  private int rotationAngle = 0;
		  Direction(int angle){
			  this.rotationAngle = angle;
		  }
		  public int getAngle(){
			  return this.rotationAngle;
		  }
	}
	
	public static MovementManager getInstance() {
		if(instance == null)
			instance = new MovementManager();
		return instance;	
	}
	private MovementManager() {
		Port port = LocalEV3.get().getPort(GYRO_SENSOR_PORT);
		EV3GyroSensor gyro = new EV3GyroSensor(port);
		gyro.reset();
		this.gyroProvider = gyro.getAngleMode();
		float[] gyroSamples = new float[gyroProvider.sampleSize()];
	}
	
	public void forward()
	{
		MotorLeft.forward();
		MotorRight.forward();
	}
	public void backward() 
	{
		MotorLeft.backward();
		MotorRight.backward();
	}
	
	public void stop()
	{
		MotorLeft.stop(true); 
		MotorRight.stop(true);
	}
	
	public void rotate(Direction direction) {
		rotate(direction.getAngle());
	}
	
	public void rotate(int degree){
		float degreeBefore = getGyroAngle();
		int wheelRotateDegree = Math.round(WHEEL_DEGREE_90 * degree / 90);
		if(degree < 0)
			MotorRight.rotate(-wheelRotateDegree);
		else
			MotorLeft.rotate(wheelRotateDegree);
		gyroCount += getGyroAngle() - degreeBefore;
	}
	
	public float getGyroAngle() {
		gyroProvider.fetchSample(this.gyroSamples, 0);
		return gyroSamples[0];
	}
	public boolean isOverRotating() {
		LCD.drawString("GC: "+gyroCount, 0, 6);
		return Math.abs(gyroCount) >= ROTATING_TOLERANCE;
	}
	public boolean isLeftOverRotating() {
		return gyroCount < 0;
	}
	public void resetGyroCount() {
		gyroCount = 0;
	}
}
