package test;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class UltrasonicSensor  {
   
   public static void main(String[] args) {
	   
       EV3UltrasonicSensor capteur = new EV3UltrasonicSensor(SensorPort.S4);
       
       LCD.clear();
       LCD.drawString("Distance :    cm", 0, 3);
       /*
       while (capteur.getDistanceMode() <= SimplePro)
       {
    	   Motor.D.forward();
    	   Motor.B.forward();
    	   Delay.msDelay(5000);
    	   Motor.D.stop();
    	   Motor.B.stop();
       }
       */
   }
}
