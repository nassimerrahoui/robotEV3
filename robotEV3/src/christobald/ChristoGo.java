package christobald;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;

public class ChristoGo {
	public final static float MIN_FRONT_DISTANCE = (float)0.15;
	
	public static void main(String[] args) {
		MovementManager MM = new MovementManager(Motor.B, Motor.D);
		EnvironmentManager EM = new EnvironmentManager("S2");
		
		BlockIO.displayMessage("Yo, I'm Christo");
		BlockIO.waitUntilPress();
		
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
		}
	}
}
