package christobald;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class ChristoGoy {
	public final static float MIN_WALL_DISTANCE = (float)0.10;
	public final static float MAX_WALL_DISTANCE = (float)0.15;
	public final static int CORRECTION_ANGLE = 7;
	public static MovementManager MM = new MovementManager(Motor.B, Motor.D);
	public static EnvironmentManager EM = new EnvironmentManager("S2", Motor.A, "S3");
	
	public enum ChristoMode {
		  WALL_FINDING,
		  WALL_FOLLOWING,
		  FORWARD_MOVING,
		  QUARTER_TURN_AVOIDANCE,
		  LEFT_CORRECTION,
		  RIGHT_CORRECTION
	}
	
	public static void forwardCheck() throws MoustachePressException
	{
		if(EM.isMoustachePressed()){
			//BlockIO.setLedColor(100);
			throw new MoustachePressException();
		}
		MM.forward(); 
	}
	
	public static void main(String[] args) {
		
		BlockIO.displayMessage("Yo, I'm Chri 6.6");
		BlockIO.waitUntilPress();
		ChristoMode mode = ChristoMode.WALL_FINDING;
		
		while(Button.ESCAPE.isUp()) {
			switch (mode) {
				case WALL_FINDING:
					MM.stop();
					float distanceLeft = EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT);
					float distanceRight = EM.getDistanceOn(EnvironmentManager.HeadDirection.RIGHT);
					if(distanceLeft > 0.60) {
						if(distanceRight < distanceLeft)
							MM.rotate(60);
						else
							MM.rotate(-60);
						mode = ChristoMode.FORWARD_MOVING;
					} else {
						mode = ChristoMode.WALL_FOLLOWING;
					}
					EM.look(EnvironmentManager.HeadDirection.LEFT);
					break;
				case WALL_FOLLOWING:
					float distance = EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT);
					BlockIO.displayMessage("Distance : " + distance);
					if(distance < MIN_WALL_DISTANCE)
					{
						MM.rotate(-1 *CORRECTION_ANGLE);
					}
					else if(distance >= MAX_WALL_DISTANCE)
					{
						MM.forward();
						if(distance < 0.50) {
							MM.rotate(CORRECTION_ANGLE);
						}
						else {
							MM.forward();
							Delay.msDelay(800);
							MM.stop();
							MM.rotate(-90);
						}
					}
					MM.forward(); 
					if(EM.isMoustachePressed())
						mode = ChristoMode.QUARTER_TURN_AVOIDANCE;
					break;
				case FORWARD_MOVING:
					MM.forward();
					if(EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT) < 0.3)
						mode = ChristoMode.WALL_FOLLOWING;
					if(EM.isMoustachePressed())
						mode = ChristoMode.QUARTER_TURN_AVOIDANCE;
					break;
				case QUARTER_TURN_AVOIDANCE:
					MM.stop();
					MM.backward();
					MM.rotate(60);
					MM.stop();
					mode = ChristoMode.WALL_FOLLOWING;
					break;
				default:
					break;
			}
			/**try {
				
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
			}**/
		}
		EM.look(EnvironmentManager.HeadDirection.FRONT); 
	}
}
