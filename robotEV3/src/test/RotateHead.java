package test;
import lejos.hardware.motor.*;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

public class RotateHead {

	public static void main(String[] args) {
	
		RotateHead.RotateHead90();

	}
	
	public static void RotateHead90() {
		
		RegulatedMotor m = new EV3MediumRegulatedMotor(MotorPort.A);
		
		m.rotate(90);
		m.rotate(-180);
		m.rotate(90);
		
		m.close();
	}
	

}
