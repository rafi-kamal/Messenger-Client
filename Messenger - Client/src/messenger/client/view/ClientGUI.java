package messenger.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import messenger.Constants;
import messenger.client.controller.ChatManager;

public class ClientGUI extends JFrame implements GUI, Constants {

	private JPanel contentPane;
	private JTabbedPane chatPane;
	private JTabbedPane friendListPane;
	private JTextField searchField;
	private JLabel infoLabel;
	
	private HashMap<Integer, ChatPanel> connections = new HashMap<Integer, ChatPanel>(5);
	private ChatManager user;
	private int userID;
	
	private ArrayList<String> allUserList;
	private ArrayList<String> friendList;
	private FriendListPanel friendsPanel;
	private FriendListPanel allUsersPanel; 

	/**
	 * Create the frame.
	 */
	public ClientGUI(final ChatManager user, int userID) {
		super("Messsenger");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 721, 492);
		
		this.user = user;
		this.userID = userID;
		
		allUserList = new ArrayList<String>();
		friendList = new ArrayList<String>();
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		Action newTabAction = new AbstractAction("New Tab") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewTab();
			}
		};
		newTabAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("Images/new.png")));
		newTabAction.putValue(Action.SHORT_DESCRIPTION, "Opens a new tab");
		
		Action closeTabAction = new AbstractAction("Close Tab") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int currentTabIndex = chatPane.getSelectedIndex();
				if(currentTabIndex >= 0)
					chatPane.remove(currentTabIndex);
			}
		};
		closeTabAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("Images/close.png")));
		closeTabAction.putValue(Action.SHORT_DESCRIPTION, "Closes the current tab");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		mnFile.add(mntmExit);
		
		JMenu windowMenu = new JMenu("Window");
		menuBar.add(windowMenu);
		
		JMenuItem newTabMenuItem = new JMenuItem(newTabAction);
		windowMenu.add(newTabMenuItem);
		
		JMenuItem closeTabMenuItem = new JMenuItem(closeTabAction);
		windowMenu.add(closeTabMenuItem);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 5));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(Color.WHITE);
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton showInfoButton = new JButton(new ImageIcon(getClass().getResource("Images/Info.png")));
		showInfoButton.setBackground(Color.WHITE);
		toolBar.add(showInfoButton);
		
		JButton shareButton = new JButton(new ImageIcon(getClass().getResource("Images/share.png")));
		shareButton.setBackground(Color.WHITE);
		toolBar.add(shareButton);
		
		JButton addButton = new JButton(new ImageIcon(getClass().getResource("Images/add.png")));
		addButton.setBackground(Color.WHITE);
		toolBar.add(addButton);
		
		searchField = new JTextField();
		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int clientID = Integer.parseInt(searchField.getText());
					createNewTab(clientID + "", clientID);
					searchField.setText("");
				}
				catch(Exception exception) {
					displayErrorMessage("Please enter a valid ID");
				}
				
			}
		});
		searchField.setForeground(Color.BLACK);
		toolBar.add(searchField);
		searchField.setColumns(10);
		
		JButton searchButton = new JButton(new ImageIcon(getClass().getResource("Images/search.png")));
		searchButton.setBackground(Color.WHITE);
		toolBar.add(searchButton);
		
		JButton emptyButton = new JButton(new ImageIcon(getClass().getResource("Images/empty.png")));
		emptyButton.setBackground(Color.WHITE);
		emptyButton.setForeground(Color.WHITE);
		emptyButton.setDisabledIcon(new ImageIcon(getClass().getResource("Images/empty.png")));
		emptyButton.setEnabled(false);
		toolBar.add(emptyButton);
		
		JButton newTabButton = new JButton(newTabAction);
		newTabButton.setBackground(Color.WHITE);
		newTabButton.setText("");
		toolBar.add(newTabButton);
		
		JButton closeTabButton = new JButton(closeTabAction);
		closeTabButton.setBackground(Color.WHITE);
		closeTabButton.setText("");
		toolBar.add(closeTabButton);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(Color.WHITE);
		splitPane.setDividerLocation(200);
		splitPane.setOneTouchExpandable(true);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		friendListPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(friendListPane);
		
		chatPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(chatPane);
		
		friendsPanel = new FriendListPanel(this);
		allUsersPanel = new FriendListPanel(this);
		
		friendListPane.add("Friends", friendsPanel);
		friendListPane.add("All User", allUsersPanel);
		
		infoLabel = new JLabel("Welcome");
		infoLabel.setBackground(Color.WHITE);
		contentPane.add(infoLabel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	void createNewTab(String title, int clientID) {
		
		ChatPanel panel = new ChatPanel(user, clientID);
		chatPane.add(title, panel);
		chatPane.setSelectedIndex(chatPane.countComponents() - 1);
		
		if(clientID > 0)
			connections.put(clientID, panel);
	}
	
	private void createNewTab() {
		createNewTab("New Tab", 0);
	}
	
	/**
	 * When the user sends or receives a message, it shows that message
	 * @param message
	 */
	public void displayMessage(final String message, final int clientID) {
		
		SwingUtilities.invokeLater(
				new Runnable() {					
					@Override
					public void run() {
						if(clientID == ERROR_CODE) {
							displayInfoMessage(message);
						}
						else {
							ChatPanel clientTab = connections.get(clientID);
							System.out.println(clientID);
							if(clientTab != null)
								clientTab.getShowMessageArea().append(message + "\n");
						}
					}
				}
			);
	}
	
	/* (non-Javadoc)
	 * @see messenger.client.view.GUI#displayErrorMessage(java.lang.String)
	 */
	@Override
	public void displayErrorMessage(final String message)
	{
		SwingUtilities.invokeLater(
				new Runnable()  {					
					@Override
					public void run() {
						infoLabel.setText(message);
					}
				}
			);
	}
	
	/* (non-Javadoc)
	 * @see messenger.client.view.GUI#displayInfoMessage(java.lang.String)
	 */
	@Override
	public void displayInfoMessage(final String message) {
		SwingUtilities.invokeLater(
				new Runnable() {					
					@Override
					public void run() {
						infoLabel.setText(message);
					}
				}
			);
	}
	
	public void setAllUserList(ArrayList<String> allUserList) {
		this.allUserList = allUserList;
		allUsersPanel.setValues(allUserList);
	}
	
	public void setFriendList(ArrayList<String> friendList) {
		this.friendList = friendList;
		friendsPanel.setValues(friendList);
	}

	@Override
	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));		
	}
}
