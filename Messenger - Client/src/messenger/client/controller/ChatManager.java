package messenger.client.controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messenger.Constants;
import messenger.client.ClientConnection;
import messenger.client.model.InfoReceiver;
import messenger.client.view.ClientGUI;


/**
 * Controls the messages to be sent and received.<p>
 * <b>Date:</b> <i>16 June 2012</i>
 * 
 * @author Rafi
 */
public class ChatManager extends ClientConnection implements Constants
{
	/** This thread will work for getting non-message data (e.g. user lists or user info) from the server. */
	private Thread infoThread;
	private InfoReceiver infoReceiver;

	public ChatManager(Socket connection, ObjectOutputStream output, ObjectInputStream input) {
		this.output = output;
		this.input = input;
		this.connection = connection;
		userInterface = new ClientGUI(this, serverPort);
		infoReceiver = new InfoReceiver(userInterface);
	}

	public void run() {
		infoThread = new Thread(infoReceiver);
		infoThread.start();
		processConnection();
		closeConnection();
	}
	
	@Override
	public void processConnection() {
		

		try {
			Client.clientID = (Integer) input.readObject();
			Client.clientName = (String) input.readObject();
			((ClientGUI) userInterface).setTitle(Client.clientName);
			while(true) {
				int senderID = (Integer) input.readObject();
				String message = (String) input.readObject();				

				((ClientGUI) userInterface).displayMessage(message, senderID);
			}
		}
		catch(IOException ioException) {
			userInterface.displayErrorMessage("Server terminated connection");
			closeConnection();
		}
		catch(ClassNotFoundException classNotFoundException) {
			userInterface.displayErrorMessage("Unknown object type received");
		}
	}
	
	@Override
	public void closeConnection() {
		super.closeConnection();

		if(infoReceiver != null) infoReceiver.closeConnection();
		if(infoThread != null) infoThread.interrupt();
		userInterface.displayInfoMessage("Connection terminated");
	}
	
	/** 
	 * Sends a message to a specific client via the server.
	 * 
	 *  @param message Message to be sent
	 *  @param receiverID Client ID of the receiver 
	 */
	public void sendData(String message, int receiverID) {
		super.sendData(receiverID);
		super.sendData(message);
	}
	
	public void sendFile(int clientID) {
		infoReceiver.sendFileIntent(clientID);
	}
}
