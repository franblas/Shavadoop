package test;

import java.io.IOException;

import mapreduce.MapReduce;

/**
 * 
 * @author Paco
 *
 */
public class Test {

	/**
	 * MapReduce Test
	 * See the class Configuration to set parameters 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException{
		MapReduce mapreduce = new MapReduce();
		mapreduce.execMapReduce("INPUT.txt");
		System.out.println("MapReduce in "+mapreduce.getDurationTime()/1000+" s");
	}

}//end of class
