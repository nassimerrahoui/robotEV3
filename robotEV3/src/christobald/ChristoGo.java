package christobald;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;

public class ChristoGo {
	public final static float MIN_WALL_DISTANCE = (float)0.15;
	public final static float MAX_WALL_DISTANCE = (float)0.20;
	public final static int CORRECTION_ANGLE = 10;
	public static MovementManager MM = new MovementManager(Motor.B, Motor.D);
	public static EnvironmentManager EM = new EnvironmentManager("S2", Motor.A, "S3");
	
	public static void forwardCheck() throws MoustachePressException
	{
		if(EM.isMoustachePressed()){
			throw new MoustachePressException();
		}
		MM.forward();
	}
	
	public static void main(String[] args) {
		
		BlockIO.displayMessage("Yo, I'm Christobald");
		BlockIO.waitUntilPress();
		
		while(Button.ESCAPE.isUp()) {
			try {
				float distance = EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT);
				if(distance < MIN_WALL_DISTANCE)
				{
					MM.rotate(-1 * CORRECTION_ANGLE);
				}
				else if(distance >= MAX_WALL_DISTANCE)
				{
					MM.rotate(CORRECTION_ANGLE);
				}
				forwardCheck();
			}
			
			catch(MoustachePressException e)
			{
				MM.backward();
				MM.rotate(MovementManager.Direction.RIGHT);
			}
		}
	}
}
