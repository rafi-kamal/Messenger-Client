package messenger.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messenger.Constants;
import messenger.client.view.GUI;

public abstract class Connection implements Constants{
	protected String serverIP;
	protected int serverPort;
	protected int clientID;
	
	protected ObjectOutputStream output;
	protected ObjectInputStream input;
	protected Socket connection;
	
	protected GUI userInterface;
	
	public Connection(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}
	
	/**
	 * Sets up the connection with the specified IP address and PortNumber and creates input-output streams from that connection by initializing components.<br>
	 * If it fails, then it attempts again to setup a connection.
	 */
	
	public void setUpConnection(String ipAddress, int portNumber) {
		try
		{
			connection = new Socket(ipAddress, portNumber);
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());	
			System.out.println("Connection established with " + ipAddress + ", Port: " + portNumber);
		}
		catch(IOException ioException)
		{
			userInterface.displayErrorMessage("Error getting info input-output stream");
			closeConnection();
		}
	}
	
	public abstract void processConnection();

	public void closeConnection()
	{
		try
		{			
			if(input != null) input.close();
			if(output != null) output.close();
			if(connection != null) connection.close();
		}
		catch(IOException ioException)
		{
			userInterface.displayErrorMessage("Error closing connection properly");
		}
	}

	/**
	 * Called when the user is ready to send his message
	 * @param message Message to be sent to the server
	 */
	public void sendData(Object message)
	{
		try
		{
			output.writeObject(message);
			output.flush();
		}
		catch(IOException ioException)
		{
			userInterface.displayErrorMessage("Error sending data. Please try again");
		}
	}

}
