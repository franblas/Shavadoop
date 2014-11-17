package utils;

import java.util.Random;

/**
 * 
 * @author Paco
 *
 */
public class SystemUtils {

	/**
	 * Generate an Integer randomly between min and max (included)
	 * @param min The min value
	 * @param max The max value
	 * @return a random integer
	 */
	public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	/**
	 * Detect the host OS
	 * @return the OS family name
	 */
	public String detectOS(){
		String det = System.getProperty("os.name");
		if(det.contains("Windows")){
			return "Windows";
		}
		if(det.contains("Linux")){
			return "Linux";
		}
		else{
			return det;
		}
	}

}//end of class
