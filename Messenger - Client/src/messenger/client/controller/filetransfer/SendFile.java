package messenger.client.controller.filetransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JFileChooser;

import messenger.client.view.ClientGUI;
import messenger.client.view.GUI;


public class SendFile implements Runnable
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

	
	public SendFile(String host, Integer port, ClientGUI userInterface)
	{
		this.server = host;
		this.serverPort = port;
		this.userInterface = userInterface;
	}
	
	private File getFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if(fileChooser.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
			return null;
		
		File file = fileChooser.getSelectedFile();
		if(file==null || file.getName().equals(""))
		{
			userInterface.displayInfoMessage("\nInvalid file name");
			return null;
		}
		
		return file;
	}
	
	/*private void updateProgressBar()
	{
		try
		{
			updateProgressbarLock.lock();
			
			uploadInfo.bytesSent += bytesRead;
			
			SwingUtilities.invokeLater
			(
				new Runnable()
				{	
					@Override
					public void run()
					{
						uploadBar.setValue((int)((100 * uploadInfo.bytesSent) / uploadInfo.filesSize));
					}
				}
			);
		}
		finally
		{
			updateProgressbarLock.unlock();
		}
	}*/
	
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
	
	private void sendFile() throws IOException
	{
		file = getFile();
		if(file==null)
			return;
		
		userInterface.displayInfoMessage("\nSending file " + file.getName());
		
		/*
		try
		{
			updateProgressbarLock.lock();
			
			uploadInfo.filesSize += file.length();
		}
		finally
		{
			updateProgressbarLock.unlock();
		}
		*/
		
		output.writeObject(file.getName());
		output.writeObject(BUFFER_SIZE);
		
		FileInputStream fileInputStream = new FileInputStream(file);
		
		byte[] buffer = new byte[BUFFER_SIZE];
		
		//updateProgressBar();
		while((bytesRead = fileInputStream.read(buffer)) > 0)
		{
			output.writeObject(bytesRead);
			output.writeObject(Arrays.copyOf(buffer, buffer.length));
			
			//updateProgressBar();
		}
		
		userInterface.displayInfoMessage("\nFile " + file.getName() + " sent");
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
		/*
		try
		{
			updateProgressbarLock.lock();
			
			uploadInfo.filesSending++;
			uploadBar.setVisible(true);
		}
		finally
		{
			updateProgressbarLock.unlock();
		}
		*/
		
		try
		{
			connectToServer();
			getStreams();
			sendFile();
		}
		catch(IOException exception)
		{
			System.err.println("\nError in I/O during file trasfer");
		}
		finally
		{
			closeConnection();
			
			/*try
			{
				updateProgressbarLock.lock();
				
				if(--uploadInfo.filesSending==0)
				{
					uploadInfo.filesSize = uploadInfo.bytesSent = 0L;
					uploadBar.setVisible(false);
				}
			}
			finally
			{
				updateProgressbarLock.unlock();
			}
			*/
		}
	}
}
