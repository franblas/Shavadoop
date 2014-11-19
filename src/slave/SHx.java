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
public class SHx {

	//public String ipMaster = "";
	public static String word = "";
	public static int nb = 0;
	public static int nbum = 0;
	public static String slavePath = "";
	
	/**
	 * Main
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		shufflingShava();
	}
	
	/**
	 * Shuffling method
	 * @throws IOException
	 */
	public static void shufflingShava() throws IOException{
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
	 * @param filepath The file to write
	 * @param content The content to write
	 * @param overwrite if false rewrite the whole document, if true begin to write at the end of the document
	 */
	public static void writeFile(String filepath,String content,boolean overwrite){
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
	public static ArrayList<String> readFile(String filepath) throws IOException{
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
