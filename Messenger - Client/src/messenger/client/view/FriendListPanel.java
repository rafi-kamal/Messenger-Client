package messenger.client.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FriendListPanel extends JPanel {
	
	ArrayList<String> values;
	private JList list;

	/**
	 * Create the panel.
	 */
	public FriendListPanel(final ClientGUI parent) {
		setLayout(new BorderLayout(0, 0));
		
		list = new JList();
		
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()) {
					try
					{
						int clientID = Integer.parseInt(list.getSelectedValue().toString());
						parent.createNewTab(clientID + "", clientID);
					}
					catch(Exception exception) 
					{
						parent.displayErrorMessage("Please enter a valid ID");
					}
				}
			}
		});
		add(new JScrollPane(list), BorderLayout.CENTER);
	}
	
	public void setValues(final ArrayList<String> values) {

		list.setModel(new AbstractListModel() {
			public int getSize() {
				return values.size();
			}
			public Object getElementAt(int index) {
				return values.get(index);
			}
		});
		
		repaint();
	}
}
