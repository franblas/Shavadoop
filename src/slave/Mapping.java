package slave;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import master.Master;

import ssh.CommandSSH;
import utils.Configuration;

/**
 * 
 * @author Paco
 *
 */
public class Mapping extends Thread{
	
	public String um = "";
	public String nameMachine = ""; 
	//public ArrayList<String> wordsUnique = new ArrayList<String>();
	
	public String runLine = "";
	public int runNb = 0;
	
	public Master master;
	
	/**
	 * Mapping Constructor
	 * @param runNb
	 * @param nameMachine
	 * @param line
	 */
	public Mapping(int runNb, String nameMachine, String line, Master master) {
		this.um = "UM"+runNb;
		this.runNb = runNb;
		this.nameMachine = nameMachine;
		this.runLine = line;
		this.master = master;
	}

	/**
	 * Mapping Runnable
	 */
	public void run(){
		String plop = "";
		try {
			plop = createExecJava(runLine, nameMachine, runNb);
			execMapping(um, nameMachine, plop, master);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compile and execute java file with SSH
	 * @param umx
	 * @param ip
	 * @param com
	 */
	public void execMapping(String umx, String ip, String com, Master master){
		String mod = "chmod 777 "+new Configuration().slavePath+umx+".java;";
		String compile = "javac "+new Configuration().slavePath+umx+".java;";
		String exec = "java -classpath "+new Configuration().slavePath+" "+umx+";";
		String command = com+mod+compile+exec;
		String top = new CommandSSH().outputCommandSSH(new Configuration().sshUser, ip, new Configuration().sshKey, command);
		ArrayList<String> wordsUnique = new ArrayList<String>();
		wordsUnique.add(top);
		master.dictionary2.put(nameMachine, wordsUnique);
		//wordsUnique.add(top);
		if(new Configuration().Debug){
			System.out.println(top);
		}
	}
	
	/**
	 * Create java file to execute
	 * @param line
	 * @param ip
	 * @param nb
	 * @return
	 * @throws IOException
	 */
	public String createExecJava(String line, String ip, int nb) throws IOException{	
		String test = addEcho("src/slave/UMx.java",nb);
		test = test.replace("ipMaster = \\\"\\\"", "ipMaster = \\\""+ip+"\\\"");
		test = test.replace("line = \\\"\\\"", "line = \\\""+line+"\\\"");
		test = test.replace("nb = 0", "nb = "+nb+"");
		test = test.replace("UMx", "UM"+nb+"");
		test = test.replace("slavePath = \\\"\\\"", "slavePath = \\\""+new Configuration().slavePath+"\\\"");
		return test;
	}
	
	/**
	 * Parse 'echo' and ';' for the ssh command 
	 * @param filepath
	 * @param nb
	 * @return
	 * @throws IOException
	 */
	public String addEcho(String filepath, int nb) throws IOException{
		String res = null;
	    BufferedReader br = new BufferedReader(new FileReader(filepath));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	        	line = line.replace("package slave;", "");
	        	line = line.replaceAll("\\\\n", "\\\\\\\\n");
	        	line = line.replace("\\\\r", "\\\\\\\\r");
	        	line = line.replace("\\s", "\\\\\\s");
	        	line = line.replace("\"", "\\\"");
	            sb.append("echo \""+line+"\" >> "+new Configuration().slavePath+"UM"+nb+".java;");
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        res = sb.toString();
	    } finally {
	        br.close();
	    }
	    return res;
	}

	/**
	 * 
	 * @return
	 */
	public String getUm() {
		return um;
	}

	/**
	 * 
	 * @return
	 */
	public String getNameMachine() {
		return nameMachine;
	}

}//end of class
