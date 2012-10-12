package messenger.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.sun.xml.internal.bind.v2.TODO;

import messenger.Constants;
import messenger.client.controller.ChatManager;
import messenger.client.controller.Client;

public class ClientGUI extends JFrame implements GUI, Constants {

	private JPanel contentPane;
	private JTabbedPane chatPane;
	private JTabbedPane userListPane;
	private JTextField searchField;
	private JLabel infoLabel;

	private HashMap<Integer, ChatPanel> connections = new HashMap<Integer, ChatPanel>();
	private ChatManager user;
	private int userID;

	private Map<Integer, String> allUserList;
	private Map<Integer, String> friendList;
	private FriendListPanel friendsPanel;
	private FriendListPanel allUsersPanel;

	/**
	 * Create the frame.
	 */
	public ClientGUI(final ChatManager user, int userID) {
		super("Messsenger - " + Client.clientName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 721, 492);

		this.user = user;
		this.userID = userID;

		allUserList = new HashMap<Integer, String>();
		friendList = new HashMap<Integer, String>();

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		Action newTabAction = new AbstractAction("Sign Out") {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		};
		newTabAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("Images/signout.png")));
		newTabAction.putValue(Action.SHORT_DESCRIPTION, "Sign out from this account");

		Action closeTabAction = new AbstractAction("Close Tab") {

			@Override
			public void actionPerformed(ActionEvent e) {
				int currentTabIndex = chatPane.getSelectedIndex();
				if (currentTabIndex >= 0)
					chatPane.remove(currentTabIndex);
			}
		};
		closeTabAction.putValue(Action.SMALL_ICON, new ImageIcon(getClass()
				.getResource("Images/close.png")));
		closeTabAction.putValue(Action.SHORT_DESCRIPTION,
				"Closes the current tab");

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

		JButton sendFileButton = new JButton(new ImageIcon(getClass().getResource(
				"Images/share.png")));
		sendFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatPanel chatPanel = (ChatPanel) chatPane.getSelectedComponent();
				if(chatPanel == null) {
					JOptionPane.showMessageDialog(chatPanel, "Please select one");
				} else {
					int recieverID = chatPanel.clientID;
					user.sendFile(recieverID);
				}
			}
		});
		sendFileButton.setBackground(Color.WHITE);
		toolBar.add(sendFileButton);

		searchField = new JTextField();
		searchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		searchField.setForeground(Color.BLACK);
		toolBar.add(searchField);
		searchField.setColumns(10);

		JButton searchButton = new JButton(new ImageIcon(getClass()
				.getResource("Images/search.png")));
		searchButton.setBackground(Color.WHITE);
		toolBar.add(searchButton);

		JButton emptyButton = new JButton(new ImageIcon(getClass().getResource(
				"Images/empty.png")));
		emptyButton.setBackground(Color.WHITE);
		emptyButton.setForeground(Color.WHITE);
		emptyButton.setDisabledIcon(new ImageIcon(getClass().getResource(
				"Images/empty.png")));
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

		chatPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(chatPane);

		userListPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(userListPane);

		friendsPanel = new FriendListPanel(this);
		allUsersPanel = new FriendListPanel(this);

		userListPane.add("All User", allUsersPanel);
		userListPane.add("Friends", friendsPanel);

		infoLabel = new JLabel("Welcome");
		infoLabel.setBackground(Color.WHITE);
		contentPane.add(infoLabel, BorderLayout.SOUTH);

		setVisible(true);
	}

	ChatPanel createNewTab(String title, int clientID) {

		ChatPanel panel = new ChatPanel(user, clientID);
		chatPane.add(title, panel);
		chatPane.setSelectedIndex(chatPane.countComponents() - 1);
		connections.put(clientID, panel);

		return panel;
	}

	/**
	 * When the user sends or receives a message, it shows that message
	 * 
	 * @param message
	 */
	public void displayMessage(final String message, final int clientID) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (clientID == ERROR_CODE) {
					displayInfoMessage(message);
				} else {
					ChatPanel clientTab = connections.get(clientID);
					String clientName = allUserList.get(clientID);
					/* If a new user is knocking */
					if (clientTab == null)
						clientTab = createNewTab(clientName, clientID);

					String firstName = clientName.split(" ")[0];
					clientTab.show(firstName + ": " + message );
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messenger.client.view.GUI#displayErrorMessage(java.lang.String)
	 */
	@Override
	public void displayErrorMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoLabel.setText(message);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see messenger.client.view.GUI#displayInfoMessage(java.lang.String)
	 */
	@Override
	public void displayInfoMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				infoLabel.setText(message);
			}
		});
	}

	public void setAllUserList(Map<Integer, String> allUserList) {
		this.allUserList = allUserList;
		allUsersPanel.setValues(allUserList);
	}

	public void setFriendList(Map<Integer, String> friendList) {
		this.friendList = friendList;
		friendsPanel.setValues(friendList);
	}

	@Override
	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
