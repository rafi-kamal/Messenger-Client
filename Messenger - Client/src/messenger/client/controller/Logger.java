package messenger.client.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import messenger.Constants;
import messenger.client.ClientConnection;
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
public class Logger implements Constants{
	
	/** Prevents the <code>getNewPort</code> and <code>getClientID</code> methods from executing 
	 * until the user logs in. */
	private final Lock proceed = new ReentrantLock();
	private final Condition loginCompleted = proceed.newCondition();
	private ObjectOutputStream output;
	private ObjectInputStream input;
	/** If the user is logged in. */
	private boolean isLoggedIn = false;
	private LoggingUI loggingUI;
	
	public Logger(ObjectOutputStream output, ObjectInputStream input) {
		this.output = output;
		this.input = input;
		loggingUI = new LoggingUI(this);
		loggingUI.setVisible(true);
	}

	
	/**
	 * Sends the server a login request. Server will process this request and sends a reply.
	 * The reply will be analyzed in the <code>processConnection</code> method.
	 * 
	 * @param userName User name of the client
	 * @param password Password of the user
	 * @param rememberMe If the client decides to be logged in this machine until he logs out
	 */
	public void logIn(String userName, String password, boolean rememberMe) {
		sendData(LOGIN_REQUEST);
		sendData(userName);
		sendData(password);
		processConnection();
	}
	
	/** 
	 * Sends the server a sign up request. Server will process this request and sends a reply.
	 * The reply will be analyzed in the <code>processConnection</code> method.
	 */
	public void signUp(String userName, String password) {
		sendData(SIGNUP_REQUEST);
		sendData(userName);
		sendData(password);
		processConnection();
	}

	public void processConnection() {
		try {
			int messageCode = (Integer) input.readObject();
			
			switch(messageCode) {
			case LOGIN_SUCCESSFUL:
				isLoggedIn = true;
				
				proceed.lock();
				loginCompleted.signal();
				proceed.unlock();
				break;
			case LOGIN_FAILED:
				loggingUI.displayErrorMessage("Username or password is incorrect");
				break;
			case SIGNUP_SUCCESSFUL:
				loggingUI.displayInfoMessage("Sign up successful. Please log in.");
				break;
			case SIGNUP_FAILED:
				loggingUI.displayErrorMessage("Username already exists.");
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
	public void waitForLogIn() {
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
	
	/** Sends data to the server. */
	public void sendData(Object message) {
		try {
			output.writeObject(message);
			output.flush();
		}
		catch(IOException ioException) {
			loggingUI.displayErrorMessage("Error sending data. Please try again");
		}
	}
	
	/** Closes the login screen. */ 
	public void closeUI() {
		loggingUI.close();
	}
}
