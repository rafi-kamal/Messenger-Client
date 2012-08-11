package messenger.client.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

/**
 * Read necessary data of the client from the file <i>info.txt</i>. Change the data if necessary. 
 * 
 * @author Rafi
 *
 */
public class Info 
{
	private String serverIP;
	private int serverPort;
	private String infoServerIP;
	private int infoServerPort;
	private int keepSigned;
	private String userName;
	private String passWord;
	private int clientID;
	
	private static Scanner input;
	private static Formatter output;
	
	public Info()
	{
		try
		{
			input = new Scanner(new File("data/info.txt"));
			
			serverIP = input.next();
			serverPort = input.nextInt();
			infoServerIP = input.next();
			infoServerPort = input.nextInt();
			keepSigned = input.nextInt();
			userName = input.next();
			passWord = input.next();
			clientID = input.nextInt();
			
			if(input != null)
				input.close();
		}
		catch(FileNotFoundException fileNotFoundException)
		{
			System.err.println("Error retrieving necessary data.");
			System.exit(1);
		}
	}
	
	/**
	 * Called when the user logs in
	 * @param keepUserSigned Whether the user will be kept signed until he signs out. If set to false, user has to log in every time he starts the program.
	 * @param newUserName User name
	 * @param newPassword Password
	 */
	void changeUserData(boolean keepUserSigned, String newUserName, String newPassword)
	{
		keepSigned = keepUserSigned ? 1 : 0;
		userName = newUserName;
		passWord = newPassword;
		
		try
		{
			output = new Formatter(new File("data/info.txt"));
			output.format("%s\n%d\n%d\n%s\n%s\n%d\n", serverIP, serverPort, keepSigned, userName, passWord, clientID);
			
			if(output != null)
				output.close();
		}
		catch(Exception exception)
		{
			 System.err.println("Error writing to file.");
		}
	}
	
	public String getServerIP()
	{
		return serverIP;
	}
	
	public int getServerPort()
	{
		return serverPort;
	}
	
	public boolean getKeepSigned()
	{
		return (keepSigned == 1) ? true : false;
	}
	
	public String getUserName()
	{
		return userName;
	}
	
	public String getPassword()
	{
		return passWord;
	}
	
	public int getClientID()
	{
		return clientID;
	}

	public String getInfoServerIP() {
		return infoServerIP;
	}

	public int getInfoServerPort() {
		return infoServerPort;
	}
}
