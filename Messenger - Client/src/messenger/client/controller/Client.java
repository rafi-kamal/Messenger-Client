package messenger.client.controller;

import messenger.client.model.Info;

/** Main client class. Prompts the user for login or sign up. On successful login creates a 
 * {@link ChatManager} to control messaging. */
public class Client {
	private Info info;
	private ChatManager chatManager;
	private Logger logger;
	
	private void run() {
		info = new Info();
		String serverIP = info.getServerIP();
		int serverPort = info.getServerPort();

		logger = new Logger(serverIP, serverPort);
		
		int newPort = logger.getNewPort();
		int clientID = logger.getClientID();
		System.out.println(newPort + ", " + clientID);
		logger.closeUI();
		chatManager = new ChatManager(serverIP, newPort, clientID);
		chatManager.run();
	}
	
	public static void main(String args[]) {
		Client client = new Client();
		client.run();
	}
}
