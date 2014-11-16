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
public class Shuffling extends Thread{

	public String sh = "";
	public String nameMachine = ""; 
	
	public String runLine = "";
	public int runNb = 0;
	public int runUmNb = 0;

	/**
	 * Suffling Constructor
	 * @param line
	 * @param nameMachine
	 * @param runNb
	 * @param runUmNb
	 */
	public Shuffling(String line, String nameMachine, int runNb, int runUmNb){
		this.sh = "SH"+runNb;
		this.runNb = runNb;
		this.nameMachine = nameMachine;
		this.runLine = line;
		this.runUmNb = runUmNb;
	}
	
	/**
	 * Shuffling Runnable
	 */
	public void run(){
		try {
			String plop = createExecJava(runLine, nameMachine, runNb, runUmNb);
			execShuffling(sh, nameMachine, plop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compile and execute java file with SSH
	 * @param shx
	 * @param ip
	 * @param com
	 */
	public void execShuffling(String shx, String ip, String com){
		String mod = "chmod 777 "+new Configuration().slavePath+shx+".java;";
		String compile = "javac "+new Configuration().slavePath+shx+".java;";
		String exec = "java -classpath "+new Configuration().slavePath+" "+shx+";";
		String command = com+mod+compile+exec;
		String top = new CommandSSH().outputCommandSSH(new Configuration().sshUser, ip, new Configuration().sshKey, command);
		if(new Configuration().Debug){
			System.out.println(top);
		}
	}
	
	/**
	 * Create java file to execute
	 * @param word
	 * @param ip
	 * @param nb
	 * @param nbum
	 * @return
	 * @throws IOException
	 */
	public String createExecJava(String word, String ip, int nb, int nbum) throws IOException{	
		String test = addEcho("src/slave/SHx.java",nb);
		test = test.replace("ipMaster = \\\"\\\"", "ipMaster = \\\""+ip+"\\\"");
		test = test.replace("word = \\\"\\\"", "word = \\\""+word+"\\\"");
		test = test.replace("nb = 0", "nb = "+nb+"");
		test = test.replace("nbum = 0", "nbum = "+nbum+"");
		test = test.replace("SHx", "SH"+nb+"");
		test = test.replace("slavePath = \\\"\\\"", "slavePath = \\\""+new Configuration().slavePath+"\\\"");
		//System.out.println(test);
		sh = "SH"+nb;
		nameMachine = ip;
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
	            sb.append("echo \""+line+"\" >> "+new Configuration().slavePath+"SH"+nb+".java;");
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
	public String getSh() {
		return sh;
	}

	/**
	 * 
	 * @return
	 */
	public String getNameMachine() {
		return nameMachine;
	}
	
	
	
}//end of class
