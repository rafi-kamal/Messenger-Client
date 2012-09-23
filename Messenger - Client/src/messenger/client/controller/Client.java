package messenger.client.controller;

import messenger.client.model.Info;

/** Main client class. Prompts the user for login or sign up. On successful login creates a 
 * {@link ChatManager} to control messaging. */
public class Client {
	private Info info;
	private ChatManager chatManager;
	private Logger logger;
	
	public static String clientName;
	/** The unique ID of the client. Server will use it to recognize the client. */
	public static int clientID;
	
	private void run() {
		info = new Info();
		String serverIP = info.getServerIP();
		int serverPort = info.getServerPort();

		logger = new Logger(serverIP, serverPort);
		
		int newPort = logger.getNewPort();
		System.out.println(newPort + ", " + clientID + ": " + clientName);
		logger.closeUI();
		chatManager = new ChatManager(serverIP, newPort);
		chatManager.run();
	}
	
	public static void main(String args[]) {
		Client client = new Client();
		client.run();
	}
}
