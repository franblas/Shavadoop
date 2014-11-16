package slave;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * 
 * @author Paco
 *
 */
public class UMx extends Thread{
	
	//public String ipMaster = "";
	public String line = "";
	public int nb = 0;
	public String slavePath = "";

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args){
		UMx t = new UMx();
	    t.start();
	}
	
	/**
	 * UMx Runnable
	 */
	public void run(){
		mappingShava();
		ArrayList<String> unik = new ArrayList<String>();
		try {
			unik = getUniqueWords(slavePath+"UM"+nb);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=0;i<unik.size();i++){
			writeFile(slavePath+"unik",unik.get(i),true);
		}
	}   
	
	/**
	 * Get list of unique words for a file
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> getUniqueWords(String filename) throws IOException{
		ArrayList<String> words = readFile(filename);	
		ArrayList<String> wordsunique = new ArrayList<String>();
		for (String word : words) {
			wordsunique.add(word.split(",")[0]);
		}
		HashSet<String> hs = new HashSet<String>();
	 	hs.addAll(wordsunique);
	 	wordsunique.clear();
	 	wordsunique.addAll(hs);
		return wordsunique;
	}
	
	/**
	 * Mapping method
	 */
	public void mappingShava(){
		String[] wo = line.split(" ");
		String res = "";
		writeFile(slavePath+"UM"+nb,"",false);
		for(int j=0;j<wo.length;j++){
			writeFile(slavePath+"UM"+nb,wo[j]+",1",true);
			res += wo[j]+" ";
		}
		System.out.println(res);
	}

	/**
	 * Write in a file
	 * @param filepath
	 * @param content
	 * @param overwrite
	 */
	public void writeFile(String filepath,String content,boolean overwrite){
		try { 
			File file = new File(filepath);

			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),overwrite);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			if(overwrite){
				bw.newLine();
			}	
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read in a file
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> readFile(String filepath) throws IOException{
		ArrayList<String> res = new ArrayList<String>();
	    BufferedReader br = new BufferedReader(new FileReader(filepath));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            res.add(line);
	        	sb.append(line);
	            sb.append("\\n");
	            line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
	    return res;
	}


}//end of class
