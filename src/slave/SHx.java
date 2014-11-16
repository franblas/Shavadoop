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
public class SHx extends Thread{

	//public String ipMaster = "";
	public String word = "";
	public int nb = 0;
	public int nbum = 0;
	public String slavePath = "";
	
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args){
		SHx t = new SHx();
	    t.start();
	}
	
	/**
	 * SHx Runnable
	 */
	public void run(){
		try {
			shufflingShava();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shuffling method
	 * @throws IOException
	 */
	public void shufflingShava() throws IOException{
		String res = "";
		writeFile(slavePath+"SH"+nb,"",false);
		for(int ii=0;ii<nbum;ii++){
			ArrayList<String> lines = readFile(slavePath+"UM"+ii);	
			for(int i=0;i<lines.size();i++){
				String li = lines.get(i).substring(0,lines.get(i).length()-2);
				if(li.equals(word)){
					writeFile(slavePath+"SH"+nb,lines.get(i),true);
					res += lines.get(i)+" ";
				}
			}
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
	    } finally {
	        br.close();
	    }
	    return res;
	}

}//end of class
