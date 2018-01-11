package test;

import lejos.hardware.motor.Motor;
import lejos.utility.Delay;

public class Moves {
	
	//DifferentialPilot pilot;
	
	public Moves() {
		
		moveForward();
		//this.pilot = new DifferentialPilot(1.5f, 6, Motor.A, Motor.B);
	}
	
	public void moveForward() {
		
		Motor.D.forward();
		Motor.B.forward();
		Delay.msDelay(5000);
		Motor.D.stop();
		Motor.B.stop();
	}
	
	public void travelAndRotate() {
		
		//pilot.travel(12);
		//pilot.rotate(90);
	}

	public static void main(String[] args) {
		
		new Moves();
	}
}
 