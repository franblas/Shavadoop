package slave;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ssh.CommandSSH;
import utils.Configuration;

/**
 * 
 * @author Paco
 *
 */
public class Reducing extends Thread{
	
	public String re = "";
	public String runHost = "";
	public int runNb = 0;
	
	/**
	 * Reducing Constructor
	 * @param host
	 * @param nb
	 */
	public Reducing(String host, int nb){
		this.runHost = host;
		this.runNb = nb;
		this.re = "RE"+nb;
	}
	
	/**
	 * Reducing Runnable
	 */
	public void run(){
		try {
			String plip = createExecJava(runHost, runNb);
			execReducing(re, runHost, plip);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compile and execute java file with SSH
	 * @param rex
	 * @param ip
	 * @param com
	 */
	public void execReducing(String rex, String ip, String com){
		String mod = "chmod 777 "+new Configuration().slavePath+rex+".java;";
		String compile = "javac "+new Configuration().slavePath+rex+".java;";
		String exec = "java -classpath "+new Configuration().slavePath+" "+rex+";";
		String command = com+mod+compile+exec;
		String top = new CommandSSH().outputCommandSSH(new Configuration().sshUser, ip, new Configuration().sshKey, command);
		if(new Configuration().Debug){
			System.out.println(top);
		}
	}
	
	/**
	 * Create java file to execute
	 * @param ip
	 * @param nb
	 * @return
	 * @throws IOException
	 */
	public String createExecJava(String ip, int nb) throws IOException{	
		String test = addEcho("src/slave/REx.java",nb);
		test = test.replace("ipMaster = \\\"\\\"", "ipMaster = \\\""+ip+"\\\"");
		test = test.replace("nb = 0", "nb = "+nb+"");
		test = test.replace("REx", "RE"+nb+"");
		test = test.replace("slavePath = \\\"\\\"", "slavePath = \\\""+new Configuration().slavePath+"\\\"");
		//System.out.println(test);
		//sh = "SH"+nb;
		//nameMachine = ip;
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
	            sb.append("echo \""+line+"\" >> "+new Configuration().slavePath+"RE"+nb+".java;");
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        res = sb.toString();
	    } finally {
	        br.close();
	    }
	    return res;
	}

}//end of class
