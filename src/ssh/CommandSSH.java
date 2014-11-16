package ssh;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.InputStream;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.Configuration;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * 
 * @author Paco
 *
 */
public class CommandSSH {
	
	/**
	 * Execute SSH command and return output
	 * @param username
	 * @param host
	 * @param key
	 * @param commandssh
	 * @return
	 */
	public String outputCommandSSH(String username, String host, String key, String commandssh){
		String res = "";
		try{
		      JSch jsch=new JSch();
		      java.util.Properties config = new java.util.Properties();
		      config.put("StrictHostKeyChecking", "no"); //avoid host key checking

		      int SSH_port = 22; 
		      
		      jsch.addIdentity(key , "passphrase");

		      Session session=jsch.getSession(username, host, SSH_port); //SSH connection
		 
				if(new Configuration().Debug){
					System.out.println("Connection to "+host+" on the port "+SSH_port+" with key: "+key+" and username: "+username);
				}
		      
		      // username and passphrase will be given via UserInfo interface.
		      UserInfo ui=new MyUserInfo();
		      session.setUserInfo(ui);
		      session.setConfig(config);
		      session.connect();

		      Channel channel=session.openChannel("exec"); // open channel for exec command
		      ((ChannelExec)channel).setCommand(commandssh);

		      channel.setInputStream(null);

		      ((ChannelExec)channel).setErrStream(System.err);
			
		      InputStream in=channel.getInputStream();
			
		      channel.connect();
			
			/*
			 * get output ssh command launched
			 */
			byte[] tmp=new byte[1024];
			while(true){
				while(in.available()>0){
					int i=in.read(tmp, 0, 1024);
					if(i<0)break;
					res += new String(tmp, 0, i);
					//System.out.print(new String(tmp, 0, i));
				}
				if(channel.isClosed()){
					if(in.available()>0) continue; 
					if(new Configuration().Debug){
						System.out.println("exit-status: "+channel.getExitStatus());
					}
					break;
				}
				try{Thread.sleep(1000);}catch(Exception ee){}
			}
			channel.disconnect();
			session.disconnect();
			}
			
			catch(Exception e){
				System.out.println(e);
			}
		return res;
	}
	
	
	public class MyUserInfo implements UserInfo, UIKeyboardInteractive{
	    public String getPassword(){ return null; }
	    public boolean promptYesNo(String str){
	      Object[] options={ "yes", "no" };
	      int foo=JOptionPane.showOptionDialog(null, 
	             str,
	             "Warning", 
	             JOptionPane.DEFAULT_OPTION, 
	             JOptionPane.WARNING_MESSAGE,
	             null, options, options[0]);
	       return foo==0;
	    }
	  
	    String passphrase;
	    JTextField passphraseField=(JTextField)new JPasswordField(20);
	 
	    public String getPassphrase(){ return passphrase; }
	    public boolean promptPassphrase(String message){
	      Object[] ob={passphraseField};
	      int result=
		JOptionPane.showConfirmDialog(null, ob, message,
					      JOptionPane.OK_CANCEL_OPTION);
	      if(result==JOptionPane.OK_OPTION){
	        passphrase=passphraseField.getText();
	        return true;
	      }
	      else{ return false; }
	    }
	    public boolean promptPassword(String message){ return true; }
	    public void showMessage(String message){
	      JOptionPane.showMessageDialog(null, message);
	    }
	    final GridBagConstraints gbc = 
	      new GridBagConstraints(0,0,1,1,1,1,
	                             GridBagConstraints.NORTHWEST,
	                             GridBagConstraints.NONE,
	                             new Insets(0,0,0,0),0,0);
	    private Container panel;
	    public String[] promptKeyboardInteractive(String destination,
	                                              String name,
	                                              String instruction,
	                                              String[] prompt,
	                                              boolean[] echo){
	      panel = new JPanel();
	      panel.setLayout(new GridBagLayout());
	 
	      gbc.weightx = 1.0;
	      gbc.gridwidth = GridBagConstraints.REMAINDER;
	      gbc.gridx = 0;
	      panel.add(new JLabel(instruction), gbc);
	      gbc.gridy++;
	 
	      gbc.gridwidth = GridBagConstraints.RELATIVE;
	 
	      JTextField[] texts=new JTextField[prompt.length];
	      for(int i=0; i<prompt.length; i++){
	        gbc.fill = GridBagConstraints.NONE;
	        gbc.gridx = 0;
	        gbc.weightx = 1;
	        panel.add(new JLabel(prompt[i]),gbc);
	 
	        gbc.gridx = 1;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.weighty = 1;
	        if(echo[i]){
	          texts[i]=new JTextField(20);
	        }
	        else{
	          texts[i]=new JPasswordField(20);
	        }
	        panel.add(texts[i], gbc);
	        gbc.gridy++;
	      }
	 
	      if(JOptionPane.showConfirmDialog(null, panel, 
	                                       destination+": "+name,
	                                       JOptionPane.OK_CANCEL_OPTION,
	                                       JOptionPane.QUESTION_MESSAGE)
	         ==JOptionPane.OK_OPTION){
	        String[] response=new String[prompt.length];
	        for(int i=0; i<prompt.length; i++){
	          response[i]=texts[i].getText();
	        }
		return response;
	      }
	      else{
	        return null;  // cancel
	      }
	    }
	  }

}//end of class
