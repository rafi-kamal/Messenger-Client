package messenger.client.controller.filetransfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import messenger.client.view.ClientGUI;


public class RecieveFile implements Runnable
{	
	private final String server;
	private Integer serverPort;
	
	private File file;
	private final Integer BUFFER_SIZE = 100;
	private Socket connection;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	int bytesRead;
	private ClientGUI userInterface;

	
	public RecieveFile(String host, Integer port, ClientGUI userInterface)
	{
		this.server = host;
		this.serverPort = port;
		this.userInterface = userInterface;
	}
	
	/*
	private void updateProgressBar()
	{
		try
		{
			updateProgressBarLock.lock();
			
			downloadInfo.bytesRecieved += bytesRead;
			
			SwingUtilities.invokeLater
			(
				new Runnable()
				{					
					@Override
					public void run()
					{
						downloadBar.setValue((int)((100 * downloadInfo.bytesRecieved) / downloadInfo.filesSize));
					}
				}
			);
		}
		finally
		{
			updateProgressBarLock.unlock();
		}
	}
	*/
	
	private void connectToServer() throws IOException
	{
		connection = new Socket(server, serverPort);
	}
	
	private void getStreams() throws IOException
	{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
	}
	
	private void recieveFile() throws IOException, ClassNotFoundException
	{	
		String fileName = (String)input.readObject();
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);
		
		userInterface.displayInfoMessage("\nFile " + fileName + " being recieved");
		
		/*
		try
		{
			updateProgressBarLock.lock();
			
			downloadInfo.filesSize += (Long) input.readObject();
		}
		finally
		{
			updateProgressBarLock.unlock();
		}
		*/
		
		byte[] buffer = new byte[(Integer)input.readObject()];
		
		//updateProgressBar();
		do
		{
			bytesRead = (Integer)input.readObject();
			buffer = (byte [])input.readObject();
			
			fileOutputStream.write(buffer, 0, bytesRead);
			
			//updateProgressBar();
		}while(bytesRead==buffer.length);
		
		fileOutputStream.close();
		userInterface.displayInfoMessage("\nFile " + fileName + " recieved");
	}
	
	private void closeConnection()
	{
		try
		{
			input.close();
			output.close();
			connection.close();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public void run()
	{
		
		/*try
		{
			updateProgressBarLock.lock();
			
			downloadInfo.filesRecieving++;
			downloadBar.setVisible(true);
		}
		finally
		{
			updateProgressBarLock.unlock();
		}*/
		
		try
		{
			connectToServer();
			getStreams();
			recieveFile();
		}
		catch(IOException exception)
		{
			userInterface.displayErrorMessage("\nError in I/O during file trasfer\n");
		}
		catch(ClassNotFoundException exception)
		{
			userInterface.displayErrorMessage("\nInappropriate type of object recieved\n");
		}
		finally
		{
			closeConnection();
			
			/*
			try
			{
				updateProgressBarLock.lock();
				
				if(--downloadInfo.filesRecieving==0)
				{
					downloadInfo.filesSize = downloadInfo.bytesRecieved = 0L;
					downloadBar.setVisible(false);
				}
			}
			finally
			{
				updateProgressBarLock.unlock();
			}
			*/
		}
	}
}
