package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Paco
 *
 */
public class ConsoleUtils {

	/**
	 * Get the output of a dos command
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String dosOutputCommand(String command) throws IOException, InterruptedException{
		String res = "";
		Process p=Runtime.getRuntime().exec(command); 
        p.waitFor(); 
        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream())); 
        String line; 
        while((line = reader.readLine()) != null) { 
        	res += line;
        } 
        return res;
	}
	
	/**
	 * Get the output of a bash command
	 * @param command
	 * @return
	 */
	public String outputBash(String command){
		String result = null;
		try {
		    Runtime r = Runtime.getRuntime();              
		    String[] cmd = {
	                "/bin/sh",
	                "-c",
	                command
	            };		    
		    Process p = r.exec(cmd);

		    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String inputLine;
		    while ((inputLine = in.readLine()) != null) {
		        //System.out.println(inputLine);
		    	result += "\n";
		        result += inputLine;
		    }
		    in.close();
		} 
		catch (IOException e) {
		    System.out.println(e);
		}
		return result;
	}

}//end of class
