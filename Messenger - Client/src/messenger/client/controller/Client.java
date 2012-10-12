package messenger.client.controller;

import messenger.client.ClientConnection;

/** Main client class. Prompts the user for login or sign up. On successful login creates a 
 * {@link ChatManager} to control messaging. */
public class Client extends ClientConnection{

	private ChatManager chatManager;
	private Logger logger;
	
	public static String clientName;
	/** The unique ID of the client. Server will use it to recognize the client. */
	public static int clientID;
	
	private static String serverIP = "127.0.0.1";
	private static int serverPort = 5555;

	public Client(String serverIP, int serverPort) {
		super(serverIP, serverPort);
	}
	
	@Override
	public void processConnection() {
		
		logger = new Logger(output, input);
		logger.waitForLogIn();   
		
		System.out.println(clientID + ": " + clientName);
		logger.closeUI();
		chatManager = new ChatManager(connection, output, input);
		chatManager.run();
	}
	
	
	public static void main(String args[]) {
		Client client = new Client(serverIP, serverPort);
		client.setUpConnection();
		client.processConnection();
	}
}
