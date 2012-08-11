package messenger.client.controller;
import java.io.IOException;

import messenger.Constants;
import messenger.client.Connection;
import messenger.client.model.InfoReceiver;
import messenger.client.view.ClientGUI;


/**
 * Main client class, handles and organizes the logics<p>
 * <b>Date:</b> <i>16 June 2012</i>
 * 
 * @author Rafi
 *
 */
public class ChatManager extends Connection implements Constants
{
	private Thread infoThread;
	private InfoReceiver infoReceiver;

	public ChatManager(String serverIP, int serverPort, int clientID) {
		super(serverIP, serverPort);
		this.clientID = clientID;
		userInterface = new ClientGUI(this, serverPort);
		infoReceiver = new InfoReceiver(userInterface, serverIP, serverPort + 1, clientID);
		infoThread = new Thread(infoReceiver);
	}

	public void run() {
		
		setUpConnection(serverIP, serverPort);
		infoThread.start();
		processConnection();
		closeConnection();
	}
	
	@Override
	public void processConnection() {
		
		while(true) {
			try {
				int senderID = (Integer) input.readObject();
				String message = (String) input.readObject();				

				((ClientGUI) userInterface).displayMessage(message, senderID);
			}
			catch(IOException ioException) {
				userInterface.displayErrorMessage("Server terminated connection");
				closeConnection();
				break;
			}
			catch(ClassNotFoundException classNotFoundException) {
				userInterface.displayErrorMessage("Unknown object type received");
				break;
			}
		}
	}
	
	@Override
	public void closeConnection() {
		super.closeConnection();

		if(infoReceiver != null) infoReceiver.closeConnection();
		if(infoThread != null) infoThread.interrupt();
		userInterface.displayInfoMessage("Connection terminated");
	}
	
	public void sendData(String message, int receiverID) {
		super.sendData(receiverID);
		super.sendData(message);
		((ClientGUI) userInterface).displayMessage(message, receiverID);
	}
}