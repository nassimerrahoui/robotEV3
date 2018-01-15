package christobald;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;

public class ChristoGoy {
	public final static float MIN_WALL_DISTANCE = (float)0.10;
	public final static float MAX_WALL_DISTANCE = (float)0.15;
	public final static int CORRECTION_ANGLE = 7;
	public static MovementManager MM = new MovementManager(Motor.B, Motor.D);
	public static EnvironmentManager EM = new EnvironmentManager("S2", Motor.A, "S3");
	
	public static void forwardCheck() throws MoustachePressException
	{
		if(EM.isMoustachePressed()){
			//BlockIO.setLedColor(100);
			throw new MoustachePressException();
		}
		MM.forward(); 
	}
	
	public static void main(String[] args) {
		
		BlockIO.displayMessage("Yo, I'm Chri 5.4");
		BlockIO.waitUntilPress();
		
		while(Button.ESCAPE.isUp()) {
			try {
				
				float distance = EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT);
				BlockIO.displayMessage("Distance : " + distance);
				if(distance < MIN_WALL_DISTANCE)
				{
					MM.rotate(-1 *CORRECTION_ANGLE);
				}
				else if(distance >= MAX_WALL_DISTANCE)
				{
					if(distance < 0.60)
						MM.rotate(CORRECTION_ANGLE);
					else
						MM.stop();
						MM.forward();
				}
				forwardCheck();
			}
			
			catch(MoustachePressException e)
			{
				MM.stop();
				MM.backward();
				MM.rotate(60);
			}
		}
		EM.look(EnvironmentManager.HeadDirection.FRONT); 
	}
}
