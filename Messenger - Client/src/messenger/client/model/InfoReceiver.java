package messenger.client.model;

import java.io.IOException;
import java.util.ArrayList;

import messenger.Constants;
import messenger.client.Connection;
import messenger.client.view.ClientGUI;
import messenger.client.view.GUI;

public class InfoReceiver extends Connection implements Constants, Runnable {

	public InfoReceiver(GUI userInterface, String serverIP, int serverPort, int clientID) {
		super(serverIP, serverPort);
		this.clientID = clientID;
		this.userInterface = userInterface;
	}

	@Override
	public void processConnection() {
		while(true)
		{
			try
			{
				int command = (Integer) input.readObject();
		
				switch(command) {
				case ALL_LIST:
					updateAllUserList();
					System.out.println("Update found from " + connection.getPort() + " in " + connection.getLocalPort());
					break;
				case FRIEND_LIST:
					updateFriendList();
					break;
				case USER_INFO:
					getInfo();
					break;
				}
			}
			catch(ClassNotFoundException classNotFoundException) {
				userInterface.displayErrorMessage("Unknown object type received");
			}
			catch(IOException ioException) {
				userInterface.displayErrorMessage("Server terminated connection");
				closeConnection();
			}
		}
	}
	
	public void updateAllUserList() {
		
		try {
			
			String[] userSet = (String []) input.readObject();
			ArrayList<String> userList = new ArrayList<String>();
			
			for(String user : userSet)
				if(Integer.parseInt(user) != clientID)	
					userList.add(user);
			
			((ClientGUI) userInterface).setAllUserList(userList);
		}
		catch (IOException e) {
			userInterface.displayErrorMessage("Error getting User List");
		} 
		catch (ClassNotFoundException e)
		{
			userInterface.displayErrorMessage("Error getting User List");
		}
	}
	
	private void getInfo() {
		
	}

	private void updateFriendList() {
		
	}

	@Override
	public void run() {
		setUpConnection(serverIP, serverPort);
		processConnection();
	}
}
