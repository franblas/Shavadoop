package mapreduce;

import java.io.IOException;
import java.util.ArrayList;

import master.Master;
import slave.Mapping;
import slave.Reducing;
import slave.Shuffling;
import slave.Slave;
import slave.UnikWords;
import ssh.CheckNetwork;
import utils.Configuration;
import utils.FilesUtils;
import utils.SystemUtils;

/**
 * 
 * @author Paco
 *
 */
public class MapReduce {
	
	public long durationTime = 0; //in milliseconds

	/**
	 * Execute the map reduce
	 * @param filename The file to mapreduce
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void execMapReduce(String filename) throws IOException, InterruptedException{
		
		Master master = new Master();
		Slave slave = new Slave();
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> Begin MapReduce <<<<<<<<<<<<<<<<<<<<<<<<<");
		
		long startTime = System.nanoTime();
		
		System.out.println("-------------------- Check open hosts ------------------------");
		
		//get list of available hosts
		ArrayList<String> hosts = new CheckNetwork().openSshHosts(new Configuration().sshSubnet, new Configuration().sshNumberOfHosts);
		
		System.out.println("-------------------- Clean the shabadoop folder ------------------------");
		
		//clean the shavadoop folder
		master.cleanFolder(hosts.get(new SystemUtils().randInt(0, (new Configuration().sshNumberOfHosts)-1)));
		
		System.out.println("-------------------- Split the input file ------------------------");
		
		//split
		String[] lines = master.splitting(new FilesUtils().readFile(filename));
		
		if(new Configuration().Debug){
			System.out.println("File "+filename+" has been split");
		}

		System.out.println("-------------------- Map operation ------------------------");
		
		//map
		String unikk = "";
		int correct = 1; //Correction
		if(lines.length>hosts.size()){
			correct = lines.length;
		}
		int offset = 0;
		int counter = 0;
		while(counter<correct){
			int ii = 0;
			for(int i=0;i<hosts.size();i++){		
				if(correct==1 && i==lines.length){
					break;
				}
				if(correct==lines.length && (i+offset)==lines.length){
					break;
				}
				//slave.mapping = new Mapping(i,hosts.get(hostnum),lines[i],master);
				slave.mapping = new Mapping(i+offset,hosts.get(i),lines[i+offset],master);
				master.dictionary.put(slave.mapping.getUm(),slave.mapping.getNameMachine());
				ii++;
				slave.mapping.start();
				unikk += new Configuration().slavePath+"unik"+(i+offset)+" ";
			}
			slave.mapping.join();
			offset =offset+hosts.size();
			//counter=counter+ii+1;
			counter=ii+counter;
		}	


		System.out.println("-------------------- Multiple merge ------------------------");
		
		master.multipleMerge(hosts.get(new SystemUtils().randInt(0, (new Configuration().sshNumberOfHosts)-1)), unikk, "unik");
		
		System.out.println("-------------------- Sort operation ------------------------");
		
		//sort
		slave.unik = new UnikWords(hosts.get(new SystemUtils().randInt(0, (new Configuration().sshNumberOfHosts)-1)));
		slave.unik.start();
		slave.unik.join();
		
		int umnb = master.getDictionary().keySet().size();
		String[] unwo = slave.unik.getWordsUnik().get(0).split("\\s");
		
		System.out.println("-------------------- Shuffle operation ------------------------");
		
		//shuffle
		correct=1;
		if(unwo.length>hosts.size()){ //Correction Bis
			correct = unwo.length;
		}
		offset = 0;
		counter = 0;
		while(counter<correct){
			int jj = 0;
			for(int j=0;j<hosts.size();j++){
				if(correct==1 && j==unwo.length){
					break;
				}
				if(correct==unwo.length && (j+offset)==unwo.length){
					break;
				}
				//slave.shuffling = new Shuffling(unwo[j],hosts.get(hostindex),j,umnb);
				slave.shuffling = new Shuffling(unwo[j+offset],hosts.get(j),j+offset,umnb);
				master.dictionary3.put(slave.shuffling.getSh(), slave.shuffling.getNameMachine());
				jj++;
				slave.shuffling.start();
			}
			slave.shuffling.join();
			offset = offset+hosts.size();
			//counter=jj+counter+1;
			counter=jj+counter;
		}
		
		System.out.println("-------------------- Reduce operation ------------------------");
		
		//reduce
		correct=1;
		String red = "";
		if(unwo.length>hosts.size()){ //Correction Bis
			correct = unwo.length;
		}
		offset = 0;
		counter = 0;
		while(counter<correct){
			int kk = 0;
			for(int k=0;k<hosts.size();k++){
				if(correct==1 && k==unwo.length){
					break;
				}
				if(correct==unwo.length && (k+offset)==unwo.length){
					break;
				}
				//slave.reducing = new Reducing(hosts.get(hostindex2),k);
				slave.reducing = new Reducing(hosts.get(k),k+offset);
				kk++;
				slave.reducing.start();
				red += new Configuration().slavePath+"RE"+(k+offset)+" ";
			}
			slave.reducing.join();
			offset = offset + hosts.size();
			//counter = counter + kk+1;
			counter=kk+counter;
		}
		
		System.out.println("-------------------- Merge operation ------------------------");
		
		//merge
		master.multipleMerge(hosts.get(new SystemUtils().randInt(0, (new Configuration().sshNumberOfHosts)-1)), red, "output");
		
		long endTime = System.nanoTime();
		durationTime = (endTime - startTime) / 1000000;
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> End MapReduce <<<<<<<<<<<<<<<<<<<<<<<<<");
	}

	/**
	 * Get the duration time of the MapReduce
	 * @return
	 */
	public long getDurationTime() {
		return durationTime;
	}
	
}//end of class