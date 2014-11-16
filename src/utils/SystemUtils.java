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
	 * @param min
	 * @param max
	 * @return
	 */
	public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	/**
	 * Detect the host OS
	 * @return
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
