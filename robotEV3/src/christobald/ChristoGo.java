package christobald;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;

public class ChristoGo {
	public final static float MIN_FRONT_DISTANCE = (float)0.15;
	public final static float MAX_FRONT_DISTANCE = (float)0.20;
	
	public static void main(String[] args) {
		MovementManager MM = new MovementManager(Motor.B, Motor.D);
		EnvironmentManager EM = new EnvironmentManager("S2", Motor.A, "S3");
		
		BlockIO.displayMessage("Yo, I'm Christobald");
		BlockIO.waitUntilPress();
		
		/*
		float frontDistance;
		while(Button.ESCAPE.isUp()) {
			frontDistance = EM.getSensorDistance();
			BlockIO.displayMessage("Distance : " + frontDistance + " m");
			if(frontDistance >= ChristoGo.MIN_FRONT_DISTANCE) {
				MM.forward();
			}else {
				MM.stop();
				MM.rotate(MovementManager.Direction.LEFT);
			}
		}*/
		
		while(Button.ESCAPE.isUp()) {
			float distance = EM.getDistanceOn(EnvironmentManager.HeadDirection.RIGHT);
			if(distance <= MAX_FRONT_DISTANCE && distance >= MIN_FRONT_DISTANCE)
			{
				MM.forward();
			}
			else if(distance < MIN_FRONT_DISTANCE)
			{
				MM.rotate(-20);
				MM.forward();
			}
			else if(distance > MAX_FRONT_DISTANCE)
			{
				MM.rotate(20);
				MM.forward();
			}
			else if(EM.isMoustachePressed())
			{
				MM.backward();
				MM.backward();
				MM.backward();
				MM.rotate(MovementManager.Direction.RIGHT);
			}
		}
	}
}
