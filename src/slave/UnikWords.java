package slave;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import ssh.CommandSSH;
import utils.Configuration;

/**
 * 
 * @author Paco
 *
 */
public class UnikWords extends Thread{

	public ArrayList<String> wordsUnik = new ArrayList<String>();
	public String runHost = "";
	
	/**
	 * UnikWords Constructor
	 * @param host
	 */
	public UnikWords(String host){
		this.runHost = host;
	}
	
	/**
	 * UnikWords Runnable
	 */
	public void run(){
		try {
			String ecofile = createExecJava();
			execUnikWords(runHost, ecofile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compile and execute java file with SSH
	 * @param ip
	 * @param com
	 */
	public void execUnikWords(String ip, String com){
		String mod = "chmod 777 "+new Configuration().slavePath+"UWx.java;";
		String compile = "javac "+new Configuration().slavePath+"UWx.java;";
		String exec = "java -classpath "+new Configuration().slavePath+" UWx;";
		String command = com+mod+compile+exec;
		String top = new CommandSSH().outputCommandSSH(new Configuration().sshUser, ip, new Configuration().sshKey, command);
		wordsUnik.add(top);
		if(new Configuration().Debug){
			System.out.println(top);
		}
	}
	
	/**
	 * Create java file to execute
	 * @return
	 * @throws IOException
	 */
	public String createExecJava() throws IOException{	
		String test = addEcho("src/slave/UWx.java");
		test = test.replace("slavePath = \\\"\\\"", "slavePath = \\\""+new Configuration().slavePath+"\\\"");
		//System.out.println(test);
		return test;
	}
	
	/**
	 * Parse 'echo' and ';' for the ssh command 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public String addEcho(String filepath) throws IOException{
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
	            sb.append("echo \""+line+"\" >> "+new Configuration().slavePath+"UWx.java;");
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
	public ArrayList<String> getWordsUnik() {
		return wordsUnik;
	}

}//end of class
