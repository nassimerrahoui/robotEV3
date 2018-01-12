package christobald;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;

public class ChristoGo {
	public final static float MIN_WALL_DISTANCE = (float)0.15;
	public final static float MAX_WALL_DISTANCE = (float)0.20;
	public final static int CORRECTION_ANGLE = 20;
	
	
	public static void main(String[] args) {
		MovementManager MM = new MovementManager(Motor.B, Motor.D);
		EnvironmentManager EM = new EnvironmentManager("S2", Motor.A, "S3");
		
		BlockIO.displayMessage("Yo, I'm Christobald");
		BlockIO.waitUntilPress();
		
		while(Button.ESCAPE.isUp()) {
			float distance = EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT);
			if(distance <= MAX_WALL_DISTANCE && distance >= MIN_WALL_DISTANCE)
			{
				MM.forward();
			}
			else if(distance < MIN_WALL_DISTANCE)
			{
				MM.rotate(-1 * CORRECTION_ANGLE);
				MM.forward();
			}
			else
			{
				MM.rotate(CORRECTION_ANGLE);
				MM.forward();
			}
			
			if(EM.isMoustachePressed())
			{
				MM.backward();
				MM.rotate(MovementManager.Direction.RIGHT);
			}
		}
	}
}
