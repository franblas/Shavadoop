package master;

import java.util.ArrayList;
import java.util.HashMap;

import ssh.CommandSSH;
import utils.Configuration;

/**
 * 
 * @author Paco
 *
 */
public class Master {
	
	public HashMap<String, String> dictionary = new HashMap<String, String>();
	public HashMap<String, ArrayList<String>> dictionary2 = new HashMap<String, ArrayList<String>>();
	public HashMap<String, String> dictionary3 = new HashMap<String, String>();
	public int nbWords = 0;
	
	/**
	 * Split a file by line
	 * @param readfile
	 * @return
	 */
	public String[] splitting(String readfile){
		return readfile.split("\\r?\\n");
	}
	
	/**
	 * Clean the shavadoop folder
	 * @param host
	 */
	public void cleanFolder(String host){
		Configuration conf = new Configuration();
		new CommandSSH().outputCommandSSH(conf.sshUser, host, conf.sshKey, "rm -r -f "+conf.slavePath+"*");
		if(new Configuration().Debug){
			System.out.println("Folder "+conf.slavePath+" has been cleaned");
		}
	}
	
	/**
	 * Merge all files into one
	 * @param host
	 * @param files
	 * @param outmerge
	 */
	public void multipleMerge(String host, String files, String outmerge){
		Configuration conf = new Configuration();
		new CommandSSH().outputCommandSSH(conf.sshUser, host, conf.sshKey, "cat "+files+" > "+conf.slavePath+outmerge);
		if(new Configuration().Debug){
			System.out.println("All files has been merged into "+outmerge);
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getNbWords() {
		return nbWords;
	}

	/**
	 * 
	 * @param nbWords
	 */
	public void setNbWords(int nbWords) {
		this.nbWords = nbWords;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, String> getDictionary() {
		return dictionary;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, ArrayList<String>> getDictionary2() {
		return dictionary2;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, String> getDictionary3() {
		return dictionary3;
	}

}//end of class
