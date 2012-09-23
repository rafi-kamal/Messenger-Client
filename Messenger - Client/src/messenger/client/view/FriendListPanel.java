package messenger.client.view;

import java.awt.BorderLayout;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FriendListPanel extends JPanel {

	private JList<String> list;
	private Map<Integer, String> values;
	private Integer userIDs[];

	/**
	 * Create the panel.
	 */
	public FriendListPanel(final ClientGUI parent) {
		setLayout(new BorderLayout(0, 0));

		list = new JList<String>();

		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					try {
						int clientID = userIDs[list.getSelectedIndex()];
						String clientName = list.getSelectedValue();
						parent.createNewTab(clientName, clientID);
					} catch (Exception exception) {
						parent.displayErrorMessage("Please enter a valid ID");
					}
				}
			}
		});
		add(new JScrollPane(list), BorderLayout.CENTER);
	}

	public void setValues(final Map<Integer, String> values) {
		
		this.values = values;
		userIDs = values.keySet().toArray(new Integer[values.size()]);
		list.setModel(new AbstractListModel<String>() {

			public int getSize() {
				return values.size();
			}

			public String getElementAt(int index) {
				Integer userID = userIDs[index];
				return values.get(userID);
			}
		});

		repaint();
	}
}
