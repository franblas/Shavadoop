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
	final public String sshKey = "id_dsa";
	final public String sshUser = "fblas";
	final public String sshSubnet = "137.194.35";
	final public int sshNumberOfHosts = 10;
	
	/**
	 * Debug configuration
	 */
	final public boolean Debug = true;
}
