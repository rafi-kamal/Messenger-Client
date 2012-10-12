package messenger.client.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import messenger.client.controller.Logger;

public class LoggingUI extends JFrame implements GUI{

	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField newUserName;
	private JPasswordField newPasswordField;
	private JPasswordField confirmPasswordField;
	private JCheckBox checkboxRemeberMe;
	private JLabel signUpInfoLabel;
	private JLabel logInInfoLabel;

	/**
	 * Create the frame.
	 */
	public LoggingUI(final Logger logger) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 288, 356);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		final CardLayout layout = new CardLayout(0, 0);
		final String signUpCard = "signUp";
		final String logInCard = "logIn";
		contentPane.setLayout(layout);
		
		JPanel logInPanel = new JPanel();
		logInPanel.setBackground(Color.WHITE);
		contentPane.add(logInPanel, logInCard);
		
		JLabel title = new JLabel("Messenger");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("8Pin Matrix", Font.BOLD | Font.ITALIC, 14));
		title.setForeground(Color.BLACK);
		
		logInInfoLabel = new JLabel(" ");
		logInInfoLabel.setForeground(new Color(220, 20, 60));
		logInInfoLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new GridLayout(0, 2, 10, 0));
		
		JButton logInButton = new JButton("Log In");
		
		logInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String userName = usernameField.getText();
				String password = new String(passwordField.getPassword());
				boolean rememberMe = checkboxRemeberMe.isSelected();
				
				if(userName.equals(""))
					logInInfoLabel.setText("Please enter the username.");
				else if(password.equals(""))
					logInInfoLabel.setText("Please enter the password.");
				else {
					logInInfoLabel.setText("Logging in...");
					logger.logIn(userName, password, rememberMe);
				}
			}
		});
		buttonPanel.add(logInButton);
		
		JButton signUpButton = new JButton("Sign Up");
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layout.show(LoggingUI.this.getContentPane(), signUpCard);
			}
		});
		buttonPanel.add(signUpButton);
		
		JPanel usernamePasswordPanel = new JPanel();
		usernamePasswordPanel.setBackground(Color.WHITE);
		usernamePasswordPanel.setLayout(new GridLayout(2, 0, 0, 5));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		usernamePasswordPanel.add(panel_2);
		panel_2.setLayout(new GridLayout(2, 0, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Username:");
		panel_2.add(lblNewLabel);
		
		usernameField = new JTextField();
		panel_2.add(usernameField);
		usernameField.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		usernamePasswordPanel.add(panel_1);
		panel_1.setLayout(new GridLayout(2, 0, 0, 0));
		
		JLabel lblPassword = new JLabel("Password:");
		panel_1.add(lblPassword);
		
		passwordField = new JPasswordField();
		panel_1.add(passwordField);
		
		checkboxRemeberMe = new JCheckBox("Remeber Me");
		checkboxRemeberMe.setSelected(true);
		checkboxRemeberMe.setBackground(Color.WHITE);
		GroupLayout gl_logInPanel = new GroupLayout(logInPanel);
		gl_logInPanel.setHorizontalGroup(
			gl_logInPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_logInPanel.createSequentialGroup()
					.addGroup(gl_logInPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_logInPanel.createSequentialGroup()
							.addGap(29)
							.addComponent(logInInfoLabel, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
						.addGroup(gl_logInPanel.createSequentialGroup()
							.addGap(29)
							.addGroup(gl_logInPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(checkboxRemeberMe, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
								.addComponent(usernamePasswordPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
								.addComponent(title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_logInPanel.createSequentialGroup()
							.addContainerGap(29, Short.MAX_VALUE)
							.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)))
					.addGap(29))
		);
		gl_logInPanel.setVerticalGroup(
			gl_logInPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_logInPanel.createSequentialGroup()
					.addGap(40)
					.addComponent(title)
					.addGap(26)
					.addComponent(usernamePasswordPanel, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(checkboxRemeberMe)
					.addGap(28)
					.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(26)
					.addComponent(logInInfoLabel)
					.addGap(20))
		);
		logInPanel.setLayout(gl_logInPanel);
		
		JPanel signUpPanel = new JPanel();
		signUpPanel.setBackground(Color.WHITE);
		contentPane.add(signUpPanel, signUpCard);
		
		JLabel title2 = new JLabel("Messenger");
		title2.setHorizontalAlignment(SwingConstants.CENTER);
		title2.setForeground(Color.BLACK);
		title2.setFont(new Font("8Pin Matrix", Font.BOLD | Font.ITALIC, 14));
		
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setBackground(Color.WHITE);
		buttonPanel2.setLayout(new GridLayout(0, 2, 10, 0));
		
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName = newUserName.getText();
				String password = new String(newPasswordField.getPassword());
				String confirmPassword = new String(confirmPasswordField.getPassword());
				System.out.println(password);;
				
				if(userName.equals(""))
					signUpInfoLabel.setText("Please enter a username.");
				else if(password.length() < 4)
					signUpInfoLabel.setText("Password must be of 4 characters");
				else if(!password.equals(confirmPassword))
					signUpInfoLabel.setText("Password do not match.");
				else {
					layout.show(LoggingUI.this.getContentPane(), logInCard);
					logInInfoLabel.setText("Signing up...");
					usernameField.setText(userName);
					passwordField.setText(password);
					logger.signUp(userName, password);
				}
			}
		});
		buttonPanel2.add(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layout.show(LoggingUI.this.getContentPane(), logInCard);				
			}
		});
		buttonPanel2.add(cancelButton);
		
		JPanel usernamePasswordPanel2 = new JPanel();
		usernamePasswordPanel2.setBackground(Color.WHITE);
		usernamePasswordPanel2.setLayout(new GridLayout(3, 0, 0, 5));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		usernamePasswordPanel2.add(panel_3);
		panel_3.setLayout(new GridLayout(2, 0, 0, 0));
		
		JLabel label = new JLabel("Username:");
		panel_3.add(label);
		
		newUserName = new JTextField();
		newUserName.setColumns(10);
		panel_3.add(newUserName);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		usernamePasswordPanel2.add(panel_4);
		panel_4.setLayout(new GridLayout(2, 0, 0, 0));
		
		JLabel label_1 = new JLabel("Password:");
		panel_4.add(label_1);
		
		newPasswordField = new JPasswordField();
		panel_4.add(newPasswordField);
		
		signUpInfoLabel = new JLabel("           ");
		signUpInfoLabel.setForeground(Color.RED);
		GroupLayout gl_signUpPanel = new GroupLayout(signUpPanel);
		gl_signUpPanel.setHorizontalGroup(
			gl_signUpPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_signUpPanel.createSequentialGroup()
					.addGap(30)
					.addGroup(gl_signUpPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(signUpInfoLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(title2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(usernamePasswordPanel2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(buttonPanel2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		gl_signUpPanel.setVerticalGroup(
			gl_signUpPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_signUpPanel.createSequentialGroup()
					.addGap(41)
					.addComponent(title2)
					.addGap(29)
					.addComponent(usernamePasswordPanel2, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(buttonPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(signUpInfoLabel)
					.addGap(19))
		);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		usernamePasswordPanel2.add(panel);
		panel.setLayout(new GridLayout(2, 0, 0, 0));
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setBackground(Color.WHITE);
		panel.add(lblConfirmPassword);
		
		confirmPasswordField = new JPasswordField();
		panel.add(confirmPasswordField);
		signUpPanel.setLayout(gl_signUpPanel);
	}
	
	@Override
	public void displayInfoMessage(String message) {
		signUpInfoLabel.setText(message);
		logInInfoLabel.setText(message);
	}

	@Override
	public void displayErrorMessage(String message) {
		signUpInfoLabel.setText(message);
		logInInfoLabel.setText(message);		
	}
	
	public void close() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
