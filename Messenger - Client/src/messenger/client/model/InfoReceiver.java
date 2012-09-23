package messenger.client.model;

import java.io.IOException;
import java.util.Map;

import messenger.Constants;
import messenger.client.Connection;
import messenger.client.controller.Client;
import messenger.client.view.ClientGUI;
import messenger.client.view.GUI;

/**
 * Requests and receives non-message data from the server (e.g. user lists or
 * user info).
 */
public class InfoReceiver extends Connection implements Constants, Runnable {

	public InfoReceiver(GUI userInterface, String serverIP, int serverPort) {
		super(serverIP, serverPort);
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
				case FRIEND_LIST:
					updateFriendList();
					break;
				case USER_INFO:
					int clientID = (Integer) input.readObject();
					getInfo(clientID);
					break;
				}
			} catch (ClassNotFoundException classNotFoundException) {
				userInterface
						.displayErrorMessage("Unknown object type received");
			} catch (IOException ioException) {
				userInterface
						.displayErrorMessage("Server terminated connection");
				closeConnection();
			}
		}
	}

	/*** Updates the <b>All User list.</b> */
	public void updateAllUserList() {

		try {

			Map<Integer, String> users = (Map<Integer, String>) input.readObject();
			users.remove(Client.clientID);

			((ClientGUI) userInterface).setAllUserList(users);
			System.out.println(users);
		} catch (IOException e) {
			userInterface.displayErrorMessage("Error getting User List");
		} catch (ClassNotFoundException e) {
			userInterface.displayErrorMessage("Error getting User List");
		}
	}

	/** Gets info (name, address e.g.) about a client from the server. */
	private void getInfo(int clientID) {

	}

	/*** Updates the <b>Friend list</b>. */
	private void updateFriendList() {

	}

	@Override
	public void run() {
		setUpConnection(serverIP, serverPort);
		processConnection();
	}
}
