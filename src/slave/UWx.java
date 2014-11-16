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
public class UWx extends Thread{
	
	public String slavePath = "";

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args){
		UWx t = new UWx();
	    t.start();
	}
	
	/**
	 * UWx method
	 */
	public void run(){
		ArrayList<String> un = new ArrayList<String>();
		try {
			un = getUniqueWords(slavePath+"unik");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String res = "";
		writeFile(slavePath+"unik","",false);
		for(int i=0;i<un.size();i++){
			writeFile(slavePath+"unik",un.get(i),true);
			res += un.get(i)+" ";
		}
		System.out.println(res);
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
	 * Read a file
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
	       // res = sb.toString();
	    } finally {
	        br.close();
	    }
	    return res;
	}

}//end of class
