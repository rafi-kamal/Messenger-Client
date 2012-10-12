package messenger.client.model;

import java.io.IOException;
import java.util.Map;

import javax.swing.JOptionPane;

import messenger.Constants;
import messenger.client.ClientConnection;
import messenger.client.controller.Client;
import messenger.client.controller.filetransfer.RecieveFile;
import messenger.client.controller.filetransfer.SendFile;
import messenger.client.view.ClientGUI;
import messenger.client.view.GUI;

/**
 * Requests and receives non-message data from the server (e.g. user lists or
 * user info).
 */
public class InfoReceiver extends ClientConnection implements Constants, Runnable {

	private static int infoServerPort = 5556;
	private static String infoServerIP = "127.0.0.1";
	private static int fileServerPort = 5557;
	private static String fileServerIP = "127.0.0.1";
	
	public InfoReceiver(GUI userInterface) {
		super(infoServerIP, infoServerPort);
		this.userInterface = userInterface;
	}

	@Override
	public void processConnection() {
		while (true) {
			try {
				int command = (Integer) input.readObject();

				switch (command) {
				case ALL_LIST:
					updateAllUserList();
					System.out.println("Update found from "
							+ connection.getPort() + " in "
							+ connection.getLocalPort());
					break;
				case READY:
					SendFile sendFile = new SendFile(fileServerIP, fileServerPort, 
							(ClientGUI) userInterface);
					Thread fileSendThread = new Thread(sendFile);
					fileSendThread.start();
					break;
				case RECEIVE_FILE:
					RecieveFile recieveFile = new RecieveFile(fileServerIP, fileServerPort, 
							(ClientGUI) userInterface);
					Thread fileRecieveThread = new Thread(recieveFile);
					fileRecieveThread.start();
					sendData(READY);
					break;
				case CANCEL:
					JOptionPane.showMessageDialog(null, "File sending refused");
					break;
				}
			} catch (ClassNotFoundException classNotFoundException) {
				userInterface.displayErrorMessage("Unknown object type received");
			} catch (IOException ioException) {
				userInterface.displayErrorMessage("Server terminated connection");
				closeConnection();
			}
		}
	}

	/*** Updates the <b>All User list.</b> */
	public void updateAllUserList() {

		try {

			Map<Integer, String> users = (Map<Integer, String>) input.readObject();
			System.out.println(users);
			
			users.remove(Client.clientID);
			((ClientGUI) userInterface).setAllUserList(users);
		} catch (IOException e) {
			userInterface.displayErrorMessage("Error getting User List");
		} catch (ClassNotFoundException e) {
			userInterface.displayErrorMessage("Error getting User List");
		}
	}

	public void sendFileIntent(int clientID) {
		sendData(SEND_FILE);
		sendData(clientID);
	}

	@Override
	public void run() {
		setUpConnection(infoServerIP, serverPort);
		processConnection();
	}
}
