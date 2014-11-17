package slave;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Paco
 *
 */
public class REx extends Thread{

	//public String ipMaster = "";
	public int nb = 0;
	public String slavePath = "";
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args){
		REx t = new REx();
	    t.start();
	}
	
	/**
	 * REx Runnable
	 */
	public void run(){
		try {
			reducingShava();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reducing method
	 * @throws IOException
	 */
	public synchronized void reducingShava() throws IOException{
		ArrayList<String> lines = readFile(slavePath+"SH"+nb);
		writeFile(slavePath+"RE"+nb,"",false);
		String word = lines.get(0).split(",")[0];
		writeFile(slavePath+"RE"+nb,word+","+lines.size(),true);
		System.out.println(word+","+lines.size());
	}
	
	/**
	 * Write in a file
	 * @param filepath The file to write
	 * @param content The content to write
	 * @param overwrite if false rewrite the whole document, if true begin to write at the end of the document
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
	 * @param filepath The file to read
	 * @return the whole content of the file
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
