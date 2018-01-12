package christobald;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class BlockIO {
	public static void displayMessage(String m) {
		LCD.drawString(m, 0, 1);
	}
	public static void displayCompass(){
		
	}
	public static void waitUntilPress() {
		Button.waitForAnyPress();
	}
}
