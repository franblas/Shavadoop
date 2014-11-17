package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Paco
 *
 */
public class FilesUtils {
	
	/**
	 * Write in a file
	 * @param filepath The file to write
	 * @param content The content to write
	 * @param overwrite if false rewrite the whole document, if true begin to write at the end of the document
	 */
	public void writeFile(String filepath,String content,boolean overwrite){
		try {
 
			File file = new File(filepath);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),overwrite);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			if(overwrite){
				bw.write("\n");
			}	
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read a file
	 * @param filepath The file to read
	 * @return a string with all the lines read of the file
	 * @throws IOException
	 */
	public String readFile(String filepath) throws IOException{
		String res = null;
	    BufferedReader br = new BufferedReader(new FileReader(filepath));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
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
