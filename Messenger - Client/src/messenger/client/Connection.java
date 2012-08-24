package messenger.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messenger.Constants;
import messenger.client.view.GUI;

/** 
 * Base class for client connection. Any class in the client project which will be connected to
 * the server will inherit this class.
 */
public abstract class Connection implements Constants {
	/** Client will be connected to this IP */
	protected String serverIP;
	/** Client will be connected to this port of the server */
	protected int serverPort;
	/** The unique ID of the client. Server will use it to recognize the client. */
	protected int clientID;
	
	protected ObjectOutputStream output;
	protected ObjectInputStream input;
	protected Socket connection;
	
	/** Any error or info message will be shown using this. */
	protected GUI userInterface;
	
	public Connection(String serverIP, int serverPort) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
	}
	
	/** Sets up the connection with the server. */
	public void setUpConnection(String ipAddress, int portNumber) {
		try {
			connection = new Socket(ipAddress, portNumber);
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());	
			System.out.println("Connection established with " + ipAddress + ", Port: " + portNumber);
		}
		catch(IOException ioException) {
			userInterface.displayErrorMessage("Error getting info input-output stream");
			closeConnection();
		}
	}
	
	/** Processes the input stream. */
	public abstract void processConnection();

	/** Closes. the connection */
	public void closeConnection() {
		try {			
			if(input != null) input.close();
			if(output != null) output.close();
			if(connection != null) connection.close();
		}
		catch(IOException ioException) {
			userInterface.displayErrorMessage("Error closing connection properly");
		}
	}

	/** Sends data to the server. */
	public void sendData(Object message) {
		try {
			output.writeObject(message);
			output.flush();
		}
		catch(IOException ioException) {
			userInterface.displayErrorMessage("Error sending data. Please try again");
		}
	}
}
