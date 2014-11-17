package slave;

import ssh.CommandSSH;
import utils.Configuration;

/**
 * 
 * @author Paco
 *
 */
public class Merging extends Thread{

	public String runHost = "";
	public int runNb = 0;
	
	/**
	 * Merging Constructor
	 * @param runHost
	 * @param runNb
	 */
	public Merging(String runHost, int runNb) {
		this.runHost = runHost;
		this.runNb = runNb;
	}
	
	/**
	 * Merging Runnable
	 */
	public void run(){
		execMerging("RE"+runNb, runHost);
	}
	
	/**
	 * Write the final result into a file
	 * @param rex The file to execute
	 * @param ip The host to connect
	 */
	public synchronized void execMerging(String rex, String ip){
		String command = "cat "+new Configuration().slavePath+rex+" >> output";
		String top = new CommandSSH().outputCommandSSH(new Configuration().sshUser, ip, new Configuration().sshKey, command);
		if(new Configuration().Debug){
			System.out.println(top);
		}
	}

}//end of class
