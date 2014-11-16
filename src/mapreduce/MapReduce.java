package mapreduce;

import java.io.IOException;
import java.util.ArrayList;

import master.Master;
import slave.Mapping;
import slave.Merging;
import slave.Reducing;
import slave.Shuffling;
import slave.Slave;
import slave.UnikWords;
import ssh.CheckNetwork;
import utils.Configuration;
import utils.FilesUtils;
import utils.SystemUtils;

public class MapReduce {
	
	public long durationTime = 0; //in milliseconds

	/**
	 * Execute the map reduce
	 * @param filename
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
		int hostnum = 0;
		for(int i=0;i<lines.length;i++){		
			if(hostnum > (new Configuration().sshNumberOfHosts)-1){
				hostnum = 0;
			}
			slave.mapping = new Mapping(i,hosts.get(hostnum),lines[i],master);
			master.dictionary.put(slave.mapping.getUm(),slave.mapping.getNameMachine());
			slave.mapping.start();
			hostnum++;
		}
		slave.mapping.join();
		
		System.out.println("-------------------- Sort operation ------------------------");
		
		//sort
		slave.unik = new UnikWords(hosts.get(new SystemUtils().randInt(0, (new Configuration().sshNumberOfHosts)-1)));
		slave.unik.start();
		slave.unik.join();
		
		int umnb = master.getDictionary().keySet().size();
		String[] unwo = slave.unik.getWordsUnik().get(0).split("\\s");
		
		System.out.println("-------------------- Shuffle operation ------------------------");
		
		//shuffle
		int hostindex = 0;
		for(int j=0;j<unwo.length;j++){
			if(hostindex > (new Configuration().sshNumberOfHosts)-1){
				hostindex = 0;
			}
			slave.shuffling = new Shuffling(unwo[j],hosts.get(hostindex),j,umnb);
			master.dictionary3.put(slave.shuffling.getSh(), slave.shuffling.getNameMachine());
			slave.shuffling.start();
			hostindex++;
		}
		slave.shuffling.join();
		
		System.out.println("-------------------- Reduce operation ------------------------");
		
		//reduce
		int hostindex2 = 0;
		for(int j=0;j<unwo.length;j++){
			if(hostindex2 > (new Configuration().sshNumberOfHosts)-1){
				hostindex2 = 0;
			}
			slave.reducing = new Reducing(hosts.get(hostindex2),j);
			slave.reducing.start();
			hostindex2++;
		}
		slave.reducing.join();
		
		System.out.println("-------------------- Merge operation ------------------------");
		
		//merge
		int hostindex3 = 0;
		for(int k=0;k<unwo.length;k++){
			if(hostindex3 > (new Configuration().sshNumberOfHosts)-1){
				hostindex3 = 0;
			}
			slave.merging = new Merging(hosts.get(hostindex3),k);
			slave.merging.start();
			hostindex3++;
		}
		slave.merging.join();
		
		long endTime = System.nanoTime();
		durationTime = (endTime - startTime) / 1000000;
		
		System.out.println(">>>>>>>>>>>>>>>>>>>> End MapReduce <<<<<<<<<<<<<<<<<<<<<<<<<");
	}

	/**
	 * 
	 * @return
	 */
	public long getDurationTime() {
		return durationTime;
	}
	
	

}//end of class
