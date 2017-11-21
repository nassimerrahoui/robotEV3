package test;

import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class Robot {
	
	DifferentialPilot pilot;
	
	public Robot() {
		
		moveForward();
		this.pilot = new DifferentialPilot(1.5f, 6, Motor.A, Motor.B);
		for (int i = 0; i < 3; i++) {
			/*travelAndRotate();*/
			moveForward();
		}
	}
	
	public void moveForward() {
		
		Motor.D.forward();
		Motor.B.forward();
		Delay.msDelay(5000);
		Motor.D.stop();
		Motor.B.stop();
	}
	
	public void travelAndRotate() {
		
		pilot.travel(12);
		pilot.rotate(90);
	}

	public static void main(String[] args) {
		
		new Robot();
	}
}
