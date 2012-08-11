package messenger.client.controller;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import messenger.client.Connection;
import messenger.client.view.LoggingUI;

public class Logger extends Connection {
	
	private final Lock proceed = new ReentrantLock();
	private final Condition loginCompleted = proceed.newCondition();
	
	boolean isLoggedIn = false;
	
	public Logger(String serverIP, int serverPort) {
		super(serverIP, serverPort);
		userInterface = new LoggingUI(this);
		userInterface.setVisible(true);
	}

	private int newPort;
	
	/**
	 * Displays a login screen and interact with the server for logging in.<br>
	 * If user is has no account, he can sign in and open a new account.<p>
	 * 
	 * After signing in, the client will be assigned a specific port number from the server through which he can conduct further communication
	 * @return 
	 * @return Port number assigned to the client
	 */
	public void logIn(String userName, String password, boolean rememberMe) {
		setUpConnection(serverIP, serverPort);
		sendData(LOGIN_REQUEST);
		sendData(userName);
		sendData(password);
		processConnection();
		closeConnection();
	}
	
	public void signUp(String userName, String password) {
		setUpConnection(serverIP, serverPort);
		sendData(SIGNUP_REQUEST);
		sendData(userName);
		sendData(password);
		processConnection();
		closeConnection();
	}
	
	public int getNewPort() {
		if(!isLoggedIn)
			waitForLogIn();
		return newPort;
	}
	
	public int getClientID() {
		if(!isLoggedIn)
			waitForLogIn();
		return clientID;
	}

	@Override
	public void processConnection() {
		try {
			int messageCode = (Integer) input.readObject();
			
			switch(messageCode) {
			case LOGIN_SUCCESSFUL:
				newPort = (Integer) input.readObject();
				clientID = (Integer) input.readObject();
				isLoggedIn = true;
				
				proceed.lock();
				loginCompleted.signal();
				proceed.unlock();
				
				System.out.println("Login successful. Client ID: " + clientID + ". Reconnecting in port " + newPort);
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
	
	public void closeUI() {
		userInterface.close();
	}
}
