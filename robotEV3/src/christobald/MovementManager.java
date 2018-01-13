package christobald;

import lejos.hardware.motor.NXTRegulatedMotor;

public class MovementManager {
	private NXTRegulatedMotor MotorLeft;
	private NXTRegulatedMotor MotorRight;
	
	public enum Direction {
		  LEFT,
		  RIGHT
	}
	
	//private final static int rotateOffset = 0;
	private final static int wheelDegreeFor90 = 270;
	
	public MovementManager(NXTRegulatedMotor left, NXTRegulatedMotor right) {
		this.MotorLeft = left;
		this.MotorRight = right;
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
		if(direction == Direction.LEFT)
			rotate(-90);
		else
			rotate(90);
	}
	
	public void rotate(double degree){
		int wheelRotateDegree = (int)(wheelDegreeFor90 * degree / 90);
		if(degree < 0)
			MotorRight.rotate(- wheelRotateDegree);
		else
			MotorLeft.rotate(wheelRotateDegree);
	}
}
