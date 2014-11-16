package utils;

/**
 * 
 * @author Paco
 *
 */
public class Configuration {

	/**
	 * Slave configuration
	 */
	final public String slavePath = "shavadoop/";
	
	/**
	 * SSH configuration
	 */
	final public String sshKey = "id_dsa_loc";
	final public String sshUser = "paco";
	final public String sshSubnet = "10.120.150";
	final public int sshNumberOfHosts = 1;
	
	/**
	 * Debug configuration
	 */
	final public boolean Debug = true;
}
