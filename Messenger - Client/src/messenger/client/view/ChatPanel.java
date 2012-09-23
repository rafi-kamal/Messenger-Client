package messenger.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import messenger.client.controller.ChatManager;

public class ChatPanel extends JPanel {
	private JTextArea showMessageArea;
	private JTextField writeMessageField;
	private int clientID;
	
	public ChatPanel(final ChatManager user, final int clientID) {
		this.clientID = clientID;

		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		setBackground(Color.WHITE);
		setLayout(new BorderLayout(5, 5));
		
		showMessageArea = new JTextArea();
		showMessageArea.setEditable(false);
		writeMessageField = new JTextField();
		if(clientID <= 0)
			writeMessageField.setEditable(false);
		
		writeMessageField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = writeMessageField.getText();
				user.sendData(message, clientID);
				writeMessageField.setText("");
				show("Me: " + message);
			}
		});
		
		add(new JScrollPane(showMessageArea), BorderLayout.CENTER);
		add(writeMessageField, BorderLayout.SOUTH);
	}
	
	/*public JTextArea getShowMessageArea() {
		return showMessageArea;
	}*/
	public void show(String message) {
		showMessageArea.append(message + "\n");
	}
}
