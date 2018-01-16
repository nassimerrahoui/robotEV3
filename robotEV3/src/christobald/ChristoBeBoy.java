package christobald;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class ChristoBeBoy {
	public final static float MIN_WALL_DISTANCE = 0.10f;
	public final static float MAX_WALL_DISTANCE = 0.15f;
	public final static float NO_WALL_DISTANCE = 0.5f;
	public final static int CORRECTION_ANGLE = 10;
	public final static int DELAY_BEFORE_ROTATION = 400;
	public static MovementManager MM = MovementManager.getInstance();
	public static EnvironmentManager EM = EnvironmentManager.getInstance();
	public static ChristoMode mode;
	 
	public enum ChristoMode {
		  WALL_FINDING,
		  WALL_FOLLOWING,
		  FORWARD_MOVING,
		  QUARTER_TURN_AVOIDANCE,
		  FINISH_LINE,
		  BACK_ESCAPE,
		  MAD_LEFT_ESCAPE,
		  MAD_RIGHT_ESCAPE,
		  MAD_FRONT_ESCAPE
	}
	
	public static void main(String[] args) {
		
		BlockIO.displayMessage("Yo, I'm Chris 38");
		BlockIO.waitUntilPress();
		switchMode(ChristoMode.WALL_FINDING);
		
		while(Button.ESCAPE.isUp() || EM.isRedColor()) {
			switch (mode) {
				case WALL_FINDING:
					MM.stop();
					float distanceLeft = EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT);
					float distanceRight = EM.getDistanceOn(EnvironmentManager.HeadDirection.RIGHT);
					if(distanceLeft > NO_WALL_DISTANCE) {
						MM.rotate(distanceRight < distanceLeft 
								? MovementManager.Direction.RIGHT 
								: MovementManager.Direction.LEFT);
						switchMode(ChristoMode.FORWARD_MOVING);
					} else {
						switchMode(ChristoMode.WALL_FOLLOWING);
					}
					EM.look(EnvironmentManager.HeadDirection.LEFT);
					break;
				case WALL_FOLLOWING:
					float distance = EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT);
					BlockIO.displayMessage("Distance : " + distance);
					if(distance < MIN_WALL_DISTANCE)
					{
						MM.rotate(-CORRECTION_ANGLE);
					}
					else if(distance >= MAX_WALL_DISTANCE && distance <= NO_WALL_DISTANCE)
					{
						MM.rotate(CORRECTION_ANGLE);
					}
					else if(distance > NO_WALL_DISTANCE){
						Delay.msDelay(DELAY_BEFORE_ROTATION);
						MM.stop();
						MM.rotate(MovementManager.Direction.LEFT);
					}
					MM.forward();
					if(EM.isMoustachePressed())
						switchMode(ChristoMode.QUARTER_TURN_AVOIDANCE);
					break;
				case FORWARD_MOVING:
					MM.forward();
					if(EM.getDistanceOn(EnvironmentManager.HeadDirection.LEFT) < NO_WALL_DISTANCE)
						switchMode(ChristoMode.WALL_FOLLOWING);
					if(EM.isMoustachePressed())
						switchMode(ChristoMode.QUARTER_TURN_AVOIDANCE);
					break;
				case QUARTER_TURN_AVOIDANCE:
					MM.stop();
					MM.backward();
					Delay.msDelay(100);
					MM.rotate(MovementManager.Direction.RIGHT);
					MM.stop();
					switchMode(ChristoMode.WALL_FOLLOWING);
					break;
				case MAD_LEFT_ESCAPE:
					MM.rotate(MovementManager.Direction.LEFT);
					switchMode(ChristoMode.MAD_FRONT_ESCAPE);
				case MAD_RIGHT_ESCAPE:
					MM.rotate(MovementManager.Direction.RIGHT);
					switchMode(ChristoMode.MAD_FRONT_ESCAPE);
				case MAD_FRONT_ESCAPE:
					MM.forward();
					if(EM.isMoustachePressed())
						switchMode(ChristoMode.QUARTER_TURN_AVOIDANCE);
				default:
					break;
			}
			
			if(MM.isOverRotating()) {
				if(MM.isLeftOverRotating())
					switchMode(ChristoMode.MAD_RIGHT_ESCAPE);
				else
					switchMode(ChristoMode.MAD_LEFT_ESCAPE);
				MM.resetGyroCount();
			}
			// FINALLY
			if(EM.isOverRed())
				break;
		}
		EM.look(EnvironmentManager.HeadDirection.FRONT);
	}
	
	public static void switchMode(ChristoMode m) {
		mode = m;
		LCD.drawString("mode: "+m.toString(), 0, 3);
	}
}
