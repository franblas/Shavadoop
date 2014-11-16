package ssh;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import utils.Configuration;

public class CheckNetwork {
	
	/**
	 * 
	 * @param subnet
	 * @param nbhosts
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ArrayList<String> openSshHosts(String subnet, int nbhosts){
		//TODO : issue if more than 250 hosts or if nbhosts > nbhosts available for a subnet
		ArrayList<String> res = new ArrayList<String>();
		int iter = 0;
		for(int i=0;i<255;i++){
			if(checkHost(subnet+"."+i,22,300)){
				res.add(subnet+"."+i);
				if(new Configuration().Debug){
					System.out.println("Host "+subnet+"."+i+" is open and ready to be used");
				}		
				iter++;
				if(iter==nbhosts){i=254;}
			}
			if(new Configuration().Debug){
				System.out.println("Host "+subnet+"."+i+" is not open");
			}	
		}
		return res;
	}
	
	/**
	 * Check if port is open on host
	 * @param host
	 * @param port
	 * @param timeout
	 * @return
	 */
	public boolean checkHost(String host, int port, int timeout){
		boolean res = false;
		try {  
			Socket s = new Socket();
			s.connect(new InetSocketAddress(host, port), timeout);
			res = true; 
            s.close();  
        }  
        catch (IOException ex) {  
        	res = false;
        }  
		return res;
	}
	
}//end of class
