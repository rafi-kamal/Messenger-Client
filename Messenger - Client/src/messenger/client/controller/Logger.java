package messenger.client.controller;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import messenger.client.Connection;
import messenger.client.view.LoggingUI;

/**
 * Displays a login screen and interact with the server for logging in.<br>
 * If user is has no account, he can sign up and open a new account.<p>
 * 
 * After logging in, the client will be assigned a specific port number from the server through which
 * he can conduct further communication.
 * 
 * @return Port number assigned to the client
 */
public class Logger extends Connection {
	
	/** Prevents the <code>getNewPort</code> and <code>getClientID</code> methods from executing 
	 * until the user logs in. */
	private final Lock proceed = new ReentrantLock();
	private final Condition loginCompleted = proceed.newCondition();
	
	/** If the user is logged in. */
	private boolean isLoggedIn = false;
	
	public Logger(String serverIP, int serverPort) {
		super(serverIP, serverPort);
		userInterface = new LoggingUI(this);
		userInterface.setVisible(true);
	}

	/** The new port number on which client will reconnect after successful login. */
	private int newPort;
	
	/**
	 * Sends the server a login request. Server will process this request and sends a reply.
	 * The reply will be analyzed in the <code>processConnection</code> method.
	 * 
	 * @param userName User name of the client
	 * @param password Password of the user
	 * @param rememberMe If the client decides to be logged in this machine until he logs out
	 */
	public void logIn(String userName, String password, boolean rememberMe) {
		setUpConnection(serverIP, serverPort);
		sendData(LOGIN_REQUEST);
		sendData(userName);
		sendData(password);
		processConnection();
		closeConnection();
	}
	
	/** 
	 * Sends the server a sign up request. Server will process this request and sends a reply.
	 * The reply will be analyzed in the <code>processConnection</code> method.
	 */
	public void signUp(String userName, String password) {
		setUpConnection(serverIP, serverPort);
		sendData(SIGNUP_REQUEST);
		sendData(userName);
		sendData(password);
		processConnection();
		closeConnection();
	}
	
	/** Returns the new port number where the client will reconnect.
	 *  If log in is yet to be completed, the method waits. */
	public int getNewPort() {
		if(!isLoggedIn)
			waitForLogIn();
		return newPort;
	}

	@Override
	public void processConnection() {
		try {
			int messageCode = (Integer) input.readObject();
			
			switch(messageCode) {
			case LOGIN_SUCCESSFUL:
				newPort = (Integer) input.readObject();
				Client.clientID = (Integer) input.readObject();
				Client.clientName = (String) input.readObject();
				isLoggedIn = true;
				
				proceed.lock();
				loginCompleted.signal();
				proceed.unlock();
				
				System.out.println("Login successful. Reconnecting in port " + newPort);
				break;
			case LOGIN_FAILED:
				userInterface.displayErrorMessage("Username or password is incorrect");
				break;
			case SIGNUP_SUCCESSFUL:
				userInterface.displayInfoMessage("Sign up successful. Please log in.");
				break;
			case SIGNUP_FAILED:
				userInterface.displayErrorMessage("Username already exists.");
				break;
			}
		}
		catch (Exception exception) {
			JOptionPane.showMessageDialog(null, "Cannot connect to the server. Please check your internet connection",
					"Connection Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/** Blocks the <code>getNewPort</code> and <code>getClientID</code> methods from executing
	 * until successful login. */
	private void waitForLogIn() {
		proceed.lock();
		
		try {
			if(!isLoggedIn)
				loginCompleted.await();
		} 
		catch (InterruptedException e) {
			System.err.println("Thread interrupted");
		}
		finally {
			proceed.unlock();
		}		
	}
	
	/** Closes the login screen. */ 
	public void closeUI() {
		userInterface.close();
	}
}
